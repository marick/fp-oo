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

