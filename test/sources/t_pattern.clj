(ns sources.t-pattern
  (:use midje.sweet))

(load-file "sources/pattern.clj")

(fact
  (count-sequence 0 [:a :b :c]) => 3)

