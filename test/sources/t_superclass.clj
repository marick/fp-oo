(ns sources.t-superclass
  (:use midje.sweet))

(load-file "sources/superclass.clj")

(fact
  (class-symbol-above 'Point) => 'Anything)

(fact
  (keys (class-instance-methods 'Point)) => (contains :shift)
  (keys (class-instance-methods 'Point)) =not=> (contains :class))

(fact
  (class-from-instance (a Point 1 2)) => Point)

(fact
  (lineage 'Anything) => ['Anything]
  (lineage 'Point) => ['Anything 'Point])

(fact
  (:class (method-cache Point)) => (exactly (:class (class-instance-methods 'Anything)))
  (:add (method-cache Point)) => (exactly (:add (class-instance-methods 'Point)))
  (:add-instance-values (method-cache Point))
  => (exactly (:add-instance-values (class-instance-methods 'Point))))

(fact
  (apply-message-to Point (a Point 1 2) :class-name '()) => 'Point
  (apply-message-to Point (a Point 1 2) :shift '(1 2)) => (a Point 2 4))

(fact
  (send-to (a Point 1 2) :add (a Point 2 1)) => (a Point 3 3)
  (send-to (a Point 1 2) :class) => Point
  (send-to (a Anything) :class) => Anything)



  
