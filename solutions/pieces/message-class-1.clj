(load-file "sources/consolidation.clj")


;;; Exercise 1

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
          }
          {
          })


;;; Exercise 2

(def tentative-message
     (fn [target name args]
       (let [initialize (get (held-methods 'Message) :add-instance-values)]
         (binding [this (basic-object 'Message)]
           (initialize :name name
                       :holder-name (find-containing-holder-symbol
                                      (:__left_symbol__ target)
                                      name)
                       :args args
                       :target target)))))

(def message-or-method-missing-message
     (fn [tentative]
         (if (:holder-name tentative)
           tentative
           (fresh-message (:target tentative)
                          :method-missing
                          (vector (:name tentative)
                                  (:args tentative))))))

(def fresh-message
     "Construct the message corresponding to the
      attempt to send the particular `name` to the
      `target` with the given `args`. If there is no
      matching method, the message becomes one that
      sends `:method-missing` to the target."
     (fn [target name args]
       (message-or-method-missing-message (tentative-message target name args))))


