(ns solutions.ts-add-and-make
  (:use midje.sweet))

(load-file "sources/add-and-make.clj")
(load-file "solutions/pieces/add-and-make-1a.clj")

(fact 
  (add (Point 1 2) (Point 3 5)) => (Point 4 7))

(load-file "solutions/pieces/add-and-make-1b.clj")

(fact 
  (add (Point 1 2) (Point 3 5)) => (Point 4 7))

(load-file "solutions/pieces/add-and-make-2a.clj")
(fact
  (make Point 1 2) => (Point 1 2))

(load-file "solutions/pieces/add-and-make-2b.clj")

(fact
  (let [triangle  (make Triangle (make Point 1 2) (make Point 1 3) (make Point 3 1))]
    (:point1 triangle) => (make Point 1 2)))

(load-file "solutions/pieces/add-and-make-3.clj")

(fact
  (equal-triangles? right-triangle equal-right-triangle) => truthy
  (equal-triangles? right-triangle different-triangle) => falsey)


(fact
  (equal-triangles? right-triangle equal-right-triangle different-triangle) => falsey)

(load-file "solutions/pieces/add-and-make-5a.clj")

(fact 
  (valid-triangle? (make Point 0 0) (make Point 0 0) (make Point 3 4)) => falsey
  (valid-triangle? (make Point 0 0) (make Point 0 3) (make Point 3 0)) => truthy)


(load-file "solutions/pieces/add-and-make-5b.clj")

(fact 
  (valid-triangle? (make Point 0 0) (make Point 0 1)) => falsey
  (valid-triangle? (make Point 0 0) (make Point 0 0) (make Point 3 4)) => falsey
  (valid-triangle? (make Point 0 0) (make Point 0 3) (make Point 3 0)) => truthy)

(load-file "solutions/pieces/add-and-make-5c.clj")

(fact
  (valid-2d-figure? (make Point 0 0) (make Point 0 1)) => truthy
  (valid-2d-figure? (make Point 0 0) (make Point 0 0) (make Point 3 4)) => falsey
  (valid-2d-figure? (make Point 0 0) (make Point 0 3) (make Point 3 0)) => truthy)
