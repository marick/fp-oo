;;; `declare` is a forward declaration. It "predefines" a symbol with
;;; no value, which allows you to write code that uses functions you
;;; haven't defined yet. I'm using this so that this code can
;;; (roughly) match the order of the book.
(declare class-from-instance send-to a)

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
             (a Point (+ (:x this) xinc)
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

(def a
     (fn [class & args]
       (let [seeded {:__class_symbol__ (:__own_symbol__ class)}]
         (apply-message-to class seeded :add-instance-values args))))

(def send-to
     (fn [instance message & args]
       (apply-message-to (class-from-instance instance)
                         instance message args)))
