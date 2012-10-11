;; This version will work with Triangle
(def make
     (fn [type & args]
       (apply type args)))

(prn (make Triangle (make Point 1 2)
                    (make Point 1 3)
                    (make Point 3 1)))


