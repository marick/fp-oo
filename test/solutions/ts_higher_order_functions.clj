(ns solutions.ts-higher-order-functions
  (:use midje.sweet))

(load-file "sources/without-klass.clj")
(load-file "solutions/without-klass.clj")


(load-file "solutions/pieces/higher-order-functions-1-7.clj")

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

(def point (send-to Point :new 1 2))
(def a (send-to Anything :new))

(fact
  (send-to a :to-string) => "{:__class_symbol__ Anything}"
  (send-to Anything :to-string) => (contains ":__class_symbol__ MetaAnything")
  (send-to point :to-string) => "A Point like this: {:y 2, :x 1, :__class_symbol__ Point}"
  (send-to Point :to-string) => (contains ":__class_symbol__ MetaPoint"))


;;; 6

(fact ( (always 1) 8 8 8) => 1)

(load-file "sources/higher-order-functions.clj")
(load-file "solutions/pieces/higher-order-functions-8-9.clj")

(fact 
  (isbn? "0131774115") => truthy
  (isbn? "0977716614") => falsey
  (isbn? "1934356190") => truthy)

(load-file "solutions/pieces/higher-order-functions-a.clj")

(fact
  (upc? "074182265830") => truthy
  (upc? "731124100023") => truthy
  (upc? "722252601404") => falsey)

(load-file "solutions/pieces/higher-order-functions-b.clj")

(fact 
  (isbn? "0131774115") => truthy
  (isbn? "0977716614") => falsey
  (isbn? "1934356190") => truthy
  (upc? "074182265830") => truthy
  (upc? "731124100023") => truthy
  (upc? "722252601404") => falsey)

