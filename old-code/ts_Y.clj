(ns solutions.ts-Y
  (:use midje.sweet))

(load-file "solutions/pieces/Y-1.clj")

(fact 
  ( (recurser-core (partial >= 1) (constantly 1) * dec) 
    5) => 120)

(load-file "sources/Y.clj")
(load-file "solutions/pieces/Y-2.clj")

(fact 
  ( (recurser-core (partial >= 1) (constantly 1) * dec) 
    5) => 120)

(load-file "solutions/pieces/Y-3.clj")

(def factorial
     (recurser :stop-when (partial >= 1)
               :ending-value 1
               :combiner *
               :reducer dec))

(fact (factorial 5) => 120)
