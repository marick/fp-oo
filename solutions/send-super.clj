(load-file "sources/dynamic.clj")

;;; Exercise 1

;; Some notes:
;; * The results of `lineage` need to be reversed because `lineage`
;;   returns `Anything` first, then subclasses in descending order.
;;   That was convenient for `method-cache`. Now that we're abandoning
;;   `method-cache`, we should probably change `lineage` to return its
;;   results in the other order, but I'm not doing it in this solution
;;   so as to minimize the number of code changes.
;; * The use of `(first (filter ...))` is a little tricky, as it relies
;;   on the fact that `first` of an empty sequence is `nil`.

(def find-containing-holder-symbol
     (fn [first-candidate message]
       (first (filter (fn [holder-symbol]
                        (message (held-methods holder-symbol)))
                      (reverse (lineage first-candidate))))))

;;; Exercise 2 

(def ^:dynamic holder-of-current-method nil)

(def apply-message-to
     (fn [method-holder instance message args]
       (let [holder (find-containing-holder-symbol (:__own_symbol__ method-holder) message)]
         (if holder
           (binding [this instance
                     holder-of-current-method holder]
             (apply (message (held-methods holder)) args))
           (send-to instance :method-missing message args)))))


;;; Exercise 3


(def ^:dynamic current-message)

(def apply-message-to
     (fn [method-holder instance message args]
       (let [holder-symbol (find-containing-holder-symbol (:__own_symbol__ method-holder) message)]
         (if holder-symbol
           (binding [holder-of-current-method holder-symbol
                     this instance
                     current-message message]
             (apply (message (held-methods holder-symbol)) args))
           (send-to instance :method-missing message args)))))


;;; Exercise 4


(def next-higher-holder-or-die
     (fn []
       (let [first-candidate (method-holder-symbol-above holder-of-current-method)]
         (or (find-containing-holder-symbol first-candidate current-message)
             (throw (Error. (str "No superclass method `" current-message
                                 "` above `" holder-of-current-method
                                 "`.")))))))

(def send-super
     (fn [& args]
       (binding [holder-of-current-method (next-higher-holder-or-die)]
         (apply (current-message (held-methods holder-of-current-method)) args))))
       


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


