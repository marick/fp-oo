(ns solutions.t-building-data
  (:use midje.sweet))

(load-file "sources/building-data.clj")
(load-file "solutions/pieces/building-data-1.clj")

(fact
  (let [start (fassoc (fap) :a 1)
        merged (fmerge start {:a 'not-1 :b 2})]
    (merged :a) => 'not-1
    (merged :b) => 2
    (merged :c) => nil))
    
(load-file "solutions/pieces/building-data-2.clj")

(fact
  (let [start (fmerge (fap) {:a 1})
        assd (fassoc start :b 2 :c 3)]
    (assd :a) => 1
    (assd :b) => 2
    (assd :c) => 3))
  
