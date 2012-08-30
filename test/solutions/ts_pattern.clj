(ns solutions.ts-pattern
  (:use midje.sweet))

(load-file "solutions/pattern.clj")

(fact
  (count-sequence [:a :b :c]) => 3)
