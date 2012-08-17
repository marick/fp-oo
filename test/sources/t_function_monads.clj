(ns sources.t-function-monads
  (:use midje.sweet))


(load-file "sources/function-monads.clj")

(fact (calculation) => (+ 8 88 8))

(fact (run-and-charge 3) => {:charge 5 :result (+ 8 88 8)})
(fact (run-and-charge-and-speak 3) => (run-and-charge 3))

(fact (calculation-with-initial-state -88) => {:state 88, :result "original state -88 was set to 88"})
(fact (mixer -87) => {:state 2 :result [-87 1 2]})
