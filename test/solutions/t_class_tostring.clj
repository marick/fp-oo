(ns solutions.t-class-tostring
   (:use midje.sweet))

(load-file "sources/class-creation.clj")
(load-file "sources/ruby-classes.clj")
(load-file "solutions/class-tostring.clj")

(fact
  (send-to Point :to-string) => "class Point"
  (send-to Klass :to-string) => "class Klass"
  (send-to (send-to Anything :new) :to-string) => "{:__class_symbol__ Anything}")

