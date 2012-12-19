(ns sources.t-triangle
  (:use midje.sweet))

(load-file "sources/add-and-make.clj")
(load-file "solutions/add-and-make.clj")
(load-file "sources/triangle.clj")


(fact "base constructor"
  (let [tri (Triangle (Point 1 2)
                      (Point 1 3)
                      (Point 3 1))]
    (contains? tri (Point 1 2)) => true
    (contains? tri (Point 1 3)) => true
    (contains? tri (Point 3 1)) => true))

(fact "triangles can be made"
  (let [tri (make Triangle (make Point 1 2)
                           (make Point 1 3)
                           (make Point 3 1))]
    (contains? tri (make Point 1 2)) => true
    (contains? tri (make Point 1 3)) => true
    (contains? tri (make Point 3 1)) => true))

