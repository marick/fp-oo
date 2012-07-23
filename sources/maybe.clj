(use 'clojure.algo.monads)

(def use-value-to-decide-what-to-do
     (fn [value continuation]
       (if (nil? value)
         nil
         (continuation value))))

(defmonad maybe-monad
  [m-result identity
   m-bind   use-value-to-decide-what-to-do])

(domonad maybe-monad
         [a nil
          b (+ 1 a)] ; would blow up
   b)

(def oops!
     (fn [reason & args]
       (with-meta (merge {:reason reason}
                         (apply hash-map args))
                  {:type :error})))

(def oopsie?
     (fn [value]
       (= (type value) :error)))
