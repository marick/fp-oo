;;; Exercise 4


(def apply-message-to
     (fn [class instance message args]
       (let [method (or (method-from-message message class)
                        message)]
       (apply method instance args))))

(prn (send-to (make Holder "stuff") :held))
