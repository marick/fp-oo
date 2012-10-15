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

;;; Exercise 2

(install
 (invisible
  (method-holder 'MetaModule
               :left 'Klass
               :up 'Klass
               {
                :new
                (fn [this name methods]
                  (install
                   (method-holder name
                                  ;; We move left to find `:install`.
                                  ;; That means the class `Module` must be in
                                  ;; the "up" chain of the leftward object.
                                  ;; Since we don't have a need for a Meta
                                  ;; version of this new module, "left" can
                                  ;; point directly at `Module`. 
                                  :left 'Module

                                  ;; If `:up` pointed to, say, `Anything`, then
                                  ;; the methods from `Anything` would get
                                  ;; inserted into the inheritance chain earlier than
                                  ;; they would otherwise be, preventing other classes
                                  ;; from overriding them.
                                  :up nil
                                  
                                  methods)))
                })))

;;; Exercise 3

(install
 (method-holder 'Module
                :left 'MetaModule
                :up 'Anything
                {
                 :include
                 (fn [this module]
                   (let [module-name (:__own_symbol__ module)
                         stub-name (gensym module-name)
                         stub {:__own_symbol__ stub-name
                               :__up_symbol__ (:__up_symbol__ this)
                               :__left_symbol__ module-name}]
                     ;; This now points up to the included stub:
                     (install (assoc this :__up_symbol__ stub-name))
                     ;; And the included stub points to the real module:
                     (install stub)))
               }))

;;; Exercise 4

;; This version just adds a flag so it's easy to tell that
;; the stub is a stub.
(install
 (method-holder 'Module
                :left 'MetaModule
                :up 'Anything
                {
                 :include
                 (fn [this module]
                   (let [module-name (:__own_symbol__ module)
                         stub-name (gensym module-name)
                         stub {:__own_symbol__ stub-name
                               :__up_symbol__ (:__up_symbol__ this)
                               :__left_symbol__ module-name
                               :__module_stub?__ true}]              ;;<<== New
                     (install (assoc this :__up_symbol__ stub-name))
                     (install stub)))
               }))


(def names-module-stub?
     (fn [symbol]
       (:__module_stub?__ (eval symbol))))

(def method-holder-symbol-to-left
     (fn [symbol]
       (assert (symbol? symbol))
       (:__left_symbol__ (eval symbol))))

(def lineage-1
     (fn [symbol so-far]
       (cond (nil? symbol)
             so-far

             (names-module-stub? symbol)
             (lineage-1 (method-holder-symbol-above symbol)
                        (concat (lineage (method-holder-symbol-to-left symbol))
                                so-far))

             :else 
             (lineage-1 (method-holder-symbol-above symbol)
                        (cons symbol so-far)))))

