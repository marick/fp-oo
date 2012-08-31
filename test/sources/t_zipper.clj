(ns sources.t-zipper
  (:use midje.sweet))

(load-file "sources/zipper-text.clj")

(fact
  (flattenize '(1 [2 3] 4 (5 (6 (7))))) => '(1 [2 3] 4 5 6 7))

(fact
  (tumult-just-plus '(- ( (+ 3)))) => '(- ( (PLUS 3)))
  (tumult-append-node '(+ (- 3 4 (- 5 (+ 6))))) => '(PLUS (- 3 4 (- 5 (PLUS 6) 55555) 55555))
  (tumult-flip-args '(+ (/ 3 (- 4 5)))) => '(PLUS (/ (- 4 5 55555) 3))
  (tumult-flip-args '(/ (+ 1 2) (+ 4 (- 4)))) => '(/ (PLUS 4 (- 4 55555)) (PLUS 1 2))
  (tumult-flip-args '(/ (+ 1 2) 4 EXTRA)) => '(/ 4 (PLUS 1 2) EXTRA))


(load-file "sources/zipper.clj")

(fact
  (flattenize '(1 [2 3] 4 (5 (6 (7))))) => '(1 [2 3] 4 5 6 7))

(fact
  (tumult '(* 1 2)) => '(/ (PLUS 3 (- 0 9999 55555)) 1)
  (tumult '(- 3 (+ 6 (+ 3 4) (* 2 1) (/ 8 3))))
  => '(- 3 (PLUS 6 (PLUS 3 4) (/ (PLUS 3 (- 0 9999 55555)) 1) (/ 3 8)) 55555)
  (tumult '(/ (+ 1 2) (+ 4 (- 4)))) => '(/ (PLUS 4 (- 4 55555)) (PLUS 1 2))
  (tumult '(/ (+ 1 2) 4 EXTRA)) => '(/ 4 (PLUS 1 2) EXTRA))
