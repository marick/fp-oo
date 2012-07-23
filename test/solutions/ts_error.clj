(ns solutions.ts-error
  (:use midje.sweet))

(load-file "sources/maybe.clj")
(load-file "solutions/error.clj")

(fact
  (let [result (domonad error-monad
                        [a -1
                         b (factorial a)
                         c (factorial (- a))]
                  (* a b c))]
    result =>  {:reason "Factorial can never be less than zero.", :number -1}
    (type result) => :error))

