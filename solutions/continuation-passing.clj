;;; Exercise 1

;; Original
(let [a (concat '(a b c) '(d e f))
      b (count a)]
  (odd? b))

;; Continuation-passing style
(-> (concat '(a b c) '(d e f))
    ((fn [a]
      (-> (count a)
          ((fn [b]
            (odd? b)))))))

;;; Exercise 2

;; Original 
(odd? (count (concat '(a b c) '(d e f))))

;; Since you can rewrite the nested expression into the `if` expression above, the
;; same continuation-passing style is appropriate.

;; However, the `concat` expression is fairly complicated. An
;; automated tool making the transformation would probably convert it,
;; too, into continuation-passing style, which would give this:


(-> '(a b c)
    ((fn [value1]
      (-> (concat value1 '(d e f))
          ((fn [value2]
             (-> (count value2)
                 ((fn [value3]
                    (odd? value3))))))))))

;; I could even imagine an automated tool converting the literals, like this:

(-> nil
    ((fn [tail]
       (-> (cons 'c tail)
           ((fn [tail]
              (-> (cons 'b tail)
                  ((fn [tail]
                     (cons 'a tail))))))))))

;; The correct thoroughness depends on the purpose of the
;; transformation. For our purposes, ones like the solution to
;; exercise 1 are fine.

;; By the way, the decomposition of '(a b c) is closer to the real
;; definition of currying, which (in its technical rather than
;; colloquial sense) is about representing a function with multiple
;; arguments as multiple composed functions with one argument. In some
;; functional languages, notably Haskell, all functions are
;; curried. That is, if you had a function `plus` that added two
;; arguments, it's *really* a function that takes one argument and
;; yields a function that takes one argument. Here's an example:
;;
;; This defines the function `plus`:
;;
;; Prelude> let plus a b = a + b 
;;
;; It can take two arguments:
;;
;; Prelude> plus 1 2 
;; 3
;;
;; You don't have to use something like `partial` to make a one-argument
;; incrementor. Instead, `plus` with one argument *is* a one argument
;; function:
;;
;; Prelude> let increment = plus 1
;;
;; Prelude> increment 300
;; 301


;;; Exercise 3

(-> 3
    ((fn [value1]
       (-> (+ value1 2)
           ((fn [value2]
              (inc value2)))))))
