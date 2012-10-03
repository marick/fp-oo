(ns sources.t-dynamic
   (:use midje.sweet))

(load-file "sources/dynamic.clj")


(fact "Anything"
  (let [instance (send-to Anything :new)]
    (send-to instance :class-name ) => 'Anything

    (send-to instance :class) => Anything)
  (send-to Anything :class-name) => 'Klass
  (send-to Anything :class) => Klass
  (send-to Anything :ancestors) => '[Anything])

(fact "comparison"
  (<=> 10 200) => -1
  (<=> 200 200) => 0
  (<=> 2000 200) => 1)

;; These are global so that we can see that adding modules
;; affects existing instances.
(def cyclops (send-to Trilobite :new 1))
(def panopty (send-to Trilobite :new 1000))


(fact "Trilobites"
  (send-to Trilobite :ancestors) => '[Trilobite Komparable Anything]

  (send-to cyclops :class-name) => 'Trilobite
  (send-to panopty :class) => Trilobite
    
  (send-to cyclops :facets) => 1
  (send-to cyclops :<=> panopty) => -1
  (send-to cyclops :<=> cyclops) => 0
  (send-to panopty :<=> cyclops) => 1)

(send-to Module :new 'Cuddlesome
         {:purr (fn [] "puuuurrrrrrr")})

(send-to Module :new 'Squamous
         {:rattle (fn [] "chinka-chinka-chinka")})

(send-to Cuddlesome :include Squamous)
(send-to Trilobite :include Cuddlesome)

(send-to Trilobite :ancestors) => '[Trilobite Squamous Cuddlesome Komparable Anything]
(send-to cyclops :purr) => "puuuurrrrrrr"
(send-to cyclops :rattle) => "chinka-chinka-chinka"

(fact "Modules have classes"
  (send-to Cuddlesome :class-name) => 'Module)


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





(facts "about Points"
  (send-to Point :origin) => (send-to Point :new 0 0)
  (let [point (send-to Point :new 1 2)]
    (send-to point :x) => 1
    (send-to point :y) => 2
    (send-to point :to-string) => "A Point like this: [1, 2]"
    (send-to point :shift 100 200) => (send-to Point :new 101 202)
    (send-to point :add (send-to point :shift 98 196)) => (send-to Point :new 100 200)))
  
