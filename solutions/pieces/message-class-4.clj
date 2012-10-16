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

