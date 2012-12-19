;; Exercise 2

(def valid-triangle?
     (fn [& points]
       (and (= (count points) 3)
            (= (count (set points)) 3)
            (> (count (set (map :x points))) 1)     ; not a vertical line
            (> (count (set (map :y points))) 1))))  ; not a horizontal line

(prn (valid-triangle? (make Point 0 0) (make Point 0 1)))    ; false (too few)
(prn (valid-triangle? (make Point 0 0) (make Point 0 1) (make Point 1 2) (make Point 1 2))) 
(prn (valid-triangle? (make Point 0 0) (make Point 0 0) (make Point 3 4))) ; false (dups)
(prn (valid-triangle? (make Point 0 1) (make Point 0 2) (make Point 0 3))) ; false (vertical line)
(prn (valid-triangle? (make Point 1 0) (make Point 2 0) (make Point 3 0))) ; false (horizonal line)
(prn (valid-triangle? (make Point 0 0) (make Point 0 3) (make Point 3 0))) ; true

