;;; Exercise 1

(def method-from-message
     (fn [message class]
       (message (:__instance_methods__ class))))

(def class-from-instance
     (fn [instance]
       (eval (:__class_symbol__ instance))))

(def apply-message-to
     (fn [class instance message args]
       (apply (method-from-message message class)
              instance args)))

(def make
     (fn [class & args]
       (let [seeded {:__class_symbol__ (:__own_symbol__ class)}]
         (apply-message-to class seeded :add-instance-values args))))

(def send-to
     (fn [instance message & args]
       (apply-message-to (class-from-instance instance)
                         instance message args)))

;; For example:
(prn (send-to (make Point 1 2) :class))
(prn (send-to (make Point 1 2) :shift -1 -2))

