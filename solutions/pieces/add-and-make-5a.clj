;;; Exercise 5

(def valid-triangle?
     (fn [& points]
       (= (distinct points) points)))

(prn (valid-triangle? (make Point 0 0) (make Point 0 0) (make Point 3 4)))
(prn (valid-triangle? (make Point 0 0) (make Point 0 3) (make Point 3 0)))

