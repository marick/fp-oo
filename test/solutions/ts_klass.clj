(ns solutions.ts-klass
   (:use midje.sweet))

(load-file "sources/klass-1.clj")
(load-file "sources/klass-2.clj")
(load-file "solutions/pieces/klass-1.clj")

(fact
  (send-to Point :to-string) => "class Point"
  (send-to Klass :to-string) => "class Klass"
  (send-to (send-to Anything :new) :to-string) => "{:__class_symbol__ Anything}")




(load-file "solutions/pieces/klass-2.clj")

;; Let's check that the new subclass has been build correctly.

(fact "construction was correct"
  (let [point (send-to ColoredPoint :new 1 2 'red)]
    (send-to point :x) => 1
    (send-to point :y) => 2
    (send-to point :color) => 'red
    (send-to point :class-name) => 'ColoredPoint))

(fact
  (send-to Point :ancestors) => '[Point Anything]
  (send-to ColoredPoint :ancestors) => '[ColoredPoint Point Anything]
  (send-to Klass :ancestors) => '[Klass Anything]
  (send-to (send-to Anything :new) :ancestors) => (throws Error))

(fact "the metaclass case"
  (send-to MetaPoint :ancestors) => '[Klass Anything]
  (send-to MetaColoredPoint :ancestors) => '[Klass Anything])

(load-file "solutions/pieces/klass-3.clj")

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


(fact "class works correctly"
  (send-to ColoredPoint :class) => Klass
  (send-to ColoredPoint :class-name) => 'Klass)

