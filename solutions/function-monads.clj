(use 'clojure.algo.monads)
(use '[clojure.pprint :only [cl-format]])

;;; Exercise 3
(load-file "sources/function-monads.clj")

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

;;; Exercise 4

(def transform-state
     (fn [transformer]
       (fn [state]
         {:state (transformer state), :result state})))

(def transform-state-example
     (domonad [b (transform-state inc)]
         b))

(prn (transform-state-example 1))

;;; Exercise 5

(def get-state
     (fn [variable]
       (fn [state]
         {:state state, :result (variable state)})))


(def assign-state
     (fn [variable new-state]
       (fn [state]
         (let [old-var-val (variable state)]
           {:state (assoc state variable new-state), :result old-var-val}))))

(def transform-state
     (fn [variable transformer]
       (fn [state]
         (let [old-var-val (variable state)]
           {:state (assoc state variable (transformer old-var-val)), :result old-var-val}))))


(def map-state-example
     (with-monad state-monad
       (domonad [a (get-state :a)
                 old-b (assign-state :b 3)
                 old-c (transform-state :c inc)]
          [a old-b old-c])))
     
