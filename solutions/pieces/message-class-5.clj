;;; Exercise 7

(def ^:dynamic message nil)


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
          :sender      (fn [] (:sender this))
          :previous    (fn [] (:previous this))    ;; <<==
          :super-count (fn [] (:super-count this)) ;; <<==

          :trace                                   ;; <<==
          (fn []
            ;; Note that I decided, just to make things more varied, that
            ;; the work of turning a linked list of message lists into a
            ;; sequence should be a class method of `Message`.
            (let [raw-results (send-to (send-to this :class) :message-trace-to-sequence this)
                  formatted-results (map (fn [result]
                                           (select-keys result
                                                        [:name :args :holder-name :target :super-count]))
                                         raw-results)]
              (reverse formatted-results)))

          :move-up 
          (fn []
            (let [holder-name (send-to this :find-containing-holder-symbol)]
              (if holder-name 
                (assoc this
                       :holder-name holder-name
                       :previous this
                       :super-count (inc (send-to this :super-count)))  ;; <<==
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
         {
          :message-trace-to-sequence                                   ;; <<==
          (fn [final-message]
            (if (nil? final-message)
              nil
              (cons final-message
                    (send-to this :message-trace-to-sequence
                             (send-to final-message :previous)))))
          })


(def tentative-message
     (fn [target name args]
       (let [initialize (get (held-methods 'Message) :add-instance-values)
             previous message                                                 ;; <<==
             sender this]
         (binding [this (basic-object 'Message)]
           (initialize :name name
                       :holder-name (find-containing-holder-symbol
                                      (:__left_symbol__ target)
                                      name)
                       :args args
                       :target target
                       :sender sender
                       :previous previous                                     ;; <<==
                       :super-count 0)))))                                    ;; <<==


