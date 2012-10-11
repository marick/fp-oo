(ns solutions.ts-class
  (:use midje.sweet))

(load-file "sources/class.clj")
(load-file "solutions/pieces/class-1.clj")

(fact
  (let [method (method-from-message :class Point)]
    (method (make Point 1 2)) => 'Point))

(fact
  (class-from-instance (make Holder "held")) => Holder)

(fact
  (send-to (make Point 1 2) :add (make Point 2 3)) => (make Point 3 5))


(load-file "solutions/pieces/class-2.clj")

(fact
  (send-to (make Point 1 2) :class-name) => 'Point
  (send-to (make Point 1 2) :class) => Point)

(def point (make Point 1 2))
(fact (method-from-message :origin Point) => nil)
(load-file "solutions/pieces/class-3.clj")
(fact
  (method-from-message :origin Point) =not=> nil
  (send-to (make Point 1 2) :origin) => (make Point 0 0))

(fact (send-to (make Point 1 2) :x) => (throws Exception))
(load-file "solutions/pieces/class-4.clj")
(fact
  (send-to (make Point 1 2) :x) => 1
  (send-to (make Holder "foo") :held) => "foo")


(fact (send-to (make Point 1 2) :unknown-message) => nil)
