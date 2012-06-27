(def method-from-message
     (fn [message class]
       (message (:__instance_methods__ class))))

(def class-from-instance
     (fn [instance]
       (eval (:__class_symbol__ instance))))

(def apply-message-to
     (fn [class instance message args]
       (apply (method-from-message message class)
              instance args)))

(def a
     (fn [class & args]
       (let [seeded {:__class_symbol__ (:__symbol__ class)}]
         (apply-message-to class seeded :add-instance-values args))))

(def send-to
     (fn [instance message & args]
       (apply-message-to (class-from-instance instance)
                         instance message args)))

(def Anything
{
  :__symbol__ 'Point
  :__instance_methods__
  {
    :add-instance-values (fn [this])
    :class-name :__class_symbol__    
    :class (fn [this] (class-from-instance this))
   }
 })

(def class-symbol-above
     (fn [class-symbol]
        (:__superclass_symbol__ (eval class-symbol))))

(def forebears
     (fn [class-symbol]
       (prn class-symbol)
       (if class-symbol
           (forebears class-symbol-above))))

(def forebears 
     (fn [class-symbol so-far]
       (if (nil? class-symbol)
         so-far
         (forebears (class-symbol-above class-symbol)
                    (cons class-symbol so-far)))))

(def instance-methods
     (fn [class-symbol]
       (:__instance_methods__ (eval class-symbol))))



(def available-methods
     (fn [class-symbol]
       (let [method-maps (map instance-methods (forebears class-symbol []))]
         (apply merge method-maps))))
         


(def Point
{
  :__symbol__ 'Point
  :__superclass_symbol__ 'Anything
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


;; To show shadowing (make them do it as an exercise)

(def Point
{
  :__symbol__ 'Point
  :__superclass_symbol__ 'Anything
  :__instance_methods__
  {
    :class-name (fn [this]
                  "I am a POINT and don't you forget it!!")
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


(def send-super
     (fn [instance message & args]
       (apply-message-to (class-symbol-above (class-from-instance instance))
                         instance message args)))

