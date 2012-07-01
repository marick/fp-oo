(declare class-from-instance send-to apply-message-to)
;; This imports a function from another namespace. (Think package or module.)
(use '[clojure.pprint :only [cl-format]])


(def MetaAnything
{
  :__own_symbol__ 'MetaAnything
  :__instance_methods__
  {
  }
})

(def MetaClasser
{
  :__own_symbol__ 'MetaClasser
  :__instance_methods__
  {
  }
})

(def Classer
{
  :__own_symbol__ 'Classer
  :__class_symbol__ 'MetaClasser
  :__instance_methods__
  {
   :new
   (fn [class & args]
     (let [seeded {:__class_symbol__ (:__own_symbol__ class)}]
       (apply-message-to class seeded :add-instance-values args)))
   }
})
  

(def MetaPoint
{
  :__own_symbol__ 'MetaPoint
  :__superclass_symbol__ 'Classer
  :__instance_methods__
  {
   :origin (fn [class] (send-to class :new 0 0))
   }
})


(def Anything
{
  :__own_symbol__ 'Anything
  :__class_symbol__ 'MetaAnything
  :__instance_methods__
  {
    :add-instance-values identity
    :to-string (fn [this] (str this))
    :method-missing
    (fn [this message args]
      (throw (Error. (cl-format nil "A ~A does not accept the message ~A."
                                (send-to this :class-name)
                                message))))
    :class-name :__class_symbol__    
    :class (fn [this] (class-from-instance this))
   }
 })

(def Point
{
  :__own_symbol__ 'Point
  :__class_symbol__ 'MetaPoint
  :__superclass_symbol__ 'Anything
  :__instance_methods__
  {
    :x :x
    :y :y 
    :add-instance-values (fn [this x y]
                           (assoc this :x x :y y))
    :shift (fn [this xinc yinc]
             (send-to Point :new (+ (:x this) xinc)
                                 (+ (:y this) yinc)))
    :add (fn [this other]
           (send-to this :shift (:x other)
                                (:y other)))
   }
 })


(def print-and-add
     (memoize
      (fn [x y]
        (let [result (+ x y)]
          (println x "plus" y "is" result)
          result))))



