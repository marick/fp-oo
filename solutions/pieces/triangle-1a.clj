;;; Exercise 1

(def valid-triangle?
     (fn [& points]
       (= 3 (count (set points)))))

(prn (valid-triangle? (make Point 0 0) (make Point 0 0) (make Point 3 4)))
(prn (valid-triangle? (make Point 0 0) (make Point 0 3) (make Point 3 0)))

;; To be a bit more picky, we'd check that we weren't given 2 or 4
;; (etc.) points.

