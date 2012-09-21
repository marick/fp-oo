(load-file "sources/javascript.clj")

;;; Exercise 1
(def js-apply
     (fn [javascript-function object arguments]
       (binding [this object]
         (apply (:__function__ javascript-function) arguments))))


;;; Exercise 2
(def Function
     (make-function (fn [fn-value & property-pairs]
                      (merge this
                             {:__function__ fn-value}
                             (apply hash-map property-pairs)))))
    
