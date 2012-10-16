(ns solutions.ts-send-super
  (:use midje.sweet))

(load-file "sources/dynamic.clj")
(load-file "solutions/pieces/send-super-1.clj")

(fact
  (find-containing-holder-symbol 'Point :shift) => 'Point
  (find-containing-holder-symbol 'Point :to-string) => 'Point
  (find-containing-holder-symbol 'Point :class-name) => 'Anything
  (find-containing-holder-symbol 'Point :nonsense) => nil)

(fact
  (let [point (send-to Point :new 1 2)]
    (send-to point :class-name) => 'Point
    (send-to point :x) => 1
    (send-to point :shift 100 200) => (send-to Point :new 101 202)))

(load-file "solutions/pieces/send-super-2.clj")

(send-to Klass :new
         'DynamicPoint 'Point
         {
          :shift
          (fn [xinc yinc]
            [current-message holder-of-current-method current-arguments])
         }
         {})

(fact
  (let [point (send-to DynamicPoint :new 1 2)]
    (send-to point :class-name) => 'DynamicPoint
    (send-to point :shift 1 2) => [:shift 'DynamicPoint [1 2]]))
        
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
  (let [object (send-to NoSuper :new)]
    (send-to object :with-explicit-args 1)
    => (throws Error "No superclass method `:with-explicit-args` above `NoSuper`.")
    (send-to object :without-explicit-args 1)
    => (throws Error "No superclass method `:without-explicit-args` above `NoSuper`.")
    (send-to object :no-such-message)
    => (throws Error "A NoSuper does not accept the message :no-such-message.")))


;; `repeat-to-super` works both within a normal method call and
;; within a method that was reached with repeat-to-super.

(send-to Klass :new
                'One 'Anything
                {
                 :add-instance-values
                 (fn [value] (assoc this :one value))
                }
                {})

(send-to Klass :new
                'Two 'One
                {
                 :add-instance-values
                 (fn [value]
                   (assoc (repeat-to-super) :two value))
                }
                {})

(send-to Klass :new
                'Three 'Two
                {
                 :add-instance-values
                 (fn [value]
                   (assoc (repeat-to-super) :three value))

                 :reveal
                 (fn []
                   [(:one this) (:two this) (:three this)])
                }
                {})


(fact
  (let [o (send-to Three :new 1)]
    (send-to o :reveal) => [1 1 1]))



;;; This set of classes checks that `repeat-to-super` works
;;; after a `send-super`.

(send-to Klass :new
                'A 'Anything
                {
                 :add-instance-values
                 (fn [value] (assoc this :a value))
                }
                {})

(send-to Klass :new
                'B 'A
                {
                 :add-instance-values
                 (fn [value]
                   (assoc (repeat-to-super) :b value))
                }
                {})

(send-to Klass :new
                'C 'B
                {
                 :add-instance-values
                 (fn [value]
                   (assoc (send-super (* 2 value)) :c value))

                 :reveal
                 (fn []
                   [(:a this) (:b this) (:c this)])
                }
                {})

(fact
  (let [o (send-to C :new 1)]
    (send-to o :reveal) => [2 2 1]))



(load-file "sources/send-super-exercises.clj")
;; Send-super tests from exercises file

(fact
  (let [mild-braggart (send-to ExaggeratingPoint :new 1 2)
        mild-braggart-claim (send-to mild-braggart :shift 1 2)
        big-braggart (send-to SuperDuperExaggeratingPoint :new 1 2)
        big-braggart-claim (send-to big-braggart :shift 1 2)]
    (send-to mild-braggart-claim :x) => 101
    (send-to mild-braggart-claim :y) => 202

    (send-to big-braggart-claim :x) => 123401
    (send-to big-braggart-claim :y) => 246802))


(def object (send-to Lowest :new))
(fact 
  (send-to object :super-exists 1 2 3) => "Got these args: (1 2 3)")
