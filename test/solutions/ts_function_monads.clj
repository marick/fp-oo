(ns solutions.ts-function-monads
  (:use midje.sweet))


(load-file "solutions/function-monads.clj")

(fact (run-and-charge-and-speak 3) => {:charge 4, :result 104})
