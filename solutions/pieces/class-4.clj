;;; Exercise 4


(def apply-message-to
     (fn [class instance message args]
       (assert (map? class))
       (assert (map? instance))
       (let [method (or (method-from-message message class)
                        message)]
       (apply method instance args))))

(prn (send-to (a Holder "stuff") :held))
