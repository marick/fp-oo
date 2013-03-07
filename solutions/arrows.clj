;;; Exercise 1

(-> [1] first inc list)

;;; Exercise 2

(-> [1] first inc (* 3) list)

;;; Exercise 3

;; An extra pair of parentheses is needed to "protect" the `fn` from `->`:

(-> 3
    ((fn [n] (* 2 n)))
    inc)

;; To see why, let's start with a simpler case where an argument to the arrow is a list:
;;
;;    (-> 3 (- 2))
;;          -----
;;
;; I've underlined the list. By the second rule for arrows, that is
;; the same as (or "expands to") this:
;;
;;          (- 3 2)
;;
;; The mechanics are that the 3 is put after the first element of the list (`-`). 
;;
;; Now consider this
;;
;;    (-> 3 (fn [n] (inc n)))
;;          ----------------
;;
;; Again, I've underlined the list. Note that it's a list whose first
;; element is `fn`. So the 3 is put after that:
;;
;;          (fn 3 [n] (inc n))
;;
;; Not so good. We want the 3 to be put after the entire `fn` expression. That means it must be
;; the first element of a list. So we need another set of parentheses:
;;
;;    (-> 3 (  (fn [n] (inc n))  ))
;;          ^                    ^
;;
;; Once again, the 3 is placed after the first element of that (single-element) list:
;;
;; 
;;          (  (fn [n] (inc n)) 3)



;;; Exercise 5

(-> (+ 1 2) (* 3) (+ 4))

