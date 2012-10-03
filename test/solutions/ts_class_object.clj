(ns solutions.ts-class-object
  (:use midje.sweet))

(load-file "sources/class-object.clj")
(load-file "solutions/pieces/class-object-1.clj")

;;; Exercise 1

(def object (send-to Anything :new))
     
(fact "instance methods on Anything"
  (send-to object :shift) => (throws Error)
  (send-to object :to-string) => "{:__class_symbol__ Anything}"
  (send-to object :class-name) => 'Anything
  (send-to object :class) => Anything)

(def point (send-to Point :new 1 2))

;; As before
(fact "instance methods on Point"
  (send-to point :shift 1 2) => (send-to Point :new 2 4)
  (send-to point :add point) => (send-to Point :new 2 4)
  (send-to point :x) => 1
  (send-to point :y) => 2
  (send-to point :to-string) => "A Point like this: [1, 2]"
  (send-to point :class-name) => 'Point
  (send-to point :class) => Point)

(fact "class methods for Point"
  (send-to Point :origin) => (send-to Point :new 0 0))

(fact "method missing works for Points"
  (send-to (send-to Point :origin) :dumb-down 1 2 3) => (throws Error))


;;; Check the printing version in the source
(prn "method-missing check")
(send-to (send-to MissingOverrider :new) :mumble 1 2 3)

(def MetaOverrider
{
  :__own_symbol__ 'MetaOverrider
  :__superclass_symbol__ 'MetaAnything
  :__instance_methods__
  {
  }
 })


(def Overrider
{
  :__own_symbol__ 'Overrider
  :__class_symbol__ 'MetaOverrider
  :__superclass_symbol__ 'Anything
  :__instance_methods__
  {
   :method-missing
   (fn [this message args]
     [this message args])
  }
 })



(fact "method-missing can be overridden"
   (let [overrider (send-to Overrider :new)]
     (prn overrider)
     (send-to overrider :mumble 1 2) => [overrider :mumble [1 2]]))
    

;; Exercise 2

(fact "class methods for Point"
  (send-to Point :origin) => (send-to Point :new 0 0)
  (send-to Point :to-string) => (contains ":__class_symbol__ MetaPoint"))

(fact "method-missing works on classes"
  (send-to Point :unknown) => (throws Error)
  (send-to Anything :to-string) => (contains "__own_symbol__ Anything")
  (send-to Anything :class) => MetaAnything
  (send-to Anything :class-name) => 'MetaAnything)
  

(load-file "solutions/pieces/class-object-2.clj")

(fact "method-missing works on classes"
  (send-to MetaPoint :unknown) => (throws Error)
  (send-to MetaPoint :to-string) => (contains "__own_symbol__ MetaPoint")
  (send-to MetaPoint :class) => Anything
  (send-to MetaAnything :class-name) => 'Anything

  (send-to MetaAnything :to-string) => (contains "__own_symbol__ MetaAnything")
  (send-to MetaAnything :class) => Anything
  (send-to MetaAnything :class-name) => 'Anything)
