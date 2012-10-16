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
