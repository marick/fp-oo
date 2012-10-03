(ns sources.t-class-object
  (:use midje.sweet))

(load-file "sources/class-object.clj")

(def object (send-to Anything :new))
     
(fact "instance methods on Anything"
  (send-to object :to-string) => "{:__class_symbol__ Anything}"
  (send-to object :class-name) => 'Anything
  (send-to object :class) => Anything)

(def point (send-to Point :new 1 2))

(fact "instance methods on Point"
  (send-to point :shift 1 2) => (send-to Point :new 2 4)
  (send-to point :add point) => (send-to Point :new 2 4)
  (send-to point :x) => 1
  (send-to point :y) => 2
  (send-to point :to-string) => "A Point like this: [1, 2]"
  (send-to point :class-name) => 'Point
  (send-to point :class) => Point)

(fact "class methods for Point"
  (send-to Point :origin) => (send-to Point :new 0 0))

