(ns sources.t-class-creation
  (:use midje.sweet))

(load-file "sources/class-creation.clj")

(send-to ClassMaker :new
         'Point 'Anything
         {
          :x :x
          :y :y 

          :add-instance-values
          (fn [this x y] (assoc this :x x :y y))
          
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

(fact
  (send-to Point :origin) => (send-to Point :new 0 0)

  (let [point (send-to Point :new 1 2)]
    (send-to point :x) => 1
    (send-to point :y) => 2
    (send-to point :class-name) => 'Point
    (send-to point :class) => Point
    (send-to point :add (send-to Point :new 1 2))
    => (send-to Point :new 2 4)))


(fact "Anything behavior still works"
  (send-to (send-to Anything :new) :to-string) => "{:__class_symbol__ Anything}")
