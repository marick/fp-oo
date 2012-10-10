(use 'clojure.algo.monads)

(def sequence-monad-decider
     (fn [step-value monadic-continuation]
       (mapcat monadic-continuation step-value)))

(def sequence-monad-monadifier list)

(def sequence-monad
     (monad [m-result sequence-monad-monadifier
             m-bind sequence-monad-decider]))

(prn
 (with-monad sequence-monad
   (domonad [a [1 2]
             b [10, 100]
             c [-1 1]]
            (* a b c))))

(prn 
 (with-monad sequence-monad
   (domonad [a (list 1 2 3)
             b (list (- a) a)
             c (list (+ a b) (- a b))]
            (* a b c))))
