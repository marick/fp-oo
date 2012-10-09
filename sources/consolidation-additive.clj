(load-file "sources/dynamic.clj")

(def ^:dynamic message {:name :undefined,
                        :holder-name :undefined,
                        :args :undefined,
                        :target :undefined})

;;; Constructing messages

(def find-containing-holder-symbol
     (fn [first-candidate message-name]
       (first (filter (fn [holder-symbol]
                        (message-name (held-methods holder-symbol)))
                      (reverse (lineage first-candidate))))))

(def fresh-message
     (fn [target name args]
       (let [tentative (assoc message
                             :name name 
                             :holder-name (find-containing-holder-symbol (:__left_symbol__ target) name)
                             :args args
                             :target target)]
         (if (:holder-name tentative)
           tentative
           (fresh-message target
                          :method-missing
                          (vector name args))))))
       
(def message-above
     (fn [message]
       (let [holder-name (find-containing-holder-symbol
                          (method-holder-symbol-above (:holder-name message))
                          (:name message))]
         (if holder-name
           (assoc message :holder-name holder-name)
           (throw (Error. (str "No superclass method `" (:name message)
                           "` above `" (:holder-name message)
                           "`.")))))))

;; Activating messages

(def method-to-run
     (fn [message]
       (get (held-methods (:holder-name message)) (:name message))))

(def activate
     (fn [message]
       (binding [message message
                 this (:target message)]
         (apply (method-to-run message) (:args message)))))

;;; Public interface


(def send-to
     (fn [instance message-name & args]
       (activate (fresh-message instance message-name args))))

(def repeat-to-super
     (fn []
       (activate (message-above message))))
       
(def send-super
     (fn [& args]
       (def mss message)
       (activate (message-above (assoc message :args args)))))

;; Klass
(install (method-holder 'Klass,
                        :left 'MetaKlass,
                        :up 'Module,
                        {
                         :new
                         (fn [& args]
                           (let [seeded {:__left_symbol__ (:__own_symbol__ this)}]
                             (apply send-to seeded :add-instance-values args)))    ;; <<== change

                         :to-string
                         (fn []
                           (str "class " (:__own_symbol__ this)))

                         :ancestors
                         (fn []
                           (remove invisible?
                                   (reverse (lineage (:__own_symbol__ this)))))
                         }))
                            
          
"clueby 0.2 (2012-10-02 patchlevel 0)"
