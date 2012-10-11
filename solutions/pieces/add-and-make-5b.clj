;; In the above case, we'd probably want to check
;; that we have the right number of points:

(def valid-triangle?
     (fn [& points]
       (and (= 3 (count points))
            (= (distinct points) points))))

(prn (valid-triangle? (make Point 0 0) (make Point 0 1)))
(prn (valid-triangle? (make Point 0 0) (make Point 0 0) (make Point 3 4)))
(prn (valid-triangle? (make Point 0 0) (make Point 0 3) (make Point 3 0)))

