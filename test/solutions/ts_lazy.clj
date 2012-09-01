(ns solutions.ts-lazy
  (:use midje.sweet))

(load-file "sources/lazy.clj")
(load-file "solutions/lazy.clj")

(fact
  (mmap inc (rrange 0 10)) => [1 2 3 4 5 6 7 8 9 10])

(fact
  (ffilter even? (rrange 0 10)) => [0 2 4 6 8])

(fact
  (eager-filter even? (rrange 0 10)) => [0 2 4 6 8])

(fact
  ffilter =not=> eager?
  eager-filter => eager?)
