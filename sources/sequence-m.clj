(use 'clojure.algo.monads)

(def decider
     (fn [step-value continuation]
       (apply concat
              (map continuation step-value))))

(def monadifier list)

(def nested-loop-monad
     (monad [m-result monadifier
             m-bind decider ]))

(with-monad nested-loop-monad
  (domonad [a [1 2]
            b [10, 100]
            c [-1 1]]
     (* a b c)))


(with-monad nested-loop-monad
  (domonad [a (list 1 2 3)
            b (list (- a) a)
            c (list (+ a b) (- a b))]
     (* a b c)))
