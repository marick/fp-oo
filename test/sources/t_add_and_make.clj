(ns sources.t-add-and-make
  (:use midje.sweet))

(load-file "sources/add-and-make.clj")

(fact
  (Point 1 2) => {:x 1 :y 2 :__class_symbol__ 'Point}
  (let [new-point (shift (Point 1 2) 1 2)]
    (x new-point) => 2
    (y new-point) => 4
    (class-of new-point) => 'Point))
  
