(ns sources.t-class
  (:use midje.sweet))

(load-file "sources/class.clj")

(fact
  (let [point (make Point 1 2)]
    (:x point) => 1
    (:y point) => 2
    (send-to point :class) => 'Point
    (send-to point :shift 1 2) => (make Point 2 4)
    (send-to point :add (make Point -1 -2)) => (make Point 0 0)))

(fact
  (:held (make Holder "held")) => "held")
