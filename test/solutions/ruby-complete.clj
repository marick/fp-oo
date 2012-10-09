
(fact "Anything"
  (let [instance (send-to Anything :new)]
    (send-to instance :class-name ) => 'Anything
    (send-to instance :class) => Anything)
  (send-to Anything :class-name) => 'Klass
  (send-to Anything :class) => Klass
  (send-to Anything :ancestors) => '[Anything]
  (lineage 'Anything) => '[Anything])

(fact "Module to Klass comparison "
  (send-to Module :ancestors) => '[Module Anything]
  (send-to Klass :ancestors) => '[Klass Module Anything]

  (send-to Klass :class-name) => 'Klass
  (send-to Module :class-name) => 'Klass

  (send-to Klass :to-string) => "class Klass"
  (send-to Module :to-string) => "class Module")


(def <=>
     (fn [a-number another-number]
       (max -1 (min 1 (compare a-number another-number)))))

(fact "comparison"
  (<=> 10 200) => -1
  (<=> 200 200) => 0
  (<=> 2000 200) => 1)



(send-to Klass :new
         'Trilobite 'Anything
         {
          :add-instance-values
          (fn [facets]
            (assoc this :facets facets))

          :facets (fn [] (:facets this))

          :<=>
          (fn [that]
            (<=> (send-to this :facets)
                 (send-to that :facets)))
         } 
         
         {
         })

;; These are global so that we can see that adding modules
;; affects existing instances.
(def cyclops (send-to Trilobite :new 1))
(def panopty (send-to Trilobite :new 1000))

(fact "Trilobites"
  (send-to Trilobite :ancestors) => '[Trilobite Anything]

  (send-to cyclops :class-name) => 'Trilobite
  (send-to panopty :class) => Trilobite
    
  (send-to cyclops :facets) => 1
  (send-to cyclops :<=> panopty) => -1
  (send-to cyclops :<=> cyclops) => 0
  (send-to panopty :<=> cyclops) => 1

  (lineage 'Trilobite) => '[Anything Trilobite])

(send-to Module :new 'Komparable
         {:=  (fn [that] (zero? (send-to this :<=> that)))
          :>  (fn [that] (= 1 (send-to this :<=> that)))
          :>= (fn [that] (or (send-to this := that)
                                  (send-to this :> that)))

          :<  (fn [that] (send-to that :> this))
          :<= (fn [that] (send-to that :>= this))

          :between?
          (fn [lower upper]
            (and (send-to this :>= lower)
                 (send-to this :<= upper)))})

(fact "creating a module"
  (send-to Komparable :class-name) => 'Module
  (:__own_symbol__ Komparable) => 'Komparable
  (:__up_symbol__ Komparable) => nil
  (:between? (:__methods__ Komparable)) =not=> nil)



(send-to Trilobite :include Komparable)

(fact 
  (send-to cyclops :<=> panopty) => -1
  (send-to cyclops :> panopty) => falsey
  (send-to cyclops :>= panopty) => falsey
  (send-to cyclops := panopty) => falsey
  (send-to cyclops :<= panopty) => truthy
  (send-to cyclops :< panopty) => truthy

  (send-to cyclops :<=> cyclops) => 0
  (send-to cyclops :> cyclops) => falsey
  (send-to cyclops :>= cyclops) => truthy
  (send-to cyclops := cyclops) => truthy
  (send-to cyclops :<= cyclops) => truthy
  (send-to cyclops :< cyclops) => falsey

  (send-to cyclops :between? cyclops cyclops) => truthy
  (send-to cyclops :between? cyclops panopty) => truthy
  (send-to cyclops :between? panopty cyclops) => falsey
  (send-to cyclops :between? panopty panopty) => falsey)

;; 

(send-to Module :new 'Cuddlesome
         {:purr (fn [] "puuuurrrrrrr")})
(send-to Trilobite :include Cuddlesome)

(send-to Module :new 'Squamous
         {:rattle (fn [] "chinka-chinka-chinka")})
(send-to Cuddlesome :include Squamous)

(fact "about multiple inclusion"
  (send-to cyclops :facets) => 1
  (send-to cyclops :> cyclops) => falsey
  (send-to cyclops :purr) => "puuuurrrrrrr"
  (send-to cyclops :rattle) => "chinka-chinka-chinka")


;;; More about inclusion and lineages



(send-to Klass :new
         'C 'Anything
         {}
         {})

  
(send-to Module :new 'T1
         {:common (fn [] "common in t1")
          :t1 (fn [] "t1")})

(send-to Module :new 'T2
         {:common (fn [] "common in t2")
          :t2 (fn [] "t2")})


(fact "lineages when modules included into classes"
  (send-to C :include T1)
  (lineage 'C) => '[Anything T1 C]
  (send-to C :include T2)
  (lineage 'C) => '[Anything T1 T2 C])

(send-to Module :new 'T1-1
         {:common (fn [] "common in t1-1")
          :t1-1 (fn [] "t1-1")})

(send-to Module :new 'T1-2
         {:common (fn [] "common in t1-2")
          :t1-2 (fn [] "t1-2")})

(fact "lineages when modules included into modules"
  (send-to T1 :include T1-1)
  (lineage 'C) => '[Anything T1-1 T1 T2 C]
  (send-to T1 :include T1-2)
  (lineage 'C) => '[Anything T1-1 T1-2 T1 T2 C])


(fact "inheritance works"
  (send-to (send-to C :new) :t1) => "t1"
  (send-to (send-to C :new) :common) => "common in t2")

(fact "functions that depend on the lineage work"
  (send-to C :ancestors) => '[C T2 T1 T1-2 T1-1 Anything]
  (send-to (send-to C :new) :class-name) => 'C)



;;; send-super

(send-to Klass :new
         'ExaggeratingPoint 'Point
         {
          :shift
          (fn [xinc yinc]
            (send-super (* 100 xinc) (* 100 yinc)))
         }
         {})

(send-to Klass :new
                'SuperDuperExaggeratingPoint 'ExaggeratingPoint
                {
                 :shift
                 (fn [xinc yinc]
                   (send-super (* 1234 xinc) (* 1234 yinc)))
                 }
                {})


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

(fact "repeat-to-super can follow itself"
  (let [o (send-to Three :new 1)]
    (send-to o :reveal) => [1 1 1]))



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

(fact "repeat-to-super can follow send-super"
  (let [o (send-to C :new 1)]
    (send-to o :reveal) => [2 2 1]))



(send-to Klass :new
         'NoSuper 'Anything
         {
          :with-explicit-args
          (fn [x] (send-super 3))

          :without-explicit-args
          (fn [x] (repeat-to-super))
          }
         {})


(fact "method missing behavior"
  (let [object (send-to NoSuper :new)]
    (send-to object :with-explicit-args 1)
    => (throws Error "No superclass method `:with-explicit-args` above `NoSuper`.")
    (send-to object :without-explicit-args 1)
    => (throws Error "No superclass method `:without-explicit-args` above `NoSuper`.")
    (send-to object :no-such-message)
    => (throws Error "A NoSuper does not accept the message :no-such-message.")))
