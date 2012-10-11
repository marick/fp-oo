;;; `declare` is a forward declaration. It "predefines" a symbol with
;;; no value, which allows you to write code that uses functions you
;;; haven't defined yet. I'm using this so that this code can
;;; (roughly) match the order of the book.
(declare class-from-instance send-to make)

(def Anything
{
  :__own_symbol__ 'Anything
  :__instance_methods__
  {
    :add-instance-values identity
    :class-name :__class_symbol__    
    :class (fn [this] (class-from-instance this))
   }
 })

(def Point
{
  :__own_symbol__ 'Point
  :__superclass_symbol__ 'Anything   ;; <<= New
  :__instance_methods__
  {
    :add-instance-values (fn [this x y]
                           (assoc this :x x :y y))
    :shift (fn [this xinc yinc]
             (make Point (+ (:x this) xinc)
                         (+ (:y this) yinc)))
    :add (fn [this other]
           (send-to this :shift (:x other)
                                (:y other)))
   }
 })


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

(declare lineage)

(def method-cache
     (fn [class]
       (let [class-symbol (:__own_symbol__ class)
             method-maps (map class-instance-methods
                              (lineage class-symbol))]
         (apply merge method-maps))))

(def lineage-1
     (fn [class-symbol so-far]
       (if (nil? class-symbol)
         so-far
         (lineage-1 (class-symbol-above class-symbol)
                    (cons class-symbol so-far)))))
(def lineage
     (fn [class-symbol]
       (lineage-1 class-symbol [])))

(def apply-message-to
     (fn [class instance message args]
         (apply (message (method-cache class))
                instance args)))

(def make
     (fn [class & args]
       (let [seeded {:__class_symbol__ (:__own_symbol__ class)}]
         (apply-message-to class seeded :add-instance-values args))))

(def send-to
     (fn [instance message & args]
       (apply-message-to (class-from-instance instance)
                         instance message args)))

;; This imports a function from another namespace. (Think package or module.)
(use '[clojure.pprint :only [cl-format]])

;;; For the exercises

(declare send-super)

(def Anything
{
  :__own_symbol__ 'Anything
  :__instance_methods__
  {
   :add-instance-values identity
   ;; vvvvv                             New
   :to-string (fn [this] (str this))
   :method-missing
   (fn [this message args]
     (throw (Error. (cl-format nil "A ~A does not accept the message ~A."
                               (send-to this :class-name)
                               message))))
   ;; ^^^^^                             New
   
   :class-name :__class_symbol__    
   :class (fn [this] (class-from-instance this))
   }
 })

(def Point
{
  :__own_symbol__ 'Point
  :__superclass_symbol__ 'Anything
  :__instance_methods__
  {
   ;; vvvvv                             New
   :to-string
   (fn [this]
     (str "A point like this: "
          (send-super this :to-string)))
   ;; ^^^^^                             New
   :add-instance-values (fn [this x y]
                           (assoc this :x x :y y))
   :shift (fn [this xinc yinc]
            (make Point (+ (:x this) xinc)
                        (+ (:y this) yinc)))
   :add (fn [this other]
           (send-to this :shift (:x other)
                                (:y other)))
   }
 })

;;;; These two test classes let you watch dispatching in progress.
;; To see them in action, do this:
;;
;;    (send-to (make SubClass 1 2) :summer 3)
;;
;; Note that you have to have implemented send-super first.
(declare send-super)

;; This makes available a more convenient string-formatting and printing function
(use '[clojure.pprint :only [cl-format]])

(def SuperClass
{
  :__own_symbol__ 'SuperClass
  :__instance_methods__
  {
   :add-instance-values
   (fn [this val]
     (cl-format true ">>>> SUPERCLASS constructor to add on ~A to ~A.~%" val this)
     (let [retval (assoc this :super val)]
       (cl-format true "<<<< SUPERCLASS constructor returns ~A.~%" retval)
       retval))


   :super-val
   (fn [this]
     (cl-format true ">>>>>> SUPERCLASS accessor :super-val applied to ~A.~%" this)
     (let [retval (:super this)]
       (cl-format true "<<<<<< SUPERCLASS accessor :super-val returns ~A.~%" retval)
       retval))

   :summer
   (fn [this val]
     (cl-format true ">>>> SUPERCLASS :summer of ~A to add ~A.~%" this val)
     (let [retval (+ val (send-to this :super-val))]
       (cl-format true "<<<< SUPERCLASS :summer returns ~A.~%" retval)
       retval))
  }
 })

(def SubClass
{
  :__own_symbol__ 'SubClass
  :__superclass_symbol__ 'SuperClass
  :__instance_methods__
  {
   :add-instance-values
   (fn [this x y]
     (cl-format true ">> subclass constructor given ~A and ~A.~%" x y)
     (let [retval (assoc (send-super this :add-instance-values x) :sub y)]
       (cl-format true "<< subclass constructor returns ~A.~%" retval)
       retval))

   :sub-val
   (fn [this]
     (cl-format true ">>>> subclass accessor :sub-val applied to ~A.~%" this)
     (let [retval (:sub this)]
       (cl-format true "<<<< subclass accessor :sub-val returns ~A.~%" retval)
       retval))

   :summer
   (fn [this val]
     (cl-format true ">> subclass :summer of ~A to add ~A.~%" this val)
     (let [retval (+ (send-to this :sub-val)
                     (send-super this :summer val))]
     (cl-format true "<< subclass :summer returns A.~%" retval)
     retval))
   }
 })



;; These test classes can be used to experiment with
;; :method-missing

(def MissingOverrider
{
  :__own_symbol__ 'MissingOverrider
  :__superclass_symbol__ 'Anything
  :__instance_methods__
  {
   :method-missing
   (fn [this message args]
     (println (cl-format nil "method-missing called! ~A on ~A." message this))
     (println (cl-format nil "The arguments were ~A." args)))
  }
 })

;; Does a method-missing override work?
;; (send-to (make MissingOverrider) :queen-bee "Dawn")


(def SuperSender
{
  :__own_symbol__ 'SuperSender
  :__superclass_symbol__ 'Anything
  :__instance_methods__
  {
   :overrides-nothing
   (fn [this]
     (send-super this :overrides-nothing))
  }
 })

;; Does method-missing get called when `send-super` is incorrectly used?
;; (send-to (make SuperSender) :overrides-nothing)

