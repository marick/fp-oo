(ns solutions.ts-embedded-functions
  (:use midje.sweet))


(load-file "solutions/embedded-functions.clj")

(fact
  (let [point (send-to (make Point 1 2) :shift 1 2)]
    (:x point) => 2
    (:y point) => 4)
  (send-to (make Point 1 2) :class) => 'Point)

(fact
  (let [my-point (make Point 1 2)]
    (send-to my-point :x) => 1
    (send-to my-point :y) => 2
    (let [shifted (send-to my-point :shift -1 -100)]
      (send-to shifted :x) => 0
      (send-to shifted :y) => -98)
    (let [added (send-to my-point :add (make Point -1 -100))]
      (send-to added :x) => 0
      (send-to added :y) => -98)))
