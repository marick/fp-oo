

;;; Exercise 1

(def send-super
     (fn [instance message & args]
       (apply-message-to (class-symbol-above (class-from-instance instance))
                         instance message args)))


;;; Exercise 2

;; Here is the original:

(def apply-message-to
     (fn [class instance message args]
       (let [class-symbol (:__own_symbol__ class)]
         (apply (message (method-cache class-symbol))
                instance args))))

;; If (message (method-cache class)) comes back nil, we *could* apply
;; a different method, looking up `method-missing` in the
;; method-cache. That could look like this:

(def apply-message-to
     (fn [class instance message args]
       (let [class-symbol (:__own_symbol__ class)
             method-map (method-cache class-symbol)
             method (message method-map)]
         (if method
           (apply method instance args)
           (apply (:method-missing method-map)
                  instance
                  message
                  (list args))))))

;; That looks kind of ugly to me, just due to sheer length. Also,
;; having to wrap the args argument in order to defeat the way `apply` handles its
;; last argument makes the code non-obvious.
;;
;; So let's not use `apply`. We have a function. Let's just call it:

(def apply-message-to
     (fn [class instance message args]
       (let [class-symbol (:__own_symbol__ class)
             method-map (method-cache class-symbol)
             method (message method-map)]
         (if method
           (apply method instance args)
           ( (:method-missing method-map) instance message args)))))

;; That's more straightforward, but the function still seems
;; long. Instead of thinking in terms of functions, lets think about
;; messages. If an attempt at one `send-to` fails, why not send
;; `:method-missing` using the same `send-to` function? That has a
;; certain elegance, in that we're not writing custom code, just using
;; a known feature. Also, it's shorter:

(def apply-message-to
     (fn [class instance message args]
       (let [class-symbol (:__own_symbol__ class)
             method (message (method-cache class-symbol))]
         (if method
           (apply method instance args)
           (send-to instance :method-missing message args)))))


;; Does a method-missing override work?
(send-to (a MissingOverrider) :queen-bee "Dawn")
;; Does method-missing get called when `send-super` is incorrectly used?
(send-to (a SuperSender) :overrides-nothing)
