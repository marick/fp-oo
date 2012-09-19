(ns sources.t-modules
   (:use midje.sweet))

(load-file "sources/modules.clj")


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

(fact "Trilobites"
  (send-to Trilobite :ancestors) => '[Trilobite Anything]
  
  (let [cyclops (send-to Trilobite :new 1)
        panopty (send-to Trilobite :new 1000)]
    (send-to cyclops :class-name) => 'Trilobite
    (send-to panopty :class) => Trilobite
    
    (send-to cyclops :facets) => 1
    (send-to cyclops :<=> panopty) => -1
    (send-to cyclops :<=> cyclops) => 0
    (send-to panopty :<=> cyclops) => 1))
    
