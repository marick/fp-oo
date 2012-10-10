(use 'patterned.sweet)

(def add-points
     (fn [one two]
       [ (+ (first one) (first two))
         (+ (second one) (second two))]))

(def add-points-2
     (fn [ [x0 y0] [x1 y1] ]
       [ (+ x0 x1) (+ y0 y1) ]))



(def factorial
     (fn [n]
       (cond (zero? n)  1
             (= 1 n)    1
             :else      (* n (factorial (dec n))))))

(defn factorial-2
  [n]
  (cond (zero? n)  1
        (= 1 n)    1
        :else      (* n (factorial-2 (dec n)))))

(defn oops! [& rest] :oops)

(defpatterned factorial-3
  [0]  1
  [1]  1
  [n]  (* n (factorial-3 (dec n))))

(defpatterned factorial-4
  [(:when neg? :bind n)] (oops! "No negative numbers" :n n)
  [0]  1
  [1]  1
  [n]  (* n (factorial-4 (dec n))))

(defpatterned factorial-5
  [(:when neg? :bind n)] (oops! "No negative numbers" :n n)
  [(:in [0 1])]  1
  [n]            (* n (factorial-5 (dec n))))



(defpatterned count-sequence
  [  []            ] 0
  [  [head & tail] ] (inc (count-sequence tail)))

