(use 'clojure.algo.monads)

(def combined-monadifier list)

(def combined-decider
     (fn [monadic-value monadic-continuation]
       (let [maybe-ified-continuation
             (fn [binding-value]
               (if (nil? binding-value)
                 (combined-monadifier binding-value)
                 (monadic-continuation binding-value)))]
         (mapcat maybe-ified-continuation monadic-value))))

(defmonad combined-monad
  [m-result combined-monadifier
   m-bind combined-decider])

(prn 
 (with-monad combined-monad
   (domonad [a [1 nil 3]
             b [-1 1]]
            (* a b))))
