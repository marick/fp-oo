;;; 1

(def mmap
     (fn [function sequence]
       (new clojure.lang.LazySeq
            (fn []
              (if (empty? sequence)
                nil
                (cons (function (first sequence))
                      (mmap function (rest sequence))))))))

;;; 2

(def ffilter
     (fn [predicate? sequence]
       (new clojure.lang.LazySeq
            (fn []
              (cond (empty? sequence)
                    nil

                    (predicate? (first sequence))
                    (cons (first sequence)
                          (ffilter predicate? (rest sequence)))

                    :else
                    (ffilter predicate? (rest sequence)))))))

;;; 3

(def eager-filter
     (fn [predicate? sequence]
       (cond (empty? sequence)
             nil

             (predicate? (first sequence))
             (cons (first sequence)
                   (eager-filter predicate? (rest sequence)))

             :else
             (eager-filter predicate? (rest sequence)))))
