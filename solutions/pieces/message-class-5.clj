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

