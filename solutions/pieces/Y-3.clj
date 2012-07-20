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


