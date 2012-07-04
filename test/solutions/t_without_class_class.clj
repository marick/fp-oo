(ns solutions.t-without-class-class
  (:use midje.sweet))

(load-file "sources/without-class-class.clj")
(load-file "solutions/pieces/without-class-class-1.clj")

;;; As before...
(def object (send-to Anything :new))
     
(fact "instance methods on Anything"
  (send-to object :shift) => (throws Error)
  (send-to object :to-string) => "{:__class_symbol__ Anything}"
  (send-to object :class-name) => 'Anything
  (send-to object :class) => Anything)

(def point (send-to Point :new 1 2))

(fact "instance methods on Point"
  (send-to point :shift 1 2) => (send-to Point :new 2 4)
  (send-to point :add point) => (send-to Point :new 2 4)
  (send-to point :x) => 1
  (send-to point :y) => 2
  (send-to point :to-string) => "{:y 2, :x 1, :__class_symbol__ Point}"
  (send-to point :class-name) => 'Point
  (send-to point :class) => Point)

(fact "class methods for Point"
  (send-to Point :origin) => (send-to Point :new 0 0))

;;; And...
(fact "method-missing works on classes"
  (send-to Point :unknown) => (throws Error)
  (send-to Anything :to-string) => (contains "__own_symbol__ Anything")
  (send-to Anything :class) => MetaAnything
  (send-to Anything :class-name) => 'MetaAnything)
  

(load-file "solutions/pieces/without-class-class-2.clj")

(fact "method-missing works on classes"
  (send-to MetaPoint :unknown) => (throws Error)
  (send-to MetaPoint :to-string) => (contains "__own_symbol__ MetaPoint")
  (send-to MetaPoint :class) => Anything
  (send-to MetaAnything :class-name) => 'Anything

  (send-to MetaAnything :to-string) => (contains "__own_symbol__ MetaAnything")
  (send-to MetaAnything :class) => Anything
  (send-to MetaAnything :class-name) => 'Anything)
