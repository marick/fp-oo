(use 'clojure.algo.monads)

(def function-monad
     (monad [m-result
             (fn [binding-value]
               (fn [] binding-value))

             m-bind
             (fn [monadic-value continuation]
               (let [binding-value (monadic-value)]
                 (continuation binding-value)))]))

(def calculation
     (with-monad function-monad
       (let [frozen-step m-result]
         (domonad [a (frozen-step 8)
                   b (frozen-step (+ a 88))]
              (+ a b)))))


;;;; Charging monad

(def charging-monad
     (monad [m-result 
             (fn [result]
               (fn [charge]
                 {:charge charge, :result result}))

             m-bind
             (fn [monadic-value continuation]
               (fn [charge]
                 (let [enclosed-map (monadic-value charge)
                       binding-value (:result enclosed-map)]
                   ( (continuation binding-value)
                     (inc charge)))))]))

(def run-and-charge
     (with-monad charging-monad
       (let [frozen-step m-result]
         (domonad [a (frozen-step 8)
                   b (frozen-step (+ a 88))]
                  (+ a b)))))




(use '[clojure.pprint :only [cl-format]])

(def verbose-charging-monad
     (monad [m-result 
             (fn [result]
               (cl-format true "Freezing ~A.~%" result)
               (fn [charge]
                 (cl-format true "Unfrozen calculation gets charge ~A.~%" charge)
                 (cl-format true "... The frozen calculation result was ~A.~%" result)
                 {:charge charge, :result result}))

             m-bind
             (fn [monadic-value continuation]
               (cl-format true "Making a decision.~%")
               (fn [charge]
                 (let [enclosed-map (monadic-value charge)
                       binding-value (:result enclosed-map)]
                   (cl-format true "Calling continuation with ~A~%" binding-value)
                   (cl-format true "... The charge to increment is ~A~%", charge)
                   ( (continuation binding-value)
                     (inc charge)))))]))

(println "==========")
(println "Defining run-and-charge.")
         
(def run-and-charge-and-speak
     (with-monad verbose-charging-monad
       (let [frozen-step m-result]
         (domonad [a (frozen-step 8)
                   b (frozen-step (+ a 88))]
                  (+ a b)))))

(println "-----------")
(println "Running run-and-charge.")

;;; State monad

(def state-monad
     (monad [m-result 
             (fn [result]
               (fn [state]
                 {:state state, :result result}))

             m-bind
             (fn [monadic-value continuation]
               (fn [state]
                 (let [enclosed-map (monadic-value state)
                       binding-value (:result enclosed-map)
                       new-state (:state enclosed-map)]
                   (  (continuation binding-value) new-state))))]))

(def get-state
     (fn []
       (fn [state]
         {:state state, :result state})))


(def assign-state
     (fn [new-state]
       (fn [state]
         {:state new-state, :result state})))
