(declare send-to apply-message-to)
;; This imports a function from another namespace. (Think package or module.)
(use '[clojure.pprint :only [cl-format]])


;;; Functions that construct the different kinds of objects

(def basic-object
     (fn [method-holder-symbol]
       {:__left_symbol__ method-holder-symbol}))

(def method-holder
     (fn [my-name
          _left left-symbol
          _up up-symbol
          methods]
       (assert (= _left :left))
       (assert (= _up :up))
       (assoc (basic-object left-symbol)
              :__own_symbol__ my-name
              :__up_symbol__ up-symbol
              :__methods__ methods)))

(def install 
     (fn [method-holder]
         (intern *ns* (:__own_symbol__ method-holder) method-holder)
         method-holder))

(def metasymbol
     (fn [some-symbol]
       (symbol (str "Meta" some-symbol))))

(def invisible
     (fn [method-holder]
       (assoc method-holder :__invisible__ true)))

(def invisible?
     (fn [method-holder-symbol] (:__invisible__ (eval method-holder-symbol))))


;;; Here are methods that take a method-holder-symbol or instance containing one and follow it somewhere. 

(def method-holder-symbol-above
     (fn [method-holder-symbol]
       (assert (symbol? method-holder-symbol))
       (:__up_symbol__ (eval method-holder-symbol))))

(def held-methods
     (fn [method-holder-symbol]
       (assert (symbol? method-holder-symbol))
       (:__methods__ (eval method-holder-symbol))))

(def left-from-instance
     (fn [instance]
       (assert (map? instance))
       (eval (:__left_symbol__ instance))))

(def left-up-from-instance
     (fn [instance]
       (assert (map? instance))
       (eval (:__up_symbol__ (left-from-instance instance)))))


;; Core dispatch function

(def lineage-1
     (fn [method-holder-symbol so-far]
       (if (nil? method-holder-symbol)
         so-far
         (lineage-1 (method-holder-symbol-above method-holder-symbol)
                    (cons method-holder-symbol so-far)))))
(def lineage
     (fn [method-holder-symbol]
       (lineage-1 method-holder-symbol [])))

(def method-cache
     (fn [method-holder]
       (let [method-holder-symbol (:__own_symbol__ method-holder)
             method-maps (map held-methods
                              (lineage method-holder-symbol))]
         (apply merge method-maps))))

(def apply-message-to
     (fn [method-holder instance message args]
       (let [method (message (method-cache method-holder))]
         (if method
           (apply method instance args)
           (send-to instance :method-missing message args)))))



;;; The public interface

(def send-to
     (fn [instance message & args]
       (apply-message-to (left-from-instance instance)
                         instance message args)))

;;; The two class/pairs from which everything else can be built

;; Anything
(install (method-holder 'Anything,
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

                       :class
                       (fn [this]
                         (eval (send-to this :class-name)))

                       :class-name 
                       (fn [this]
                         (first (send-to (left-from-instance this) :ancestors)))
                       }))
                            
(install
 (invisible
  (method-holder 'MetaAnything,
               :left 'Klass,
               :up 'Klass,
               { 
               })))


;; Klass
(install (method-holder 'Klass,
                        :left 'MetaKlass,
                        :up 'Anything,
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
                 :up 'Klass,
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

;; An example class:

(def <=>  ;; Spaceship operator for numbers.
     (fn [a-number another-number]
       (max -1 (min 1 (compare a-number another-number)))))

(send-to Klass :new
         'Trilobite 'Anything
         {
          :add-instance-values
          (fn [this facets]
            (assoc this :facets facets))

          :facets :facets

          :<=>  ;; Spaceship operator for trilobites
          (fn [this that]
            (<=> (send-to this :facets)
                 (send-to that :facets)))
         } 
         
         {
         })


;; An example module (wrapped in a comment until it works)

(comment

(send-to Module :new 'Komparable
         {:=  (fn [this that] (zero? (send-to this :<=> that)))
          :>  (fn [this that] (= 1 (send-to this :<=> that)))
          :>= (fn [this that] (or (send-to this := that)
                                  (send-to this :> that)))

          :<  (fn [this that] (send-to that :> this))
          :<= (fn [this that] (send-to that :>= this))

          :between?
          (fn [this lower upper]
            (and (send-to this :>= lower)
                 (send-to this :<= upper)))})

)
