
;;; Exercise 1

(def add
     (fn [this other]
       (Point (+ (:x this) (:x other))
              (+ (:y this) (:y other)))))

(def add
     (fn [this other]
       (shifted this (:x other) (:y other))))

;;; Exercise 2

;; This version would not work with Triangle because
;; it requires exactly two arguments
(def a
     (fn [type arg1 arg2]  
       (type arg1 arg2)))

;; This version will work with Triangle
(def a
     (fn [type & args]
       (apply type args)))


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

;; In the above case, we'd probably want to check
;; that we have the right number of points:

(def valid-triangle?
     (fn [& points]
       (and (= 3 (count points))
            (= (distinct points) points))))

;; Alternately, we could take advantage of unintended generality:

(def valid-2d-figure?
     (fn [& points]
       (= (distinct points) points)))

