;;; Exercise 1

(def multiples
     (fn [n] (range (* n 2) 100 n)))


;;; Exercise 2

(use 'clojure.algo.monads)

(with-monad sequence-m
  (domonad [i (range 2 11)  ; You only need to try up to the square root of 100.
            nonprimes (multiples i)]
     nonprimes))


;;; Exercise 3

(def nonprimes
     (set (with-monad sequence-m
            (domonad [i (range 2 11)
                      nonprimes (multiples i)]
                     nonprimes))))
(pprint (remove nonprimes (range 2 100)))


;;; Exercise 4

(def monadifier list)

(def decider
     (fn [monadic-value continuation]
       (let [maybe-ified-continuation
             (fn [binding-value]
               (if (nil? binding-value)
                 (monadifier binding-value)
                 (continuation binding-value)))]
         (apply concat (map maybe-ified-continuation monadic-value)))))

(defmonad maybe-sequence-monad
  [m-result monadifier
   m-bind decider])
