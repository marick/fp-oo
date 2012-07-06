(ns solutions.ts-add-and-a
  (:use midje.sweet))

(load-file "sources/add-and-a.clj")
(load-file "solutions/pieces/add-and-a-1a.clj")

(fact 
  (add (Point 1 2) (Point 3 5)) => (Point 4 7))

(load-file "solutions/pieces/add-and-a-1b.clj")

(fact 
  (add (Point 1 2) (Point 3 5)) => (Point 4 7))

(load-file "solutions/pieces/add-and-a-2a.clj")
(fact
  (a Point 1 2) => (Point 1 2))

(load-file "solutions/pieces/add-and-a-2b.clj")

(fact
  (let [triangle  (a Triangle (a Point 1 2) (a Point 1 3) (a Point 3 1))]
    (:point1 triangle) => (a Point 1 2)))

(load-file "solutions/pieces/add-and-a-3.clj")

(fact
  (equal-triangles? right-triangle equal-right-triangle) => truthy
  (equal-triangles? right-triangle different-triangle) => falsey)


(fact
  (equal-triangles? right-triangle equal-right-triangle different-triangle) => falsey)

(load-file "solutions/pieces/add-and-a-5a.clj")

(fact 
  (valid-triangle? (a Point 0 0) (a Point 0 0) (a Point 3 4)) => falsey
  (valid-triangle? (a Point 0 0) (a Point 0 3) (a Point 3 0)) => truthy)


(load-file "solutions/pieces/add-and-a-5b.clj")

(fact 
  (valid-triangle? (a Point 0 0) (a Point 0 1)) => falsey
  (valid-triangle? (a Point 0 0) (a Point 0 0) (a Point 3 4)) => falsey
  (valid-triangle? (a Point 0 0) (a Point 0 3) (a Point 3 0)) => truthy)

(load-file "solutions/pieces/add-and-a-5c.clj")

(fact
  (valid-2d-figure? (a Point 0 0) (a Point 0 1)) => truthy
  (valid-2d-figure? (a Point 0 0) (a Point 0 0) (a Point 3 4)) => falsey
  (valid-2d-figure? (a Point 0 0) (a Point 0 3) (a Point 3 0)) => truthy)
