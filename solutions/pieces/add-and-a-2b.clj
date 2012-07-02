;; This version will work with Triangle
(def a
     (fn [type & args]
       (apply type args)))
(prn (a Triangle (a Point 1 2)
                 (a Point 1 3)
                 (a Point 3 1)))


