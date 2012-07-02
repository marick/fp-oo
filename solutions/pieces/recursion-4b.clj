(def recursive-function
     (fn [combiner something so-far]
       (if (empty? something)
         so-far
         (recursive-function combiner
                             (rest something)
                             (combiner (first something)
                                       so-far)))))

(prn (recursive-function * [1 2 3 4] 1))
(prn (recursive-function + [1 2 3 4] 0))

