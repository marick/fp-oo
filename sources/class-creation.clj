(declare class-from-instance send-to apply-message-to)
;; This imports a function from another namespace. (Think package or module.)
(use '[clojure.pprint :only [cl-format]])


;;; Functions that construct the different kinds of objects

(def basic-object
     (fn [class-symbol]
       {:__class_symbol__ class-symbol}))

(def basic-class
     (fn [my-name left-symbol up-symbol instance-methods]
       (assoc (basic-object left-symbol)
              :__own_symbol__ my-name
              :__superclass_symbol__ up-symbol
              :__instance_methods__ instance-methods)))

(def install-half-of-class-pair
     (fn [my-name _left left-symbol _up up-symbol instance-methods]
       (assert (= _left :left))
       (assert (= _up :up))
       (let [return-value (basic-class my-name left-symbol up-symbol instance-methods)]
         (intern *ns* my-name return-value)
         return-value)))

(def metasymbol
     (fn [some-symbol]
       (symbol (str "Meta" some-symbol))))

;;; Here are methods that take a class-symbol or instance containing one and follow it somewhere. 

(def class-symbol-above
     (fn [class-symbol]
       (assert (symbol? class-symbol))
       (:__superclass_symbol__ (eval class-symbol))))

(def class-instance-methods
     (fn [class-symbol]
       (assert (symbol? class-symbol))
       (:__instance_methods__ (eval class-symbol))))

(def class-from-instance
     (fn [instance]
       (assert (map? instance))
       (eval (:__class_symbol__ instance))))

(def superclass-from-instance
     (fn [instance]
       (assert (map? instance))
       (eval (:__superclass_symbol__ (class-from-instance instance)))))

;; Core dispatch function

(def lineage-1
     (fn [class-symbol so-far]
       (if (nil? class-symbol)
         so-far
         (lineage-1 (class-symbol-above class-symbol)
                    (cons class-symbol so-far)))))
(def lineage
     (fn [class-symbol]
       (lineage-1 class-symbol [])))

(def method-cache
     (fn [class]
       (let [class-symbol (:__own_symbol__ class)
             method-maps (map class-instance-methods
                              (lineage class-symbol))]
         (apply merge method-maps))))

(def apply-message-to
     (fn [class instance message args]
       (let [method (message (method-cache class))]
         (if method
           (apply method instance args)
           (send-to instance :method-missing message args)))))



;;; The public interface

(def send-to
     (fn [instance message & args]
       ;; This may sometimes help you:
       ;; (pprint ["in send-to" instance message args])
       (apply-message-to (class-from-instance instance)
                         instance message args)))

(def send-super
     (fn [instance message & args]
       (apply-message-to (superclass-from-instance instance)
                         instance message args)))

(install-half-of-class-pair 'MetaAnything,
                            :left 'Anything,
                            :up 'Anything,
                            { 
                             :new
                             (fn [class & args]
                               (let [seeded {:__class_symbol__ (:__own_symbol__ class)}]
                                 (apply-message-to class seeded :add-instance-values args)))
                            })

(install-half-of-class-pair 'Anything,
                            :left 'MetaAnything,
                            :up nil,
                            {
                             :add-instance-values identity
                             :method-missing
                             (fn [this message args]
                               (throw (Error. (cl-format nil "A ~A does not accept the message ~A."
                                                         (send-to this :class-name)
                                                         message))))
                             :to-string (fn [this] (str this))
                             :class-name :__class_symbol__    
                             :class (fn [this] (class-from-instance this))
                             })
                            
(install-half-of-class-pair 'MetaClassMaker,
                            :left 'Anything,
                            :up 'MetaAnything,
                            {
                             :new
                             (fn [this
                                  new-class-symbol superclass-symbol
                                  instance-methods class-methods]
                               ;; Metaclass
                               (install-half-of-class-pair
                                 (metasymbol new-class-symbol)
                                 :left 'Anything
                                 :up 'MetaAnything
                                 class-methods)
                               ;; Class
                               (install-half-of-class-pair
                                 new-class-symbol
                                 :left (metasymbol new-class-symbol)
                                 :up superclass-symbol
                                 instance-methods))
                               ;; Return value is the new class object (not name)
                            })

(install-half-of-class-pair 'ClassMaker,
                            :left 'MetaClassMaker,
                            :up 'Anything,
                            {
                            })
                            
