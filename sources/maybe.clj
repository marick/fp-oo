(use 'clojure.algo.monads)

(def decider
     (fn [value continuation]
       (if (nil? value)
         nil
         (continuation value))))

(def maybe-monad
     (monad [m-result identity
             m-bind   decider]))

(with-monad maybe-monad
  (domonad [a nil
            b (+ 1 a)] ; would blow up
     b))

;; Error utilities

(def oops!
     (fn [reason & args]
       (with-meta (merge {:reason reason}
                         (apply hash-map args))
                  {:type :error})))

(def oopsie?
     (fn [value]
       (= (type value) :error)))
