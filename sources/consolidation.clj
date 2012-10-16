(use '[clojure.pprint :only [cl-format]])

;;; Context available to all messages

(def ^:dynamic this nil)
(def ^:dynamic *active-message* nil)

;;; Public interface
(declare activate-method fresh-active-message using-method-above)

(def send-to
     (fn [instance message-name & args]
       (activate-method (fresh-active-message instance message-name args))))

(def repeat-to-super
     (fn []
       (activate-method (using-method-above *active-message*))))
       
(def send-super
     (fn [& args]
       (activate-method (assoc (using-method-above *active-message*)
                               :args args))))



;;; Bootstrapping the first few classes
;;; The following methods represent the core ideas needed
;;; to hand-construct classes.

(def basic-object
     "A basic object is the least special object.
      The minimal instance."
     (fn [method-holder-symbol]
       {:__left_symbol__ method-holder-symbol}))


(def method-holder
     "Method Holders are either classes or modules"
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
     "Bind a particular method holder to a symbol"
     (fn [method-holder]
         (intern *ns* (:__own_symbol__ method-holder) method-holder)
         method-holder))

(def invisible
     "Return a method holder that will normally be invisible
      to the user."
     (fn [method-holder]
       (assoc method-holder :__invisible__ true)))


;;; These are methods that happen to be used in core class
;;; instance methods.

(def metasymbol
     (fn [some-symbol]
       (symbol (str "Meta" some-symbol))))

(def invisible?
     (fn [method-holder-symbol] (:__invisible__ (eval method-holder-symbol))))

(def names-module-stub?
     "Module stubs indirect to modules. They allow module
      redefinitions to apply immediately to classes that have
      already included them."
     (fn [symbol]
       (:__module_stub?__ (eval symbol))))

;; These others I prefer to define later
(declare left-from-instance lineage)



;;; Core predefined classes

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
                             (apply send-to seeded :add-instance-values args)))

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


;;; Navigating the object graph

(def method-holder-symbol-above
     (fn [method-holder-symbol]
       (assert (symbol? method-holder-symbol))
       (:__up_symbol__ (eval method-holder-symbol))))

(def method-holder-symbol-to-left
     (fn [symbol]
       (assert (symbol? symbol))
       (:__left_symbol__ (eval symbol))))

(def left-from-instance
     (fn [instance]
       (assert (map? instance))
       (eval (:__left_symbol__ instance))))

(declare lineage-1)

(def lineage
     "Return, in the form of symbols, a sequence of the
      symbol names of all the method holders above the
      the given method-holder symbol. The given symbol
      appears last in the list and `Anything` appears first."
     (fn [method-holder-symbol]
       (lineage-1 method-holder-symbol [])))


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

;;; Constructing messages

(def held-methods
     "Return a map by name of what methods are
      held by the method holder named by the symbol."
     (fn [method-holder-symbol]
       (assert (symbol? method-holder-symbol))
       (:__methods__ (eval method-holder-symbol))))

(def find-containing-holder-symbol
     "Starting at `first-candidate`, find the
      first method-holder that can handle the
      message. Return `nil` if none can."
     (fn [first-candidate message-name]
       (first (filter (fn [holder-symbol]
                        (message-name (held-methods holder-symbol)))
                      (reverse (lineage first-candidate))))))

(def fresh-active-message
     (fn [target name args]
       "Construct the message corresponding to the
      attempt to send the particular `name` to the
      `target` with the given `args`. If there is no
      matching method, the message becomes one that
      sends `:method-missing` to the target."
       (let [holder-name (find-containing-holder-symbol (:__left_symbol__ target)
                                                        name)]
             (if holder-name
               {:name name, :holder-name holder-name, :args args, :target target}
               (fresh-active-message target
                                     :method-missing
                                     (vector name args))))))


(def using-method-above
     "Use this with a message that has already been created 
      needs to be re-sent to a method in `:up` from the
      method holder used last. If there is no method in the
      lineage above that method holder, an error is thrown."
     (fn [active-message]
       (let [symbol-above (method-holder-symbol-above (:holder-name active-message))
             holder-name (find-containing-holder-symbol symbol-above
                                                        (:name active-message))]
         (if holder-name
           (assoc active-message :holder-name holder-name)
           (throw (Error. (str "No superclass method `" (:name active-message)
                           "` above `" (:holder-name active-message)
                           "`.")))))))

;; Activating messages

(def method-to-run
     "Convert the holder and message names in the `message` (both symbols)
      to a function value to apply."
     (fn [active-message]
       (get (held-methods (:holder-name active-message))
            (:name active-message))))


(def activate-method
     (fn [active-message]
       (binding [*active-message* active-message
                 this (:target active-message)]
         (apply (method-to-run active-message)
                (:args active-message)))))


;;; Non-core classes

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
                                (+ (:x this) xinc)
                                (+ (:y this) yinc))))
          :add
          (fn [other]
            (send-to this :shift (:x other)
                                 (:y other)))
         } 
         
         {
          :origin (fn [] (send-to this :new 0 0))
         })

          
"clueby 0.2 (2012-10-02 patchlevel 0)"
