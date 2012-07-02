(ns solutions.t-superclass
  (:use midje.sweet))

(load-file "sources/superclass.clj")
(load-file "solutions/pieces/superclass-1.clj")

(fact
  (send-to (a Anything) :to-string) => "{:__class_symbol__ Anything}"
  ;; Note: following too will fail if ordering of keys ever changes.
  (send-super (a Point 1 2) :to-string) => "{:y 2, :x 1, :__class_symbol__ Point}"
  (send-to (a Point 1 2) :to-string) => "A point like this: {:y 2, :x 1, :__class_symbol__ Point}"
  (send-to (a Point 1 2) :shift 1 2) => (a Point 2 4))

;; This makes available a more convenient string-formatting and printing function
(use '[clojure.pprint :only [cl-format]])

(def Super
{
  :__own_symbol__ 'Super
  :__instance_methods__
  {
   :add-instance-values
   (fn [this val]
     (cl-format true ">>>> SUPERCLASS constructor to add on ~A to ~A.~%" val this)
     (let [retval (assoc this :super val)]
       (cl-format true "<<<< SUPERCLASS constructor returns ~A.~%" retval)
       retval))


   :super-val
   (fn [this]
     (cl-format true ">>>>>> SUPERCLASS accessor :super-val applied to ~A.~%" this)
     (let [retval (:super this)]
       (cl-format true "<<<<<< SUPERCLASS accessor :super-val returns ~A.~%" retval)
       retval))

   :summer
   (fn [this val]
     (cl-format true ">>>> SUPERCLASS :summer of ~A to add ~A.~%" this val)
     (let [retval (+ val (send-to this :super-val))]
       (cl-format true "<<<< SUPERCLASS :summer returns ~A.~%" retval)
       retval))
  }
 })

(def Sub
{
  :__own_symbol__ 'Sub
  :__superclass_symbol__ 'Super
  :__instance_methods__
  {
   :add-instance-values
   (fn [this x y]
     (cl-format true ">> subclass constructor given ~A and ~A.~%" x y)
     (let [retval (assoc (send-super this :add-instance-values x) :sub y)]
       (cl-format true "<< subclass constructor returns ~A.~%" retval)
       retval))

   :sub-val
   (fn [this]
     (cl-format true ">>>> subclass accessor :sub-val applied to ~A.~%" this)
     (let [retval (:sub this)]
       (cl-format true "<<<< subclass accessor :sub-val returns ~A.~%" retval)
       retval))

   :summer
   (fn [this val]
     (cl-format true ">> subclass :summer of ~A to add ~A.~%" this val)
     (let [retval (+ (send-to this :sub-val)
                     (send-super this :summer val))]
     (cl-format true "<< subclass :summer returns A.~%" retval)
     retval))
   }
 })

(fact
  (let [sub (a Sub 1 2)]
    (send-to sub :super-val) => 1
    (send-to sub :sub-val) => 2
    (send-to sub :summer 3) => 6))

(load-file "solutions/pieces/superclass-2.clj")

(fact
  (apply-message-to Point (a Point 1 2) :class-name '()) => 'Point
  (apply-message-to Point (a Point 1 2) :shift '(1 2)) => (a Point 2 4))

(fact
  (send-to (a Point 1 2) :add (a Point 2 1)) => (a Point 3 3)
  (send-to (a Point 1 2) :class) => Point
  (send-to (a Anything) :class) => Anything)


(fact
  (send-to (a MissingOverrider) :queen-bee "Dawn") => nil
  (send-to (a SuperSender) :overrides-nothing) => (throws Error))
