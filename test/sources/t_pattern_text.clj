(ns sources.t-pattern-text
  (:use midje.sweet))

(load-file "sources/pattern-text.clj")

(fact
  (add-points [1 2] [3 4]) => [4 6]
  (add-points-2 [1 2] [3 4]) => [4 6])


(fact
  (factorial 0) => 1
  (factorial 1) => 1
  (factorial 5) => 120

  (factorial-2 0) => 1
  (factorial-2 1) => 1
  (factorial-2 5) => 120

  (factorial-3 0) => 1
  (factorial-3 1) => 1
  (factorial-3 5) => 120

  (factorial-4 -1) => :oops
  (factorial-4 0) => 1
  (factorial-4 1) => 1
  (factorial-4 5) => 120

  (factorial-5 -1) => :oops
  (factorial-5 0) => 1
  (factorial-5 1) => 1
  (factorial-5 5) => 120)

(fact
  (count-sequence [:a :b :c]) => 3)



  
