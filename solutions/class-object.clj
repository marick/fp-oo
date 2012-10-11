(load-file "sources/class-object.clj")

;;; Exercise 1

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


;; Does a method-missing override work?
(send-to (send-to MissingOverrider :new) :queen-bee "Dawn")


;;; Exercise 2

;; Before:
;; (send-to Point :to-string)    ; Stack overflow error, sometimes repl crash
;; (send-to Anything :unknown)   ; Stack overflow error, sometimes repl crash

;; Reasoning:
;; * Any object must be a subclass of Anything (either direct or indirect).
;; 
;; * MetaAnything is an object
;; 
;; * Therefore: it must be a subclass of Anything.
;; 
;; * It's a direct subclass because there's nothing to put between it and Anything.

(def MetaAnything
     (assoc MetaAnything :__superclass_symbol__ 'Anything))

;; After
(send-to Point :to-string)    ; a string

(try (send-to Anything :unknown)
(catch Error e))              ; a method-missing error


;;; Exercise 3

;; Before:
;; (send-to MetaAnything :to-string)     ; Stack overflow error, sometimes repl crash
;; (send-to MetaPoint :new)              ; Stack overflow error, sometimes repl crash

;; Reasoning:
;; * Any object in the system must have a class.
;;
;; * Any object's class must be descended from Anything so that it has
;;   access to methods like `:class` and `:method-missing`.
;;
;; * MetaAnything is an object.
;;
;; * Therefore, its class link must point to Anything or something
;;   that's descended from it.
;;
;; * There are two choices: Anything or MetaAnything. 
;;     * If MetaAnything were its own class, you'd be able to send
;;       MetaAnything :new. But there's no point to having the
;;       ability to make new MetaAnythings. It seems like trying to
;;       do that should be an error -- a :method-missing error.
;;
;;      * That leaves Anything.
;;
;; The same thinking implies that MetaPoint's class should be Anything.

(def MetaAnything
     (assoc MetaAnything :__class_symbol__ 'Anything))

(def MetaPoint
     (assoc MetaPoint :__class_symbol__ 'Anything))

;; After
(send-to MetaAnything :to-string)

(try (send-to MetaPoint :new)
(catch Error e))          ; a method-missing error
