;;; Exercise 1

(def valid-triangle?
     (fn [& points]
       (= 3 (count (set points)))))

(prn (valid-triangle? (make Point 0 0) (make Point 0 0) (make Point 3 4)))
(prn (valid-triangle? (make Point 0 0) (make Point 0 3) (make Point 3 0)))

;; To be a bit more picky, we'd check that we weren't given 2 or 4
;; (etc.) points.

(def valid-triangle?
     (fn [& points]
       (and (= 3 (count points))
            (= 3 (count (set points))))))


(prn (valid-triangle? (make Point 0 0) (make Point 0 1))) ; false (too few)
(prn (valid-triangle? (make Point 0 0) (make Point 0 1) (make Point 1 2) (make Point 1 2))) ; false

(prn (valid-triangle? (make Point 0 0) (make Point 0 0) (make Point 3 4))) ; false (dups)
(prn (valid-triangle? (make Point 0 0) (make Point 0 3) (make Point 3 0))) ; true


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

;;; Exercise 3

(def equal-triangles? =)  ;; See the postscript.


(prn (equal-triangles? right-triangle right-triangle))       ; true
(prn (equal-triangles? right-triangle equal-right-triangle)) ; true
(prn (equal-triangles? right-triangle different-triangle))   ; false

;;; Exercise 4

;; The previous definition already works. Clojure's `=` can take one
;; or more arguments.

(prn (equal-triangles? right-triangle equal-right-triangle different-triangle))  ; false
