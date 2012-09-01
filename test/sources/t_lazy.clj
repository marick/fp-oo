(ns sources.t-lazy
  (:use midje.sweet))

(load-file "sources/lazy.clj")

(fact
  (rrange 0 1) => [0]
  (rrange 0 10) => [0 1 2 3 4 5 6 7 8 9]
  (type (rrange 0 10)) => clojure.lang.LazySeq
  (type (rest (rrange 0 10))) => clojure.lang.LazySeq
  (first (rrange 1 10)) => 1)

(fact
  filter =not=> eager?
  rrange =not=> eager?)
