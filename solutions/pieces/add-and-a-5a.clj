;;; Exercise 5

(def valid-triangle?
     (fn [& points]
       (= (distinct points) points)))
(prn (valid-triangle? (a Point 0 0) (a Point 0 0) (a Point 3 4)))
(prn (valid-triangle? (a Point 0 0) (a Point 0 3) (a Point 3 0)))

