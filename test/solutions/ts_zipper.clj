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


(load-file "solutions/pieces/zipper-4.clj")

(fact
  (tumult '(* 1 2)) => '(- 1 2 55555))


(load-file "solutions/pieces/zipper-5.clj")

(fact
  (let [zipper (zip/seq-zip '(fact))]
    (at? zipper 'fact) => falsey
    (at? (zip/down zipper) 'fact) => truthy
    (at? (zip/down zipper) 'facts 'fact) => truthy))


(fact
  (transform '(fact)) => '(do)
  (transform '(facts 1 2)) => '(do 1 2)
  (transform '(fact (+ 1 2) => 3)) => '(do (expect (+ 1 2) => 3))
  (transform '(facts
                (+ 1 2) => (+ 2 1)
                3 => (identity odd?)
                3 => odd?
                (+ 1 2) => odd?))
  => '(do
        (expect (+ 1 2) => (+ 2 1))
        (expect 3 => (identity odd?))
        (expect 3 => odd?)
        (expect (+ 1 2) => odd?))
  (transform '(fact
                (let [a [1 2]]
                  (first a) => 1
                  (second a) => 2)))
  => '(do
        (let [a [1 2]]
          (expect (first a) => 1)
          (expect (second a) => 2))))
                
(load-file "solutions/pieces/zipper-6-7.clj")

(fact
  (let [run (fn [form]
              (-> form zip/seq-zip zip/down skip-to-rightmost-leaf zip/node))]
    
    (run '(a)) => 'a
    (run '(a (b))) => 'b
    (run '(((a)))) => 'a
    (run '(a (a ()))) => '()))

(fact
  (transform '('(a => b) => c)) => '( (expect '(a => b) => c) )
  (transform '('(a => b) => '(c d))) => '( (expect '(a => b) => '(c d)) ))
  
(load-file "solutions/pieces/zipper-8.clj")

(let [original '(fact
                  (function-under-test 3) => -88
                  (provided
                    (subsidiary-function 3) => 88))
      expected '(do
                  (expect (function-under-test 3) => -88
                          (fake (subsidiary-function 3) => 88)))]
  (fact (transform original) => expected))



(let [original '(fact
                  (function-under-test 3) => -88
                  (provided
                    (subsidiary-function 3) => 88)
                  (function-under-test 3) => -88
                  (provided
                    (subsidiary-function 3) => 88))
      expected '(do
                  (expect (function-under-test 3) => -88
                          (fake (subsidiary-function 3) => 88))
                  (expect (function-under-test 3) => -88
                          (fake (subsidiary-function 3) => 88)))]
  (fact (transform original) => expected))


