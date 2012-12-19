(def valid-triangle?
     (fn [& points]
       (and (= 3 (count points))
            (= 3 (count (set points))))))


(prn (valid-triangle? (make Point 0 0) (make Point 0 1))) ; false (too few)
(prn (valid-triangle? (make Point 0 0) (make Point 0 1) (make Point 1 2) (make Point 1 2))) ; false

(prn (valid-triangle? (make Point 0 0) (make Point 0 0) (make Point 3 4))) ; false (dups)
(prn (valid-triangle? (make Point 0 0) (make Point 0 3) (make Point 3 0))) ; true


