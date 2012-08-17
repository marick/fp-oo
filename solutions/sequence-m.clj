;;; Exercise 1

(def multiples
     (fn [n] (range (* n 2) 101 n)))


;;; Exercise 2

(use 'clojure.algo.monads)


(def nonprimes
     (with-monad sequence-m
       (domonad [i (range 2 11)  ; You only need to try up to the square root of 100.
                 nonprimes (multiples i)]
                nonprimes)))


;;; Exercise 3

(def primes
     (remove (set nonprimes) (range 2 101)))
(prn "Behold! Primes:")
(prn primes)


;;; Exercise 4

(def combined-monadifier list)

(def combined-decider
     (fn [monadic-value continuation]
       (let [maybe-ified-continuation
             (fn [binding-value]
               (if (nil? binding-value)
                 (combined-monadifier binding-value)
                 (continuation binding-value)))]
         (mapcat maybe-ified-continuation monadic-value))))

(defmonad combined-monad
  [m-result combined-monadifier
   m-bind combined-decider])

(prn 
 (with-monad combined-monad
   (domonad [a [1 nil 3]
             b [-1 1]]
            (* a b))))
