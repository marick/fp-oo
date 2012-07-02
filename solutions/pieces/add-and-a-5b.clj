;; In the above case, we'd probably want to check
;; that we have the right number of points:

(def valid-triangle?
     (fn [& points]
       (and (= 3 (count points))
            (= (distinct points) points))))
(prn (valid-triangle? (a Point 0 0) (a Point 0 1)))
(prn (valid-triangle? (a Point 0 0) (a Point 0 0) (a Point 3 4)))
(prn (valid-triangle? (a Point 0 0) (a Point 0 3) (a Point 3 0)))

