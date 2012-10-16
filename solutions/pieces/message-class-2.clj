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


