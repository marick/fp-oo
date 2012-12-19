(ns solutions.ts-triangle
  (:use midje.sweet))

(load-file "sources/add-and-make.clj")
(load-file "solutions/add-and-make.clj")
(load-file "sources/triangle.clj")

(load-file "solutions/pieces/triangle-1a.clj")

(fact 
  (valid-triangle? (make Point 0 0) (make Point 0 0) (make Point 3 4)) => falsey
  (valid-triangle? (make Point 0 0) (make Point 0 3) (make Point 3 0)) => truthy)

(load-file "solutions/pieces/triangle-1b.clj")
(fact
  (valid-triangle? (make Point 0 0) (make Point 0 1)) => falsey
  (valid-triangle? (make Point 0 0) (make Point 0 0) (make Point 3 4)) => falsey
  (valid-triangle? (make Point 0 0) (make Point 0 3) (make Point 3 0)) => truthy
  (valid-triangle? (make Point 0 0) (make Point 0 3) (make Point 3 0) (make Point 3 3)) => falsey)


(load-file "solutions/pieces/triangle-2.clj")

(fact
  (valid-triangle? (make Point 0 0) (make Point 0 1)) => falsey
  (valid-triangle? (make Point 0 0) (make Point 0 0) (make Point 3 4)) => falsey
  (valid-triangle? (make Point 0 0) (make Point 0 3) (make Point 3 0)) => truthy
  (valid-triangle? (make Point 0 0) (make Point 0 3) (make Point 3 0) (make Point 3 3)) => falsey

  (valid-triangle? (make Point 0 0) (make Point 0 3) (make Point 0 5)) => falsey
  (valid-triangle? (make Point 0 0) (make Point 3 0) (make Point 5 0)) => falsey)


(load-file "solutions/pieces/triangle-3.clj")

(fact
  (equal-triangles? right-triangle equal-right-triangle) => truthy
  (equal-triangles? right-triangle different-triangle) => falsey)

(load-file "solutions/pieces/triangle-4.clj")


(fact
  (equal-triangles? right-triangle equal-right-triangle) => truthy
  (equal-triangles? right-triangle different-triangle) => falsey
  (equal-triangles? right-triangle equal-right-triangle different-triangle) => falsey)

