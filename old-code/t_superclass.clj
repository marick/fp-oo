(ns sources.t-superclass
  (:use midje.sweet))

(load-file "sources/superclass.clj")

(fact
  (class-symbol-above 'Point) => 'Anything)

(fact
  (keys (class-instance-methods 'Point)) => (contains :shift)
  (keys (class-instance-methods 'Point)) =not=> (contains :class))

(fact
  (class-from-instance (make Point 1 2)) => Point)

(fact
  (lineage 'Anything) => ['Anything]
  (lineage 'Point) => ['Anything 'Point])

(fact
  (:class (method-cache Point)) => (exactly (:class (class-instance-methods 'Anything)))
  (:add (method-cache Point)) => (exactly (:add (class-instance-methods 'Point)))
  (:add-instance-values (method-cache Point))
  => (exactly (:add-instance-values (class-instance-methods 'Point))))

(fact
  (apply-message-to Point (make Point 1 2) :class-name '()) => 'Point
  (apply-message-to Point (make Point 1 2) :shift '(1 2)) => (make Point 2 4))

(fact
  (send-to (make Point 1 2) :add (make Point 2 1)) => (make Point 3 3)
  (send-to (make Point 1 2) :class) => Point
  (send-to (make Anything) :class) => Anything)



  
