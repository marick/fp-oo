;;; Exercise 2

;; This version would not work with Triangle because
;; it requires exactly two arguments
(def a
     (fn [type arg1 arg2]  
       (type arg1 arg2)))
(prn (a Point 1 2))


