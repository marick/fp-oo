(use 'clojure.algo.monads)


Exercise 

Logging monad
Identity monad.
Compare to `if`
show how logging works by changing monad-name

Show how to lift functions.
Manual definition of lift in terms of patcher and decider
Then describe lift

Make a logging increment.
Make a nil-protected inc

(defn calculation1 [] 3)
(defn calculation2 [a] -8888)
(defn calculation3 [a b] (+ a b))


(with-monad logging-monad
  (domonad [a (calculation1)
            b (calculation2 a)
            c (calculation3 a b)]
    (+ a b c)))

(let [a (calculation1)
      _ (println a)
      b (calculation2 a)
      _ (println b)
      c (calculation3 a b)]
  (println c)
  (+ a b c))

(with-monad logging-monad
  (domonad [a [1 2 3]
            b [4 5 6]]
    (+ a b)))




( (with-monad logging-m (m-lift 1 inc)) 1)
(1 2)
user=> ( (with-monad maybe-m (m-lift 1 inc)) nil)
( (with-monad maybe-m (m-lift 1 inc)) nil)
nil

user=> (def mapinc (with-monad sequence-m (m-lift 1 inc)))
user=> (mapinc [1 2 3])
(mapinc [1 2 3])

Define a weaker version of `fnil` (that only works with one-element functions)






Monad composition

user=> (def f (maybe-t sequence-m))
user=> (with-monad f (domonad [a [1 2 3] b [2 nil]] (* a b)))
(2 5 4 10 6 15)
user=> (with-monad f (domonad [a [1 2 3] b [2 nil]] (* a b)))
(2 nil 4 nil 6 nil)

(with-monad (maybe-t sequence-m)
  (domonad [a [1 nil 3]
            b [(inc a) (dec a)]]
     (* a b)))      
   


(def patcher
     (fn [result]
       (fn [] result)))

(def decider
     (fn [value continuation]
       (identity
        (continuation value))))

(defmonad functional-m
  [m-result patcher
   m-bind   decider])

(def m (domonad functional-m
                [a (println 1)
                 b (println 2)
                 c (println 3)]
                nil))
(m)

(def m (domonad functional-m
                [a 1
                 b (inc a)
                 c (+ a b)]
         [a b c]))
(m)


;;;; -------------

;;; Logging monad

(with-monad identity-m 
  (domonad [a 1
            b (inc a)
            c (+ a b)]
         (+ a b c)))

(def decider
     (fn [step-value continuation]
       (cons step-value (continuation step-value))))

(def patcher
     (fn [step-value]
       (list step-value)))

(defmonad logging-m
  [m-result patcher
   m-bind   decider])

(with-monad logging-m 
  (domonad [a 1
            b (inc a)
            c (+ a b)]
         (+ a b c)))

;;;; -------------







(defn result [step-value] (fn [state] [step-value state]))

(defn bind [decision-value continuation]
  (fn [state]
    (let [[new-step-value new-state] (decision-value state)]
      ((continuation new-step-value) new-state))))



(-> ["hi!" [1 2 3]]
    (decider
     (fn [a]
       (-> 
           (decider 
            (fn [b]
              (-> [8 9]
                  (decider
                   (fn [c]
                     (patcher     ;; <= New
                      (* a b c)))))))))))
