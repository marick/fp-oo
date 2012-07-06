(ns solutions.ts-recursion
  (:use midje.sweet))

(load-file "solutions/pieces/recursion-1.clj")

(fact
  (factorial 0) => 1
  (factorial 1) => 1
  (factorial 5) => 120)

(load-file "solutions/pieces/recursion-2.clj")

(fact
  (factorial 0) => 1
  (factorial 1) => 1
  (factorial 5) => 120)

(load-file "solutions/pieces/recursion-3.clj")

(fact
  (recursive-function [] 0) => 0
  (recursive-function [1 2 3 4] 0) => 10)

(load-file "solutions/pieces/recursion-4a.clj")

(fact
  (recursive-function [] 1) => 1
  (recursive-function [1 2 3 4] 1) => 24)

(load-file "solutions/pieces/recursion-4b.clj")

(fact
  (recursive-function + [] 0) => 0
  (recursive-function + [1 2 3 4] 0) => 10
  (recursive-function * [] 1) => 1
  (recursive-function * [1 2 3 4] 1) => 24)

(load-file "solutions/pieces/recursion-5.clj")

(fact
  (recursive-function (fn [elt so-far]
                        (assoc so-far elt 0))
                      [:a :b :c]
                      {})
  => {:a 0, :b 0, :c 0})

(fact
  (recursive-function (fn [elt so-far]
                        (assoc so-far elt (count so-far)))
                      [:a :b :c]
                      {})
  => {:a 0, :b 1, :c 2})


(load-file "solutions/pieces/recursion-6.clj")


