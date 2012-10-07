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
       


