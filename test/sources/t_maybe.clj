(ns sources.t-maybe
  (:use midje.sweet))

(load-file "sources/maybe.clj")

(fact
  (domonad maybe-monad
         [a nil
          b (+ 1 a)] ; would blow up
   b) => nil)

(fact
  (let [exception (oops! "reason" :key :val :key2 :val2)]
    exception => {:reason "reason", :key :val, :key2, :val2}
    (meta exception) => {:type :error}
    (type exception) => :error
    (oopsie? exception) => truthy))
