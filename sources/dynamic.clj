(declare send-to apply-message-to)
;; This imports a function from another namespace. (Think package or module.)
(use '[clojure.pprint :only [cl-format]])


;;; Implicit variables

(def ^:dynamic this nil)


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

(def names-module-stub?
     (fn [symbol]
       (:__module_stub?__ (eval symbol))))


;;; Here are methods that take a method-holder-symbol or instance containing one and follow it somewhere. 

(def method-holder-symbol-above
     (fn [method-holder-symbol]
       (assert (symbol? method-holder-symbol))
       (:__up_symbol__ (eval method-holder-symbol))))

(def method-holder-symbol-to-left
     (fn [symbol]
       (assert (symbol? symbol))
       (:__left_symbol__ (eval symbol))))

(def held-methods
     (fn [method-holder-symbol]
       (assert (symbol? method-holder-symbol))
       (:__methods__ (eval method-holder-symbol))))

(def left-from-instance
     (fn [instance]
       (assert (map? instance))
       (eval (:__left_symbol__ instance))))


;; Core dispatch function

(declare lineage)

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
           (binding [this instance] (apply method args))
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
                       :add-instance-values
                       (fn [] this)

                       :method-missing
                       (fn [message args]
                         (throw (Error. (cl-format nil "A ~A does not accept the message ~A."
                                                   (send-to this :class-name)
                                                   message))))
                       :to-string (fn [] (str this))

                       :class
                       (fn []
                         (eval (send-to this :class-name)))

                       :class-name 
                       (fn []
                         (first (send-to (left-from-instance this) :ancestors)))
                       }))
                            
(install
 (invisible
  (method-holder 'MetaAnything,
               :left 'Klass,
               :up 'Klass,
               { 
               })))



;; Module 

(install
 (method-holder 'Module
                :left 'MetaModule
                :up 'Anything
                {
                 :include
                 (fn [module]
                   (let [module-name (:__own_symbol__ module)
                         stub-name (gensym module-name)
                         stub {:__own_symbol__ stub-name
                               :__up_symbol__ (:__up_symbol__ this)
                               :__left_symbol__ module-name
                               :__module_stub?__ true}]
                     ;; This now points up to the included stub.
                     (install (assoc this :__up_symbol__ stub-name))
                     ;; And the included stub points to the real module.
                     (install stub)))
               }))


(install
 (invisible
  (method-holder 'MetaModule
               :left 'Klass
               :up 'Klass
               {
                :new
                (fn [name methods]
                  (install
                   (method-holder name
                                  ;; We move left to find `:install`.
                                  ;; That means the class `Module` must be in
                                  ;; the "up" chain of the leftward object.
                                  ;; Since we don't have a need for a Meta
                                  ;; version of this new module, we can point
                                  ;; directly to it. Otherwise, we'd have the
                                  ;; left object point up to `Module`.
                                  :left 'Module

                                  ;; If `:up` pointed to, say, `Anything`, then
                                  ;; the methods from that method holder would get
                                  ;; inserted into the inheritance chain earlier than
                                  ;; they would otherwise be, preventing other classes
                                  ;; from overriding them.
                                  :up nil
                                  
                                  methods)))
                })))


;; Klass
(install (method-holder 'Klass,
                        :left 'MetaKlass,
                        :up 'Module,
                        {
                         :new
                         (fn [& args]
                           (let [seeded {:__left_symbol__ (:__own_symbol__ this)}]
                             (apply-message-to this seeded :add-instance-values args)))

                         :to-string
                         (fn []
                           (str "class " (:__own_symbol__ this)))

                         :ancestors
                         (fn []
                           (remove invisible?
                                   (reverse (lineage (:__own_symbol__ this)))))
                         }))
                            
(install
 (invisible
  (method-holder 'MetaKlass,
                 :left 'Klass,
                 :up 'MetaModule,
                 {
                  :new
                  (fn [new-class-symbol superclass-symbol
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





;; Trilobites

(def <=>
     (fn [a-number another-number]
       (max -1 (min 1 (compare a-number another-number)))))


(send-to Module :new 'Komparable
         {:=  (fn [that] (zero? (send-to this :<=> that)))
          :>  (fn [that] (= 1 (send-to this :<=> that)))
          :>= (fn [that] (or (send-to this := that)
                                  (send-to this :> that)))

          :<  (fn [that] (send-to that :> this))
          :<= (fn [that] (send-to that :>= this))

          :between?
          (fn [lower upper]
            (and (send-to this :>= lower)
                 (send-to this :<= upper)))})


(send-to Klass :new
         'Trilobite 'Anything
         {
          :add-instance-values
          (fn [facets]
            (assoc this :facets facets))

          :facets (fn [] (:facets this))

          :<=>
          (fn [that]
            (<=> (send-to this :facets)
                 (send-to that :facets)))
         } 
         
         {
         })
(send-to Trilobite :include Komparable)


;;; Points

(send-to Klass :new
         'Point 'Anything
         {
          :x (fn [] (:x this))
          :y (fn [] (:y this))

          :add-instance-values
          (fn [x y]
            (assoc this :x x :y y))
          
          :to-string
          (fn []
            (cl-format nil "A ~A like this: [~A, ~A]"
                       (send-to this :class-name)
                       (send-to this :x)
                       (send-to this :y)))
          :shift
          (fn [xinc yinc]
            (let [my-class (send-to this :class)]
              (send-to my-class :new
                                (+ (send-to this :x) xinc)
                                (+ (send-to this :y) yinc))))
          :add
          (fn [other]
            (send-to this :shift (send-to other :x)
                                 (send-to other :y)))
         } 
         
         {
          :origin (fn [] (send-to this :new 0 0))
         })

"clueby 0.1 (2012-10-02 patchlevel 0)"
