(ns solutions.ts-building-data
  (:use midje.sweet))

(load-file "sources/building-data.clj")
(load-file "solutions/pieces/building-data-1.clj")

(fact
  (let [start (fassoc (fap) :a 1)
        merged (fmerge start {:a 'not-1 :b 2})]
    (merged :a) => 'not-1
    (merged :b) => 2
    (merged :c) => nil))
    
(load-file "solutions/pieces/building-data-2a.clj")

(fact
  (let [start (fmerge (fap) {:a 1})
        assd (fassoc start :b 2 :c 3)]
    (assd :a) => 1
    (assd :b) => 2
    (assd :c) => 3))
  
(load-file "solutions/pieces/building-data-2b.clj")

(fact
  (let [start (fmerge (fap) {:a 1})
        assd (fassoc start :b 2 :c 3)]
    (assd :a) => 1
    (assd :b) => 2
    (assd :c) => 3))
  
(load-file "solutions/pieces/building-data-3.clj")

(fact
  ( (lector) 0) => (throws IndexOutOfBoundsException))

(load-file "solutions/pieces/building-data-4.clj")

(fact
  ( (lons 'x (lector)) 0) => 'x
  (let [l (lons 0 (lons 1 (lector)))]
    (l 0) => 0
    (l 1) => 1
    (l 2) => (throws IndexOutOfBoundsException)))
        

