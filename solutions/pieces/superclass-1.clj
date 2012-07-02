

;;; Exercise 1

(def superclass-from-instance
     (fn [instance]
       (assert (map? instance))
       (eval (:__superclass_symbol__ (class-from-instance instance)))))

(def send-super
     (fn [instance message & args]
       (apply-message-to (superclass-from-instance instance)
                         instance message args)))
(prn (send-to (a Point 1 2) :to-string))


