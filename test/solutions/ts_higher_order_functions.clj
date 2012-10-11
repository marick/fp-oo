(ns solutions.ts-higher-order-functions
  (:use midje.sweet))

(load-file "sources/class-object.clj")
(load-file "solutions/class-object.clj")


(load-file "solutions/pieces/higher-order-functions-1.clj")

;;; 1

(fact
  variant1 => [3 4 5]
  variant2 => [3 4 5])

;;; 2

(fact (separate odd? [1 2 3 4 5]) => [ [1 3 5] [2 4]])

;;; 3 

(def myfun-from-book
     (let [x 3]
       (fn [] x)))

(fact (myfun-from-book) => 3)

;;; 4

(fact (myfun) => 3)

;;; 5

(fact ( (always 1) 8 8 8) => 1)

(load-file "sources/higher-order-functions.clj")
(load-file "solutions/pieces/higher-order-functions-2.clj")

(fact 
  (isbn? "0131774115") => truthy
  (isbn? "0977716614") => falsey
  (isbn? "1934356190") => truthy)

(load-file "solutions/pieces/higher-order-functions-3.clj")

(fact
  (upc? "074182265830") => truthy
  (upc? "731124100023") => truthy
  (upc? "722252601404") => falsey)

(load-file "solutions/pieces/higher-order-functions-4.clj")

(fact 
  (isbn? "0131774115") => truthy
  (isbn? "0977716614") => falsey
  (isbn? "1934356190") => truthy
  (upc? "074182265830") => truthy
  (upc? "731124100023") => truthy
  (upc? "722252601404") => falsey)

