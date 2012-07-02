;;; Exercise 2

(def Point
{
  :__own_symbol__ 'Point
  :__instance_methods__
  {
    :add-instance-values (fn [this x y]
                           (assoc this :x x :y y))
    ;;                                   vvvvv== New
    :class-name :__class_symbol__    
    :class (fn [this] (class-from-instance this))
    ;;                                   ^^^^^== New
    :shift (fn [this xinc yinc]
             (a Point (+ (:x this) xinc)
                      (+ (:y this) yinc)))
    :add (fn [this other]
           (send-to this :shift (:x other)
                                (:y other)))
   }
 })

(prn (send-to (a Point 1 2) :class-name))
(prn (send-to (a Point 1 2) :class))


