;;; Exercise 1

(def multiples
     (fn [n] (range (* n 2) 101 n)))


;;; Exercise 2

(use 'clojure.algo.monads)

;; The Sequence monad version
(def nonprimes
     (with-monad sequence-m
       (domonad [i (range 2 11)  ; You only need to try up to the square root of 100.
                 nonprime (multiples i)]
                nonprime)))

;; The `for` version
(def nonprimes
     (for [i (range 2 11)
           nonprime (multiples i)]
       nonprime))

;;; Exercise 3

(def primes
     (remove (set nonprimes) (range 2 101)))
(prn "Behold! Primes:")
(prn primes)

