
;;; Exercise 1

(def add
     (fn [this other]
       (Point (+ (:x this) (:x other))
              (+ (:y this) (:y other)))))
(prn (add (Point 1 2) (Point 3 4)))

(def add
     (fn [this other]
       (shift this (:x other) (:y other))))
(prn (add (Point 1 2) (Point 3 4)))

;;; Exercise 2

;; This version would not work with Triangle because
;; it requires exactly two arguments
(def a
     (fn [type arg1 arg2]  
       (type arg1 arg2)))
(prn (a Point 1 2))


;; This version will work with Triangle
(def a
     (fn [type & args]
       (apply type args)))
(prn (a Triangle (a Point 1 2)
                 (a Point 1 3)
                 (a Point 3 1)))


;;; Exercise 3

(def equal-triangles? =)
;; In Clojure, there's no distinction between "pointer equality" and
;; "content equality". (If you think about it, the substitution rule
;; for function evaluation demands that.)


;;; Exercise 4

;; The previous definition already works. Clojure's `=` can take one
;; or more arguments.


;;; Exercise 5

(def valid-triangle?
     (fn [& points]
       (= (distinct points) points)))
(prn (valid-triangle? (a Point 0 0) (a Point 0 0) (a Point 3 4)))
(prn (valid-triangle? (a Point 0 0) (a Point 0 3) (a Point 3 0)))

;; In the above case, we'd probably want to check
;; that we have the right number of points:

(def valid-triangle?
     (fn [& points]
       (and (= 3 (count points))
            (= (distinct points) points))))
(prn (valid-triangle? (a Point 0 0) (a Point 0 1)))
(prn (valid-triangle? (a Point 0 0) (a Point 0 0) (a Point 3 4)))
(prn (valid-triangle? (a Point 0 0) (a Point 0 3) (a Point 3 0)))

;; Alternately, we could take advantage of unintended generality and have our function
;; go beyond triangles.

(def valid-2d-figure?
     (fn [& points]
       (= (distinct points) points)))
(prn (valid-2d-figure? (a Point 0 0) (a Point 0 1)))
(prn (valid-2d-figure? (a Point 0 0) (a Point 0 0) (a Point 3 4)))
(prn (valid-2d-figure? (a Point 0 0) (a Point 0 3) (a Point 3 0)))

