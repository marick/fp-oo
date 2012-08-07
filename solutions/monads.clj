(use 'clojure.algo.monads)

(def monadifier
     (fn [step-value]
       (list step-value)))

(def decider
     (fn [step-value continuation]
       (cons step-value
             (continuation step-value))))

(defmonad logging-monad
  [m-result list
   m-bind (fn [value continuation]
            (cons step-value
                  (continuation step-value)))]

(defmonad sequence-monad
  [m-result list
   m-bind (fn [value continuation]
            (map continuation value))])
  
     
(with-monad logging-monad
  (domonad [a 1
            b (inc a)
            c (* b (inc a))]
     (+ a b c)))


