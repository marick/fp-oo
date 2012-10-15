
;;; Exercise 1

(def add
     (fn [this other]
       (Point (+ (x this) (x other))
              (+ (y this) (y other)))))
(prn (add (Point 1 2) (Point 3 4)))

