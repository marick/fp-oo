;;; Exercise 4

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
          
          :move-up    ;; <<======
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


