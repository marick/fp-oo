(ns solutions.ts-superclass
  (:use midje.sweet))

(load-file "sources/superclass.clj")
(load-file "solutions/pieces/superclass-1.clj")

(fact
  (send-to (make Anything) :to-string) => "{:__class_symbol__ Anything}"
  ;; Note: following too will fail if ordering of keys ever changes.
  (send-super (make Point 1 2) :to-string) => "{:y 2, :x 1, :__class_symbol__ Point}"
  (send-to (make Point 1 2) :to-string) => "A point like this: {:y 2, :x 1, :__class_symbol__ Point}"
  (send-to (make Point 1 2) :shift 1 2) => (make Point 2 4))


(fact
  (let [sub (make SubClass 1 2)]
    (send-to sub :super-val) => 1
    (send-to sub :sub-val) => 2
    (send-to sub :summer 3) => 6))

(load-file "solutions/pieces/superclass-2.clj")


(load-file "solutions/pieces/superclass-3a.clj")

(fact
  (apply-message-to Point (make Point 1 2) :class-name '()) => 'Point
  (apply-message-to Point (make Point 1 2) :shift '(1 2)) => (make Point 2 4))

(fact
  (send-to (make Point 1 2) :add (make Point 2 1)) => (make Point 3 3)
  (send-to (make Point 1 2) :class) => Point
  (send-to (make Anything) :class) => Anything)


(fact
  (send-to (make MissingOverrider) :queen-bee "Dawn") => nil
  (send-to (make SuperSender) :overrides-nothing) => (throws Error))

(load-file "solutions/pieces/superclass-3b.clj")

(fact
  (apply-message-to Point (make Point 1 2) :class-name '()) => 'Point
  (apply-message-to Point (make Point 1 2) :shift '(1 2)) => (make Point 2 4))

(fact
  (send-to (make Point 1 2) :add (make Point 2 1)) => (make Point 3 3)
  (send-to (make Point 1 2) :class) => Point
  (send-to (make Anything) :class) => Anything)


(fact
  (send-to (make MissingOverrider) :queen-bee "Dawn") => nil
  (send-to (make SuperSender) :overrides-nothing) => (throws Error))

(load-file "solutions/pieces/superclass-3c.clj")

(fact
  (apply-message-to Point (make Point 1 2) :class-name '()) => 'Point
  (apply-message-to Point (make Point 1 2) :shift '(1 2)) => (make Point 2 4))

(fact
  (send-to (make Point 1 2) :add (make Point 2 1)) => (make Point 3 3)
  (send-to (make Point 1 2) :class) => Point
  (send-to (make Anything) :class) => Anything)


(fact
  (send-to (make MissingOverrider) :queen-bee "Dawn") => nil
  (send-to (make SuperSender) :overrides-nothing) => (throws Error))
