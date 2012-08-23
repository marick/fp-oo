(ns solutions.ts-zipper
  (:use midje.sweet))

(load-file "solutions/pieces/zipper-1-3.clj")

(fact
  (all-vectors '(fn [a b] (concat [a] [b]))) => '([a b] [a] [b])
  (all-vectors-2 '(fn [a b] (concat [a] [b]))) => '([a b] [a] [b])
  (first-vector '(fn [a b] (concat [a] [b]))) => '[a b]
  (first-vector '(+ 1 (* 3 4))) => nil)

(fact
  (tumult '(- ( (+ 3)))) => '(- ( (PLUS 3)) 55555)
  (tumult '(+ (- 3 4 (- 5 (+ 6))))) => '(PLUS (- 3 4 (- 5 (PLUS 6) 55555) 55555))
  (tumult '(+ (/ 3 (- 4 5)))) => '(PLUS (/ (- 4 5 55555) 3))
  (tumult '(* 1 2)) => '(/ (PLUS 3 (- 0 9999 55555)) 1)
  (tumult '(- 3 (+ 6 (+ 3 4) (* 2 1) (/ 8 3))))
  => '(- 3 (PLUS 6 (PLUS 3 4) (/ (PLUS 3 (- 0 9999 55555)) 1) (/ 3 8)) 55555)
  (tumult '(/ (+ 1 2) (+ 4 (- 4)))) => '(/ (PLUS 4 (- 4 55555)) (PLUS 1 2))
  (tumult '(/ (+ 1 2) 4 EXTRA)) => '(/ 4 (PLUS 1 2) EXTRA))
