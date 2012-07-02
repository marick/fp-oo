(ns solutions.t-superclass
  (:use midje.sweet))

(load-file "sources/superclass.clj")
(load-file "solutions/pieces/superclass-1.clj")

(fact
  (send-to (a Anything) :to-string) => "{:__class_symbol__ Anything}"
  ;; Note: following too will fail if ordering of keys ever changes.
  (send-super (a Point 1 2) :to-string) => "{:y 2, :x 1, :__class_symbol__ Point}"
  (send-to (a Point 1 2) :to-string) => "A point like this: {:y 2, :x 1, :__class_symbol__ Point}"
  (send-to (a Point 1 2) :shift 1 2) => (a Point 2 4))


(fact
  (let [sub (a SubClass 1 2)]
    (send-to sub :super-val) => 1
    (send-to sub :sub-val) => 2
    (send-to sub :summer 3) => 6))

(load-file "solutions/pieces/superclass-2.clj")


(load-file "solutions/pieces/superclass-3a.clj")

(fact
  (apply-message-to Point (a Point 1 2) :class-name '()) => 'Point
  (apply-message-to Point (a Point 1 2) :shift '(1 2)) => (a Point 2 4))

(fact
  (send-to (a Point 1 2) :add (a Point 2 1)) => (a Point 3 3)
  (send-to (a Point 1 2) :class) => Point
  (send-to (a Anything) :class) => Anything)


(fact
  (send-to (a MissingOverrider) :queen-bee "Dawn") => nil
  (send-to (a SuperSender) :overrides-nothing) => (throws Error))

(load-file "solutions/pieces/superclass-3b.clj")

(fact
  (apply-message-to Point (a Point 1 2) :class-name '()) => 'Point
  (apply-message-to Point (a Point 1 2) :shift '(1 2)) => (a Point 2 4))

(fact
  (send-to (a Point 1 2) :add (a Point 2 1)) => (a Point 3 3)
  (send-to (a Point 1 2) :class) => Point
  (send-to (a Anything) :class) => Anything)


(fact
  (send-to (a MissingOverrider) :queen-bee "Dawn") => nil
  (send-to (a SuperSender) :overrides-nothing) => (throws Error))

(load-file "solutions/pieces/superclass-3c.clj")

(fact
  (apply-message-to Point (a Point 1 2) :class-name '()) => 'Point
  (apply-message-to Point (a Point 1 2) :shift '(1 2)) => (a Point 2 4))

(fact
  (send-to (a Point 1 2) :add (a Point 2 1)) => (a Point 3 3)
  (send-to (a Point 1 2) :class) => Point
  (send-to (a Anything) :class) => Anything)


(fact
  (send-to (a MissingOverrider) :queen-bee "Dawn") => nil
  (send-to (a SuperSender) :overrides-nothing) => (throws Error))
