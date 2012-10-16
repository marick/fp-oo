;;; Exercise 2


(def ^:dynamic current-message)
(def ^:dynamic current-arguments)
(def ^:dynamic holder-of-current-method)

(def apply-message-to
     (fn [method-holder instance message args]
       (let [target-holder (find-containing-holder-symbol (:__own_symbol__ method-holder)
                                                          message)]
         (if target-holder
           (binding [this instance
                     current-message message
                     current-arguments args
                     holder-of-current-method target-holder]
             (apply (message (held-methods target-holder)) args))
           (send-to instance :method-missing message args)))))


;;; Exercise 3


(def throw-no-superclass-method-error
     (fn []
       (throw (Error. (str "No superclass method `" current-message
                           "` above `" holder-of-current-method
                           "`.")))))

(def next-higher-holder-or-die
     (fn []
       (let [first-candidate (method-holder-symbol-above holder-of-current-method)]
         (or (find-containing-holder-symbol first-candidate current-message)
             (throw-no-superclass-method-error)))))


;;; Exercise 4

(def send-super
     (fn [& args]
       (binding [holder-of-current-method (next-higher-holder-or-die)
                 current-arguments args]
         (apply (current-message (held-methods holder-of-current-method)) args))))


;;; Exercise 5

(def repeat-to-super
     (fn []
       (apply send-super current-arguments)))

