;;; Exercise 1

(def recurser-core
     (fn [stop-when ending-value combiner reducer]
       (letfn [(recursive-function [something]
                (if (stop-when something)
                  (ending-value something)
                  (combiner something
                            (recursive-function (reducer something)))))]
         recursive-function)))

;;; Exercise 2

(def recurser-core
     (fn [stop-when ending-value combiner reducer]
       (Y (fn [recursive-function]
            (fn [something]
              (if (stop-when something)
                (ending-value something)
                (combiner something
                          (recursive-function (reducer something)))))))))

;;; Exercise 3

(def recurser
     (fn [& args]
       (let [control-map (apply hash-map args)
             ending-value (:ending-value control-map)
             ending-value (if (fn? ending-value)
                            ending-value
                            (constantly ending-value))]
         (recurser-core (:stop-when control-map)
                        ending-value
                        (:combiner control-map)
                        (:reducer control-map)))))


