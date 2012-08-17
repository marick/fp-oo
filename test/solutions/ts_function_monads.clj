(ns solutions.ts-function-monads
  (:use midje.sweet))


(load-file "solutions/pieces/function-monads-3-4.clj")

(fact (run-and-charge-and-speak 3) => {:charge 4, :result 104})

(fact (transform-state-example 1) => {:state 2, :result 1})

(load-file "solutions/pieces/function-monads-5.clj")

(fact
  (map-state-example {:a 1, :b 2, :c 3})
  => {:state {:a 1, :b 3, :c 4} :result [1 2 3]})
