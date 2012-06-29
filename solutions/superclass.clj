

;;; Exercise 1

(def send-super
     (fn [instance message & args]
       (apply-message-to (class-symbol-above (class-from-instance instance))
                         instance message args)))


;;; Exercise 2

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

;; That looks kind of ugly to me, just due to sheer length. Also,
;; having to wrap the args argument in order to defeat the way `apply` handles its
;; last argument makes the code non-obvious.
;;
;; So let's not use `apply`. We have a function. Let's just call it:

(def apply-message-to
     (fn [class instance message args]
       (let [method-map (method-cache class)
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
       (let [method (message (method-cache class))]
         (if method
           (apply method instance args)
           (send-to instance :method-missing message args)))))

;;; Some tests:

;; Can a subclass override method-missing?
;; (This also checks that the arguments are passed correctly.)

(def MissingOverrider
{
  :__own_symbol__ 'MissingOverrider
  :__superclass_symbol__ 'Anything
  :__instance_methods__
  {
   :method-missing
   (fn [this message args]
     (prn "method-missing called! " message " on " this)
     (prn "The arguments were " args))
  }
 })

(send-to (a MissingOverrider) :queen-bee "Dawn")


;; Does method-missing get called when `send-super` is incorrectly used?

(def SuperSender
{
  :__own_symbol__ 'SuperSender
  :__superclass_symbol__ 'Anything
  :__instance_methods__
  {
   :overrides-nothing
   (fn [this]
     (send-super this :overrides-nothing))
  }
 })


(send-to (a SuperSender) :overrides-nothing)
