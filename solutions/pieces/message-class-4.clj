;;; Exercise 5

;; You're on your own for this one.

;;; Exercise 6

(send-to Klass :new
         'Message 'Anything
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
            (let [holder-name (send-to this :find-containing-holder-symbol)]
              (if holder-name 
                (assoc this :holder-name holder-name)
                (send-to this :no-message-to-move-up-to))))
          
          :find-containing-holder-symbol
          (fn []
            (find-containing-holder-symbol (send-to this :next-holder-up)
                                           (send-to this :name)))
          
          :next-holder-up
          (fn []
            (method-holder-symbol-above (send-to this :holder-name)))
          
          :no-message-to-move-up-to
          (fn []
            (throw (Error. (str "No superclass method `" (send-to this :name)
                                "` above `" (send-to this :holder-name)
                                "`."))))
         }
         {})


(def tentative-message
     (fn [target name args]
       (let [initialize (get (held-methods 'Message) :add-instance-values)
             ;; At this moment, `this` is still bound to the                  ;; <<==
             ;; sender of the message.                                        ;; <<==
             sender this]                                                     ;; <<==
         (binding [this (basic-object 'Message)]
           (initialize :name name
                       :holder-name (find-containing-holder-symbol
                                      (:__left_symbol__ target)
                                      name)
                       :args args
                       :target target
                       :sender sender)))))                                    ;; <<==


