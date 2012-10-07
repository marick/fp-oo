;; Exercise 5

;; Since `repeat-to-super` doesn't explicitly take the arguments, they have
;; to be made available dynamically.
(def ^:dynamic current-arguments)

;; `current-arguments` needs to be bound in both `apply-message-to`
;; and `send-super`.  I don't like the duplication, nor that the
;; binding needed for `send-super` is done in `send-super`, but the
;; binding needed for `send-to` is done in the helper function
;; `apply-message-to`. But this solution is easy to follow as just a small change from
;; what came before.

(def apply-message-to
     (fn [method-holder instance message args]
       (let [holder-symbol (find-containing-holder-symbol (:__own_symbol__ method-holder) message)]
         (if holder-symbol
           (binding [holder-of-current-method holder-symbol
                     this instance
                     current-message message
                     current-arguments args]                      ;; <<== change
             (apply (message (held-methods holder-symbol)) args))
           (send-to instance :method-missing message args)))))

(def send-super
     (fn [& args]
       (binding [holder-of-current-method (next-higher-holder-or-die)
                 current-arguments args]                         ;; <<== change
         (apply (current-message (held-methods holder-of-current-method)) args))))

;;; Then repeat-to-super is straightforward:

(def repeat-to-super
     (fn []
       (apply send-super current-arguments)))


