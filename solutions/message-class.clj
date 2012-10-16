(load-file "sources/consolidation.clj")


;;; Exercise 1

(send-to Klass :new
         'ActiveMessage 'Anything
         {
          :add-instance-values
          (fn [& key-value-pairs]
            (merge this (apply hash-map key-value-pairs)))

          :name        (fn [] (:name this))
          :holder-name (fn [] (:holder-name this))
          :args        (fn [] (:args this))
          :target      (fn [] (:target this))
          }
          {
          })


;;; Exercise 2

(def send-to-Message-new  ;; Supposed to remind you of (send-to Message :new ...)
     (fn [target name args holder-name]
       (let [initializer (get (held-methods 'ActiveMessage) :add-instance-values)]
         (binding [this (basic-object 'ActiveMessage)]
           (initializer :name name
                        :holder-name holder-name
                        :args args
                        :target target)))))

(def fresh-active-message
     (fn [target name args]
       "Construct the message corresponding to the
      attempt to send the particular `name` to the
      `target` with the given `args`. If there is no
      matching method, the message becomes one that
      sends `:method-missing` to the target."
       (let [holder-name (find-containing-holder-symbol (:__left_symbol__ target)
                                                        name)]
             (if holder-name
               (send-to-Message-new target name args holder-name)
               (fresh-active-message target
                                     :method-missing
                                     (vector name args))))))
;;; Exercise 3

(def using-message-above :ensure-that-the-function-can-no-longer-be-called)

(send-to Klass :new
         'ActiveMessage 'Anything
         {
          :add-instance-values
          (fn [& key-value-pairs]
            (merge this (apply hash-map key-value-pairs)))

          :name        (fn [] (:name this))
          :holder-name (fn [] (:holder-name this))
          :args        (fn [] (:args this))
          :target      (fn [] (:target this))

          :move-up                                      ;; <<======
          (fn []
            (let [symbol-above (method-holder-symbol-above (:holder-name this))
                  holder-name (find-containing-holder-symbol symbol-above
                                                             (:name this))]
              (if holder-name
                (assoc this :holder-name holder-name)
                (throw (Error. (str "No superclass method `" (:name this)
                                    "` above `" (:holder-name this)
                                    "`."))))))
         }
         {})
                            
(def repeat-to-super
     (fn []
       (activate-method (send-to *active-message* :move-up))))
       
(def send-super
     (fn [& args]
       (activate-method (assoc (send-to *active-message* :move-up)
                               :args args))))


;;; Exercise 4

(send-to Klass :new
         'ActiveMessage 'Anything
         {
          :add-instance-values
          (fn [& key-value-pairs]
            (merge this (apply hash-map key-value-pairs)))
          
          :name        (fn [] (:name this))
          :holder-name (fn [] (:holder-name this))
          :args        (fn [] (:args this))
          :target      (fn [] (:target this))
          
          :move-up 
          (fn []
            (let [holder-name (send-to this :holder-name-above)]
              (if holder-name
                (assoc this :holder-name holder-name)
                (send-to this :spew-fail-to-move-up-error))))

         ;; Private
         :holder-name-above
         (fn [] 
           (let [symbol-above (method-holder-symbol-above (send-to this :holder-name))]
             (find-containing-holder-symbol symbol-above (send-to this :name))))

         :spew-fail-to-move-up-error
         (fn []
           (throw (Error. (str "No superclass method `" (send-to this :name)
                               "` above `" (send-to this :holder-name)
                               "`."))))
         }
         {})


;;; Exercise 5

;; You're on your own for this one.

;;; Exercise 6

(send-to Klass :new
         'ActiveMessage 'Anything
         {
          :add-instance-values
          (fn [& key-value-pairs]
            (merge this (apply hash-map key-value-pairs)))
          
          :name        (fn [] (:name this))
          :holder-name (fn [] (:holder-name this))
          :args        (fn [] (:args this))
          :target      (fn [] (:target this))
          :sender      (fn [] (:sender this))                                    ;; <<==
          
          :move-up 
          (fn []
            (let [holder-name (send-to this :holder-name-above)]
              (if holder-name
                (assoc this :holder-name holder-name)
                (send-to this :spew-fail-to-move-up-error))))

         ;; Private
         :holder-name-above
         (fn [] 
           (let [symbol-above (method-holder-symbol-above (send-to this :holder-name))]
             (find-containing-holder-symbol symbol-above (send-to this :name))))

         :spew-fail-to-move-up-error
         (fn []
           (throw (Error. (str "No superclass method `" (send-to this :name)
                               "` above `" (send-to this :holder-name)
                               "`."))))
         }
         {})


(def send-to-Message-new  ;; Supposed to remind you of (send-to Message :new ...)
     (fn [target name args holder-name]
       (let [initializer (get (held-methods 'ActiveMessage) :add-instance-values)
             ;; At this moment, `this` is still bound to the                  ;; <<==
             ;; sender of the message.                                        ;; <<==
             sender this]                                                     ;; <<==
         (binding [this (basic-object 'ActiveMessage)]
           (initializer :name name
                        :holder-name holder-name
                        :args args
                        :target target
                        :sender sender)))))                                   ;; <<==

;;; Exercise 7

(def ^:dynamic message nil)


(send-to Klass :new
         'ActiveMessage 'Anything
         {
          :add-instance-values
          (fn [& key-value-pairs]
            (merge this (apply hash-map key-value-pairs)))
          
          :name        (fn [] (:name this))
          :holder-name (fn [] (:holder-name this))
          :args        (fn [] (:args this))
          :target      (fn [] (:target this))
          :sender      (fn [] (:sender this))
          :previous    (fn [] (:previous this))                  ;; <<==
          :super-count (fn [] (:super-count this))               ;; <<==
          
          :trace                                                 ;; <<==
          (fn []
            ;; Note that I decided, just to make things more varied, that
            ;; the work of turning a linked list of message lists into a
            ;; sequence should be a class method of `ActiveMessage`.
            (let [raw-results (send-to (send-to this :class) :message-trace-to-sequence this)
                  formatted-results (map (fn [result]
                                           (select-keys result
                                                        [:name :args :holder-name :target :super-count]))
                                         raw-results)]
              (reverse formatted-results)))

          :move-up 
          (fn []
            (let [holder-name (send-to this :holder-name-above)]
              (if holder-name
                (assoc this
                       :holder-name holder-name
                       :previous    this
                       :super-count (inc (send-to this :super-count)))  ;; <<==
                (send-to this :spew-fail-to-move-up-error))))

         ;; Private
         :holder-name-above
         (fn [] 
           (let [symbol-above (method-holder-symbol-above (send-to this :holder-name))]
             (find-containing-holder-symbol symbol-above (send-to this :name))))

         :spew-fail-to-move-up-error
         (fn []
           (throw (Error. (str "No superclass method `" (send-to this :name)
                               "` above `" (send-to this :holder-name)
                               "`."))))
         }

         ;; Class methods
         {
          :message-trace-to-sequence                                   ;; <<==
          (fn [final-message]
            (if (nil? final-message)
              nil
              (cons final-message
                    (send-to this :message-trace-to-sequence
                             (send-to final-message :previous)))))
          })


(def send-to-Message-new  ;; Supposed to remind you of (send-to Message :new ...)
     (fn [target name args holder-name]
       (let [initializer (get (held-methods 'ActiveMessage) :add-instance-values)
             previous *active-message*                                 ;; <<==
             sender this]                                                 
         (binding [this (basic-object 'ActiveMessage)]
           (initializer :name name
                        :holder-name holder-name
                        :args args
                        :target target
                        :sender sender
                        :previous previous                             ;; <<==
                        :super-count 0)))))                            ;; <<==

