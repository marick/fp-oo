;;; Exercise 1

(def recurser-core
     (fn [stop-when ending-value combiner reducer]
       (letfn [(recursive-function [something]
                (if (stop-when something)
                  (ending-value something)
                  (combiner something
                            (recursive-function (reducer something)))))]
         recursive-function)))

