(def rrange
     (fn [first past-end]
       (new clojure.lang.LazySeq
            (fn []
              (if (= first past-end)
                nil
                (cons first
                      (rrange (inc first) past-end)))))))
