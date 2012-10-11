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
             (make Point (+ (:x this) xinc)
                         (+ (:y this) yinc)))
    :add (fn [this other]
           (send-to this :shift (:x other)
                                (:y other)))
   }
 })

(send-to (make Point 1 2) :class-name)
(send-to (make Point 1 2) :class)


