(use 'clojure.algo.monads)


(use '[clojure.pprint :only [cl-format]])

(def verbose-charging-monad-alternate-inc
     (monad [m-result 
             (fn [result]
               (cl-format true "Freezing ~A.~%" result)
               (fn [charge]
                 (cl-format true "Unfrozen calculation gets charge ~A.~%" charge)
                 (cl-format true "... The frozen calculation result was ~A.~%" result)
                 {:charge (inc charge), :result result}))       ;; <<== change

             m-bind
             (fn [monadic-value continuation]
               (cl-format true "Making a decision.~%")
               (fn [charge]
                 (let [enclosed-map (monadic-value charge)
                       binding-value (:result enclosed-map)]
                   (cl-format true "Calling continuation with ~A~%" binding-value)
                   (cl-format true "... The charge to increment is ~A~%", charge)
                   ( (continuation binding-value) charge))))]))                        ;; <<== change

(println "==========")
(println "Defining run-and-charge.")
         
(def run-and-charge-and-speak
     (with-monad verbose-charging-monad-alternate-inc
       (let [frozen-step m-result]
         (domonad [a (frozen-step 8)
                   b (frozen-step (+ a 88))]
                  (+ a b)))))

(println "-----------")
(println "Running run-and-charge.")

