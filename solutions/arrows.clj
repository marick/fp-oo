;;; Exercise 1

(-> [1] first inc list)

;;; Exercise 2

(-> [1] first inc (* 3) list)

;;; Exercise 3

;; It doesn't work because (-> [1 2 3 4] (partial map inc)) is the
;; equivalent to this form:
;; 
;;  (partial [1 2 3 4] map inc)
;;
;; Interestingly, this doesn't blow up because (1) `partial` doesn't check that
;; its first argument is a callable and (2) even if it did, vectors are callables.
;; 
;; To solve the problem, you need an extra set of parentheses to produce a
;; space for the vector to go:

 (-> [1 2 3 4]
     ( (partial map inc)          ))

;;; Exercise 4

;; Again, an extra pair of parentheses is needed to "protect" the `fn` from `->`:

(-> [1 2 3 4]
    ( (fn [sequence] (map inc sequence))             ))


;;; Exercise 5

(-> (+ 1 2) (* 3) (+ 4))

