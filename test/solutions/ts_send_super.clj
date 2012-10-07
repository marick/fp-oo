(ns solutions.ts-send-super
  (:use midje.sweet))

(load-file "sources/dynamic.clj")
(load-file "solutions/pieces/send-super-1.clj")

(fact
  (find-containing-holder-symbol 'Point :shift) => 'Point
  (find-containing-holder-symbol 'Point :to-string) => 'Point
  (find-containing-holder-symbol 'Point :class-name) => 'Anything
  (find-containing-holder-symbol 'Point :nonsense) => nil)


(load-file "sources/send-super-exercises.clj")
(fact
  (let [point (send-to DynamicPoint :new 1 2)]
    (send-to point :x) => 1
    (send-to point :shift 1 2) => "Method found in DynamicPoint"))
        

(load-file "solutions/pieces/send-super-2.clj")
(send-to Klass :new
         'DynamicPoint 'Point
         {
          :shift
          (fn [xinc yinc]
            (str "Method " current-message " found in " holder-of-current-method))
         }

         {})

(fact
  (let [point (send-to DynamicPoint :new 1 2)]
    (send-to point :x) => 1
    (send-to point :shift 1 2) => "Method :shift found in DynamicPoint"))


(fact
  (let [mild-braggart (send-to ExaggeratingPoint :new 1 2)
        mild-braggart-claim (send-to mild-braggart :shift 1 2)
        big-braggart (send-to SuperDuperExaggeratingPoint :new 1 2)
        big-braggart-claim (send-to big-braggart :shift 1 2)]
    (send-to mild-braggart-claim :x) => 101
    (send-to mild-braggart-claim :y) => 202

    (send-to big-braggart-claim :x) => 123401
    (send-to big-braggart-claim :y) => 246802))


(send-to Klass :new
         'NoSuper 'Anything
         {
          :with-explicit-args
          (fn [x] (send-super 3))

          :without-explicit-args
          (fn [x] (repeat-to-super))
          }
         {})

(fact
  (send-to (send-to NoSuper :new) :with-explicit-args 1)
  => (throws Error "No superclass method `:with-explicit-args` above `NoSuper`."))


(load-file "solutions/pieces/send-super-3.clj")

(fact
  (let [o (send-to Three :new 1)]
    (send-to o :reveal) => [1 1 1]))

(fact
  (let [o (send-to C :new 1)]
    (send-to o :reveal) => [2 2 1]))

(fact
  (let [object (send-to NoSuper :new)]
    (send-to object :with-explicit-args 1)
    => (throws Error "No superclass method `:with-explicit-args` above `NoSuper`.")
    (send-to object :without-explicit-args 1)
    => (throws Error "No superclass method `:without-explicit-args` above `NoSuper`.")
    (send-to object :no-such-message)
    => (throws Error "A NoSuper does not accept the message :no-such-message.")))



