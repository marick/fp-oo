;;; Exercise 3

(def message-above :ensure-that-the-function-can-no-longer-be-called)

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
            (let [holder-name (find-containing-holder-symbol
                               (method-holder-symbol-above (:holder-name this))
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
       (activate (send-to message :move-up))))
       
(def send-super
     (fn [& args]
       (def mss message)
       (activate (assoc (send-to message :move-up)
                        :args args))))


