;; That's more straightforward, but the function still seems
;; long. Instead of thinking in terms of functions, lets think about
;; messages. If an attempt at one `send-to` fails, why not send
;; `:method-missing` using the same `send-to` function? That has a
;; certain elegance, in that we're not writing custom code, just using
;; a known feature. Also, it's shorter:

(def apply-message-to
     (fn [class instance message args]
       (let [method (message (method-cache class))]
         (if method
           (apply method instance args)
           (send-to instance :method-missing message args)))))


;; Does a method-missing override work?
(send-to (make MissingOverrider) :queen-bee "Dawn")
;; Does method-missing get called when `send-super` is incorrectly used?
(send-to (make SuperSender) :overrides-nothing)
