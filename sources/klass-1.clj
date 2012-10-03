(declare class-from-instance send-to apply-message-to)
;; This imports a function from another namespace. (Think package or module.)
(use '[clojure.pprint :only [cl-format]])


;;; Functions that construct the different kinds of objects

(def basic-object
     (fn [class-symbol]
       {:__class_symbol__ class-symbol}))

(def basic-class
     (fn [my-name
          _left left-symbol
          _up up-symbol
          instance-methods]
       (assert (= _left :left))
       (assert (= _up :up))
       (assoc (basic-object left-symbol)
              :__own_symbol__ my-name
              :__superclass_symbol__ up-symbol
              :__instance_methods__ instance-methods)))

(def install 
     (fn [class]
         (intern *ns* (:__own_symbol__ class) class)
         class))

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
       (apply-message-to (class-from-instance instance)
                         instance message args)))



;;; The two class/pairs from which everything else can be built

;; Anything
(install (basic-class 'Anything,
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
                       }))
                            
(install (basic-class 'MetaAnything,
                      :left 'Anything,
                      :up 'Anything,
                      { 
                       :new
                       (fn [class & args]
                         (let [seeded {:__class_symbol__ (:__own_symbol__ class)}]
                           (apply-message-to class seeded :add-instance-values args)))
                       }))


;; Klass
(install (basic-class 'Klass,
                      :left 'MetaKlass,
                      :up 'Anything,
                      {
                      }))
                            
(install (basic-class 'MetaKlass,
                      :left 'Anything,
                      :up 'MetaAnything,
                      {
                       :new
                       (fn [this
                            new-class-symbol superclass-symbol
                            instance-methods class-methods]
                         ;; Metaclass
                         (install
                          (basic-class (metasymbol new-class-symbol)
                                       :left 'Anything
                                       :up 'MetaAnything
                                       class-methods))
                         ;; Class
                         (install
                          (basic-class new-class-symbol
                                       :left (metasymbol new-class-symbol)
                                       :up superclass-symbol
                                       instance-methods)))
                       }))

;; An example class:

(send-to Klass :new
         'Point 'Anything
         {
          :x :x
          :y :y 

          :add-instance-values
          (fn [this x y]
            (assoc this :x x :y y))
          
          :shift
          (fn [this xinc yinc]
            (let [my-class (send-to this :class)]
              (send-to my-class :new
                                (+ (:x this) xinc)
                                (+ (:y this) yinc))))
          :add
          (fn [this other]
            (send-to this :shift (:x other)
                                 (:y other)))
         } 
         
         {
          :origin (fn [class] (send-to class :new 0 0))
         })

