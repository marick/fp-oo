(ns solutions.ts-modules
   (:use midje.sweet))

(load-file "sources/modules.clj")
(load-file "solutions/pieces/modules-1.clj")

(fact "Module to Klass comparison "
  (send-to Module :ancestors) => '[Module Anything]
  (send-to Klass :ancestors) => '[Klass Module Anything]

  (send-to Klass :class-name) => 'Klass
  (send-to Module :class-name) => 'Klass

  (send-to Klass :to-string) => "class Klass"
  (send-to Module :to-string) => "class Module"

  (let [Kuddlesome (send-to Module :new 'Kuddlesome)]
    Kuddlesome => {:__own_symbol__ 'Kuddlesome}
    (send-to Trilobite :include Kuddlesome) => "Module Kuddlesome will someday be included into Trilobite"))

(def Trilobite nil)

(send-to Klass :new
         'Trilobite 'Anything
         {
          :add-instance-values
          (fn [this facets]
            (assoc this :facets facets))

          :facets :facets

          :<=>
          (fn [this that]
            (<=> (send-to this :facets)
                 (send-to that :facets)))
         } 
         
         {
         })

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
  (send-to Trilobite :ancestors) => '[Trilobite Anything]

  (send-to cyclops :class-name) => 'Trilobite
  (send-to panopty :class) => Trilobite
    
  (send-to cyclops :facets) => 1
  (send-to cyclops :<=> panopty) => -1
  (send-to cyclops :<=> cyclops) => 0
  (send-to panopty :<=> cyclops) => 1)

(load-file "solutions/pieces/modules-2.clj")

(send-to Module :new 'Cuddlesome
         {:purr (fn [this] "puuuurrrrrrr")})

(fact "creating a module"
  (send-to Cuddlesome :class-name) => 'Module
  (:__own_symbol__ Cuddlesome) => 'Cuddlesome
  (:__up_symbol__ Cuddlesome) => nil
  (:purr (:__methods__ Cuddlesome)) =not=> nil)

(load-file "solutions/pieces/modules-3.clj")

(facts "about inclusion"
  (:__up_symbol__ Trilobite) => 'Anything
  (send-to Trilobite :include Cuddlesome)
  (let [stub (eval (:__up_symbol__ Trilobite))]
    (:__up_symbol__ stub) => 'Anything
    (:__left_symbol__ stub) => 'Cuddlesome))

(send-to Module :new 'Squamous
         {:rattle (fn [this] "chinka-chinka-chinka")})

(facts "about module-in-module inclusion"
  (:__up_symbol__ Cuddlesome) => nil
  (send-to Cuddlesome :include Squamous)
  (let [stub (eval (:__up_symbol__ Cuddlesome))]
    (:__up_symbol__ stub) => nil
    (:__left_symbol__ stub) => 'Squamous))

(fact "Modules have classes"
  (send-to Cuddlesome :class-name) => 'Module)

(load-file "solutions/pieces/modules-4.clj")

;; Because the solution adds a new flag to module stubs, need to reload
;; everything.


(send-to Klass :new
         'Trilobite 'Anything
         {
          :add-instance-values
          (fn [this facets]
            (assoc this :facets facets))

          :facets :facets

          :<=>
          (fn [this that]
            (<=> (send-to this :facets)
                 (send-to that :facets)))
         } 
         
         {
         })

  
(fact "base lineages"
  (lineage 'Anything) => '[Anything]
  (lineage 'Trilobite) => '[Anything Trilobite])

(send-to Module :new 'T1
         {:common (fn [this] "common in t1")
          :t1 (fn [this] "t1")})

(send-to Module :new 'T2
         {:common (fn [this] "common in t2")
          :t2 (fn [this] "t2")})

(fact "lineages when modules included into classes"
  (send-to Trilobite :include T1)
  (lineage 'Trilobite) => '[Anything T1 Trilobite]
  (send-to Trilobite :include T2)
  (lineage 'Trilobite) => '[Anything T1 T2 Trilobite])

(send-to Module :new 'T1-1
         {:common (fn [this] "common in t1-1")
          :t1-1 (fn [this] "t1-1")})

(send-to Module :new 'T1-2
         {:common (fn [this] "common in t1-2")
          :t1-2 (fn [this] "t1-2")})

(fact "lineages when modules included into modules"
  (send-to T1 :include T1-1)
  (lineage 'Trilobite) => '[Anything T1-1 T1 T2 Trilobite]
  (send-to T1 :include T1-2)
  (lineage 'Trilobite) => '[Anything T1-1 T1-2 T1 T2 Trilobite])


(fact "inheritance works"
  (send-to cyclops :t1) => "t1"
  (send-to cyclops :common) => "common in t2")

(fact "functions that depend on the lineage work"
  (send-to Trilobite :ancestors) => '[Trilobite T2 T1 T1-2 T1-1 Anything]
  (send-to cyclops :class-name) => 'Trilobite)

;; Remember that the whole point of this chapter is to show how Comparable works


(send-to Module :new 'Komparable
         {:=  (fn [this that] (zero? (send-to this :<=> that)))
          :>  (fn [this that] (= 1 (send-to this :<=> that)))
          :>= (fn [this that] (or (send-to this := that)
                                  (send-to this :> that)))

          :<  (fn [this that] (send-to that :> this))
          :<= (fn [this that] (send-to that :>= this))

          :between?
          (fn [this lower upper]
            (and (send-to this :>= lower)
                 (send-to this :<= upper)))})

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

