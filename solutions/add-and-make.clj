
;;; Exercise 1

(def add
     (fn [this other]
       (Point (+ (x this) (x other))
              (+ (y this) (y other)))))
(prn (add (Point 1 2) (Point 3 4)))

(def add
     (fn [this other]
       (shift this (x other) (y other))))

(prn (add (Point 1 2) (Point 3 4)))

;;; Exercise 2

(def make
     (fn [type & args]
       (apply type args)))

(prn (make Point 1 2))

