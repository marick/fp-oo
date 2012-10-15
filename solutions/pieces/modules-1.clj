;;; Exercise 1

;; Module 

(install (method-holder 'Module
                        :left 'MetaModule
                        :up 'Anything
                        {
                         :include
                         (fn [this module]
                           (str "Module " (:__own_symbol__ module)
                                " will someday be included into " (:__own_symbol__ this)))
                         }))

(install
 (invisible
  (method-holder 'MetaModule
                 :left 'Klass
                 :up 'Klass
                 {
                  :new
                  (fn [this module-symbol]
                    {:__own_symbol__ module-symbol})
                  })))


;; Klass
(install (method-holder 'Klass,
                        :left 'MetaKlass,
                        :up 'Module,             ;; <<== new
                        {
                         :new
                         (fn [class & args]
                           (let [seeded {:__left_symbol__ (:__own_symbol__ class)}]
                             (apply-message-to class seeded :add-instance-values args)))

                         :to-string
                         (fn [class]
                           (str "class " (:__own_symbol__ class)))

                         :ancestors
                         (fn [class]
                           (remove invisible?
                                   (reverse (lineage (:__own_symbol__ class)))))
                         }))
                            
(install
 (invisible
  (method-holder 'MetaKlass,
                 :left 'Klass,
                 :up 'MetaModule,              ;; <<== new
                 {
                  :new
                  (fn [this
                       new-class-symbol superclass-symbol
                       instance-methods class-methods]
                    ;; Metaclass
                    (install
                     (invisible
                      (method-holder (metasymbol new-class-symbol)
                                     :left 'Klass
                                     :up 'MetaAnything
                                     class-methods)))
                    ;; Class
                    (install
                     (method-holder new-class-symbol
                                    :left (metasymbol new-class-symbol)
                                    :up superclass-symbol
                                    instance-methods)))
                  })))

