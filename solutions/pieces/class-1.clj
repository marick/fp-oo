;;; Exercise 1

;; I've found that it's easy for me to mess up and pass classes (maps)
;; to functions that want class-symbols. This can produce
;; hard-to-debug failures. Even worse, it sometimes *doesn't* fail
;; (mostly because maps are self-evaluating, so `(eval 'Point)` and
;; `(eval Point)` produce the same result). But later changes can
;; surface the bug in an even *more* confusing way. So I use Clojure's
;; `assert` to check the type of select arguments.

(def method-from-message
     (fn [message class]
       (assert (map? class))
       (message (:__instance_methods__ class))))

(def class-from-instance
     (fn [instance]
       (assert (map? instance))
       (eval (:__class_symbol__ instance))))

(def apply-message-to
     (fn [class instance message args]
       (assert (map? class))
       (assert (map? instance))
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

