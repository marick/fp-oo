;;; Exercise 1

(def method-from-message
     (fn [message class]
       (message (:__instance_methods__ class))))

;; It's unfortunate that (eval 'Point) and (eval Point) evaluate to
;; the same map, because that allowed me to get away with code that
;; passed the latter instead of the former. That led to a
;; hard-to-debug problem in one of the next chapter's solutions. To
;; avoid that going forward, I'm putting an explicit type check on all
;; the functions that `eval`.

(def class-from-instance
     (fn [instance]
       (assert (map? instance))
       (eval (:__class_symbol__ instance))))

(def apply-message-to
     (fn [class instance message args]
       (apply (method-from-message message class)
              instance args)))

(def a
     (fn [class & args]
       (let [seeded {:__class_symbol__ (:__own_symbol__ class)}]
         (apply-message-to class seeded :add-instance-values args))))

(def send-to
     (fn [instance message & args]
       (apply-message-to (class-from-instance instance)
                         instance message args)))

;; For example:
(prn (send-to (a Point 1 2) :class))
(prn (send-to (a Point 1 2) :shift -1 -2))

