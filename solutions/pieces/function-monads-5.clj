
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
     
