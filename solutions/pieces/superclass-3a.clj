;;; Exercise 3

;; Here is the original:

(def apply-message-to
     (fn [class instance message args]
       (apply (message (method-cache class))
              instance args)))

;; If (message (method-cache class)) comes back nil, we *could* apply
;; a different method, looking up `method-missing` in the
;; method-cache. That could look like this:

(def apply-message-to
     (fn [class instance message args]
       (let [method-map (method-cache class)
             method (message method-map)]
         (if method
           (apply method instance args)
           (apply (:method-missing method-map)
                  instance
                  message
                  (list args))))))

