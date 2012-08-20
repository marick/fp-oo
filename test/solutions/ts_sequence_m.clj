(ns solutions.ts-sequence-m
  (:use midje.sweet))

(load-file "solutions/sequence-m.clj")

(fact 
  (with-monad combined-monad
    (domonad [a [1 2 3]
              b [-1 1]]
             (* a b)))
  => [-1 1 -2 2 -3 3])

(fact 
  (with-monad combined-monad
    (domonad [a [1 nil 3]
              b [-1 1]]
             (* a b)))
  => [-1 1 nil -3 3])

(fact 
  (with-monad combined-monad
    (domonad [a [1 nil 3]
              b [-1 1]]
             (* a b)))
  => 
  (with-monad (maybe-t sequence-m)
    (domonad [a [1 nil 3]
              b [-1 1]]
             (* a b))))
