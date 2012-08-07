(use 'clojure.algo.monads)

(def decider
     (fn [value continuation]
       (if (oopsie? value)
         value
         (continuation value))))

(def error-monad
     (monad [m-result identity
             m-bind   decider]))

(def factorial
     (fn [n]
       (cond (< n 0)
             (oops! "Factorial can never be less than zero." :number n)
             
             (< n 2)
             1
             
             :else
             (* n (factorial (dec n))))))

(with-monad error-monad
  (domonad [a -1
            b (factorial a)
            c (factorial (- a))]
     (* a b c)))
          
