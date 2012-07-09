(ns solutions.t-higher-order-functions
  (:use midje.sweet))

(load-file "sources/without-klass.clj")
(load-file "solutions/without-klass.clj")


(load-file "solutions/higher-order-functions.clj")

;;; 1

(fact
  variant1 => [3 4 5]
  variant2 => [3 4 5])

;;; 2 

(def myfun-from-book
     (let [x 3]
       (fn [] x)))

(fact (myfun-from-book) => 3)

;;; 3

(fact (myfun) => 3)

;;; 4

(def point (send-to Point :new 1 2))
(def a (send-to Anything :new))

(fact
  (send-to a :to-string) => "{:__class_symbol__ Anything}"
  (send-to Anything :to-string) => (contains ":__class_symbol__ MetaAnything")
  (send-to point :to-string) => "A Point like this: {:y 2, :x 1, :__class_symbol__ Point}"
  (send-to Point :to-string) => (contains ":__class_symbol__ MetaPoint"))


;;; 6

(fact ( (always 1) 8 8 8) => 1)
