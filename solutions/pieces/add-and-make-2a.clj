;;; Exercise 2

;; This version would not work with Triangle because
;; it requires exactly two arguments
(def make
     (fn [type arg1 arg2]  
       (type arg1 arg2)))

(prn (make Point 1 2))


