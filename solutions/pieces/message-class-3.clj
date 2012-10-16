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


