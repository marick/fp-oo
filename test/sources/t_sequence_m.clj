(ns sources.t-sequence-m
  (:use midje.sweet))

(load-file "sources/sequence-m.clj")

(fact
 (with-monad sequence-monad
   (domonad [a [1 2]
             b [10, 100]
             c [-1 1]]
            (* a b c)))
 =>
 (with-monad sequence-m
      (domonad [a [1 2]
                b [10, 100]
                c [-1 1]]
               (* a b c))))

(fact
 (with-monad sequence-monad
   (domonad [a (list 1 2 3)
             b (list (- a) a)
             c (list (+ a b) (- a b))]
            (* a b c)))
 => 
 (with-monad sequence-m
   (domonad [a (list 1 2 3)
             b (list (- a) a)
             c (list (+ a b) (- a b))]
            (* a b c))))
