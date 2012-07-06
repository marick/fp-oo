(ns solutions.t-class-name
   (:use midje.sweet))

(load-file "sources/class-creation.clj")
(load-file "sources/ruby-classes.clj")
(load-file "solutions/class-name.clj")

(fact
  (send-to (send-to Anything :new) :class-name) => 'Anything
  (send-to (send-to Anything :new) :class) => Anything

  (send-to Anything :class-name) => 'Klass
  (send-to Anything :class) => Klass

  (send-to Klass :class-name) => 'Klass
  (send-to Klass :class) => Klass
  
  (send-to (send-to Point :new 1 2) :class-name) => 'Point
  (send-to Point :class) => Klass
  (send-to Point :class-name) => 'Klass)

(send-to Klass :new
         'ColoredPoint 'Point
         {
          :color :color
         
          :add-instance-values
          (fn [this x y color]
            (assoc (send-super this :add-instance-values x y)
              :color color))

          :shift
          (fn [this x y]
            (let [raw (send-to Point :new (:x this) (:y this))
                  shifted (send-to raw :shift x y)]
              (send-to (send-to this :class) :new (:x shifted) (:y shifted) (:color this))))
         }
         {
          :origin (fn [class]
                    (send-to class :new 0 0 'white))
          })

(fact "construction was correct"
  (let [point (send-to ColoredPoint :new 1 2 'red)]
    (send-to point :x) => 1
    (send-to point :y) => 2
    (send-to point :color) => 'red
    (send-to point :class-name) => 'ColoredPoint
    (send-to point :shift 2 3) => (send-to ColoredPoint :new 3 5 'red)))

(fact "class works correctly"
  (send-to ColoredPoint :class) => Klass
  (send-to ColoredPoint :class-name) => 'Klass)

(fact
  (send-to Point :ancestors) => '[Point Anything]
  (send-to ColoredPoint :ancestors) => '[ColoredPoint Point Anything]
  (send-to Klass :ancestors) => '[Klass Anything]
  (send-to (eval (:__class_symbol__ Point)) :ancestors) => '[Klass Anything])
