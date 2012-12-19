(ns solutions.ts-add-and-make
  (:use midje.sweet))

(load-file "sources/add-and-make.clj")
(load-file "solutions/pieces/add-and-make-1a.clj")

(fact 
  (add (Point 1 2) (Point 3 5)) => (Point 4 7))

(load-file "solutions/pieces/add-and-make-1b.clj")

(fact 
  (add (Point 1 2) (Point 3 5)) => (Point 4 7))

(load-file "solutions/pieces/add-and-make-2.clj")
(fact
  (make Point 1 2) => (Point 1 2))

