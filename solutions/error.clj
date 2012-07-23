(use 'clojure.algo.monads)

(def use-value-to-decide-what-to-do
     (fn [value continuation]
       (if (oopsie? value)
         value
         (continuation value))))

(defmonad error-monad
  [m-result identity
   m-bind   use-value-to-decide-what-to-do])

(def factorial
     (fn [n]
       (cond (< n 0)
             (oops! "Factorial can never be less than zero." :number n)
             
             (< n 2)
             1
             
             :else
             (* n (factorial (dec n))))))
                 
(domonad error-monad
         [a -1
          b (factorial a)
          c (factorial (- a))]
   (* a b c))
          
