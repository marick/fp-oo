;;; Exercise 2

(def make
     (fn [type & args]
       (apply type args)))

(prn (make Point 1 2))

