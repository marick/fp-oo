(ns sources.t-embedded-functions
  (:use midje.sweet))

(load-file "sources/embedded-functions.clj")

(fact
  (let [point (send-to (make Point 1 2) :shift 1 2)]
    (:x point) => 2
    (:y point) => 4)
  (send-to (make Point 1 2) :class) => 'Point)
