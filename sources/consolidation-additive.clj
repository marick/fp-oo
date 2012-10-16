(load-file "sources/dynamic.clj")

(def ^:dynamic *active-message* nil)

;;; Constructing messages

(def find-containing-holder-symbol
     (fn [first-candidate message-name]
       (first (filter (fn [holder-symbol]
                        (message-name (held-methods holder-symbol)))
                      (reverse (lineage first-candidate))))))

(def fresh-active-message
     (fn [target name args]
       (let [holder-name (find-containing-holder-symbol (:__left_symbol__ target)
                                                        name)]
             (if holder-name
               {:name name, :holder-name holder-name, :args args, :target target}
               (fresh-active-message target
                                     :method-missing
                                     (vector name args))))))


(def using-method-above
     (fn [active-message]
       (let [symbol-above (method-holder-symbol-above (:holder-name active-message))
             holder-name (find-containing-holder-symbol symbol-above
                                                        (:name active-message))]
         (if holder-name
           (assoc active-message :holder-name holder-name)
           (throw (Error. (str "No superclass method `" (:name active-message)
                           "` above `" (:holder-name active-message)
                           "`.")))))))

;; Activating methods

(def method-to-run
     (fn [active-message]
       (get (held-methods (:holder-name active-message))
            (:name active-message))))

(def activate-method
     (fn [active-message]
       (binding [*active-message* active-message
                 this (:target active-message)]
         (apply (method-to-run active-message)
                (:args active-message)))))

;;; Public interface


(def send-to
     (fn [instance message-name & args]
       (activate-method (fresh-active-message instance message-name args))))

(def repeat-to-super
     (fn []
       (activate-method (using-method-above *active-message*))))
       
(def send-super
     (fn [& args]
       (let [with-replaced-args (assoc *active-message* :args args)]
         (activate-method (using-method-above with-replaced-args)))))

;; Klass
(install (method-holder 'Klass,
                        :left 'MetaKlass,
                        :up 'Module,
                        {
                         :new
                         (fn [& args]
                           (let [seeded {:__left_symbol__ (:__own_symbol__ this)}]
                             (apply send-to seeded :add-instance-values args)))

                         :to-string
                         (fn []
                           (str "class " (:__own_symbol__ this)))

                         :ancestors
                         (fn []
                           (remove invisible?
                                   (reverse (lineage (:__own_symbol__ this)))))
                         }))
                            
          
"clueby 0.2 (2012-10-02 patchlevel 0)"
