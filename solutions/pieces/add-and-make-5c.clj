;; Alternately, we could take advantage of unintended generality and have our function
;; go beyond triangles.

(def valid-2d-figure?
     (fn [& points]
       (= (distinct points) points)))

(prn (valid-2d-figure? (make Point 0 0) (make Point 0 1)))
(prn (valid-2d-figure? (make Point 0 0) (make Point 0 0) (make Point 3 4)))
(prn (valid-2d-figure? (make Point 0 0) (make Point 0 3) (make Point 3 0)))

