(ns sources.t-klass-2
   (:use midje.sweet))

(load-file "sources/klass-1.clj")
(load-file "sources/klass-2.clj")

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


(fact "Invisible classes are still not ignored."
  (send-to Point :class-name) => 'MetaPoint)

