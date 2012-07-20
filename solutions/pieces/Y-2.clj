;;; Exercise 2

(def recurser-core
     (fn [stop-when ending-value combiner reducer]
       (Y (fn [recursive-function]
            (fn [something]
              (if (stop-when something)
                (ending-value something)
                (combiner something
                          (recursive-function (reducer something)))))))))

