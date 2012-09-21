;;; For Exercise 1
;;; This function will be used to bootstrap the replacement
;;; of Clojure functions with Javascript Functions:

(def make-function 
     (fn [fn-value & property-pairs]
       (merge {:__function__ fn-value}
              (apply hash-map property-pairs))))


