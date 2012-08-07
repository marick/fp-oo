 (use 'clojure.algo.monads)

(domonad maybe-m
         [a (+ 1 2)
          b nil
          c (+ a b)]
         c)

(def oops!
     (fn [reason & args]
       (with-meta (merge {:reason reason}
                         (apply hash-map args))
                  {:type :error})))
     

(defmonad error-m
  [m-result identity
   m-bind   (fn [value continue] 
              (if (= (type value) :error) value (continue value)))  ])


(def factorial
     (fn [n]
       (cond (< n 0)
             (oops! "Factorial can never be less than zero." :number n)
             
             (< n 2)
             1
             
             :else
             (* n (factorial (dec n))))))
                 
(domonad error-m
         [a -1
          b (factorial a)
          c (factorial (- a))]
   (* a b c))
          
     

(domonad error-m
         [step1-value (+ 1 2)
          step2-value (* step1-value 3)
          step3-value (+ step2-value 4)]
   step3-value)

         
(defn make [computation with value] (computation value))

(def step3-value (fn [step2-value] (* step2-value 6)))
(def step2-value (fn [step1-value] (+ 1 step1-value)))
(def step1-value (fn [] (+ 1 2)))

(step1-value)
(step2-value (step1-value))
(step3-value (step2-value (step1-value)))


(+ (* (+ 1 2) 3) 4)

(-> (+ 1 2)
    (fn [step1-value]
      (+ (* step1-value 3) 4)))

(-> (+ 1 2)
    (fn [step1-value]
      (-> (* step1-value 3)
          (fn [step2-value]
            (+ step2-value 4)))))

(let [step1-value (+ 1 2)
      step2-value (* step1-value 3)]
  (+ step2-value 4))
      
      step3-value (+ step2-value 4)]
  step3-value)

(-> (+ 1 2)
    (fn [step1-value]
      (-> (* step1-value 3)
          (fn [step2-value]
            (-> (+ step2-value step1-value)
                (fn [step3-value]
                  (* step2-value step3-value)))))))

(let [step1-value (+ 1 2)
      step2-value (* step1-value 3)]

(def use-value-to-decide-what-to-do
     (fn [value continuation]
       (if (= :error (type value))
         value
         (continuation value))))


(def use-value-to-decide-what-to-do
     (fn [value continuation]
       (continuation value)))

(def use-value-to-decide-what-to-do
     (fn [value continuation]
       (if (nil? value)
         nil
         (continuation value))))

(-> (function-that-returns-nil)
    (fn [step1-value]
      (+ step1-value 3)))

(-> (function-that-returns-nil)
    (use-value-to-decide-what-to-do
     (fn [step1-value]
       (+ step1-value 3))))





(-> (+ 1 2)
    (use-value-to-decide-what-to-do
     (fn [step1-value]
       (-> (* step1-value 3)
           (use-value-to-decide-what-to-do 
            (fn [step2-value]
              (+ step2-value step1-value)))))))

(defmonad error-m
  [m-result identity
   m-bind   (fn [value continuation] 
              (if (= (type value) :error)
                value
                (continuation value)))  ])


(defmonad error-m
  [m-result identity
   m-bind   (fn [value continue] 
              (if (= (type value) :error) value (continue value)))  ])



;;;;

(domonad sequence-m
         [a [1 2 3]
          b [-1 1]]
   (* a b))


(def use-value-to-decide-what-to-do
     (fn [value continuation]
       (map continuation value)))

(-> [1 2 3]
    (use-value-to-decide-what-to-do
      (fn [a]
        (-> [-1 1]
            (use-value-to-decide-what-to-do 
              (fn [b]
                (* a b)))))))

(def patch-final-result
     (fn [result]
       (apply concat result)))

(-> [1 2 3]
    (use-value-to-decide-what-to-do
     (fn [a]
       (-> [-1 1]
           (use-value-to-decide-what-to-do 
            (fn [b]
              (-> [8 9]
                  (use-value-to-decide-what-to-do
                   (fn [c]
                     (* a b c))))))))))

(def patch-innermost-result list)

(-> [1 2 3]
    (use-value-to-decide-what-to-do
     (fn [a]
       (-> [-1 1]
           (use-value-to-decide-what-to-do 
            (fn [b]
              (-> [8 9]
                  (use-value-to-decide-what-to-do
                   (fn [c]
                     (patch-innermost-result
                      (* a b c)))))))))))

(domonad multiple-nesting-m
         [a [1 2 3]
          b [-1 1]
          c [8 9]]
  (* a b c))
          
(for [a [1 2 3]
      b [-1 1]
      c [8 9]]
  (* a b c))

(defmonad nested-loop-m
  [m-result list
   m-bind (fn [value continuation]
            (apply concat (map continuation value)))])

(domonad nested-loop-m
         [a [1 2 3]
          b [-1 1]]
  (* a b))


;;;;;;
(defmonad work-with-wrapped-ints-m
  [m-result list
   m-bind   (fn [value continue]
              (continue (first value)))])

(def inc-and-wrap (comp list inc))
(def double-and-wrap (comp list (partial * 2)))


(domonad work-with-wrapped-ints-m
         [a (list 1)
          b (double-and-wrap a)
          c (inc-and-wrap b)]
         c)



(def tolerant-inc
     (fn [n]
       (if (nil? n)
         1
         (inc n))))

(def nil-patch
     (fn [function replacement]
       (fn [original]
         (if (nil? original)
           (function replacement)
           (function original)))))


(def nil-patch
     (fn [function replacement]
       (fn [original]
         (function (or original replacement)))))
     
(when-let 

(domonad maybe-m
  [step1-value (function-that-might-produce-nil 1)
   step2-value (* (inc step1-value) 3)]
  (dec step2-value))




(-> [1 2 3]
    (use-value-to-decide-what-to-do
     (fn [a]
       (-> [-1 1]
           (use-value-to-decide-what-to-do 
            (fn [b]
              (-> [8 9]
                  (use-value-to-decide-what-to-do
                   (fn [c]
                     (* a b c))))))))))

(domonad sequence-m
         [a [1 2 3]
          b [-1 1]
          c [8 8]]
     (* a b c))
