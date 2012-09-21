
;;; For Exercise 2

;;; This is a Function constructor that doesn't work. 

(def Function
     (fn [fn-value & property-pairs]
       (merge this
              {:__function__ fn-value}
              (apply hash-map property-pairs))))
