(use 'clojure.algo.monads)

(def function-monad
     (monad [m-result
             (fn [binding-value]
               (fn [] binding-value))

             m-bind
             (fn [monadic-value monadic-continuation]
               (let [binding-value (monadic-value)]
                 (monadic-continuation binding-value)))]))

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
             (fn [monadic-value monadic-continuation]
               (fn [charge]
                 (let [enclosed-map (monadic-value charge)
                       binding-value (:result enclosed-map)]
                   ( (monadic-continuation binding-value)
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
             (fn [monadic-value monadic-continuation]
               (cl-format true "Making a decision.~%")
               (fn [charge]
                 (let [enclosed-map (monadic-value charge)
                       binding-value (:result enclosed-map)]
                   (cl-format true "Calling continuation with ~A~%" binding-value)
                   (cl-format true "... The charge to increment is ~A~%", charge)
                   ( (monadic-continuation binding-value)
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
             (fn [monadic-value monadic-continuation]
               (fn [state]
                 (let [enclosed-map (monadic-value state)
                       binding-value (:result enclosed-map)
                       new-state (:state enclosed-map)]
                   (  (monadic-continuation binding-value) new-state))))]))

(def get-state
     (fn []
       (fn [state]
         {:state state, :result state})))


(def assign-state
     (fn [new-state]
       (fn [state]
         {:state new-state, :result state})))


(def calculation-with-initial-state
     (with-monad state-monad
       (domonad [original-state (assign-state 88)
                 state (get-state)]
            (str "original state " original-state " was set to " state))))

 (def mixer
      (with-monad state-monad
        (let [frozen-step m-result]
          (domonad [original (get-state)
                    a (frozen-step (+ original 88))
                    b (frozen-step (* a 2))
                    _ (assign-state b)]
                   [original a b]))))
