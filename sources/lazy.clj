(def rrange
     (fn [first past-end]
       (new clojure.lang.LazySeq
            (fn []
              (if (= first past-end)
                nil
                (cons first
                      (rrange (inc first) past-end)))))))

;;; For exercise 3.

;;; This throws an exception if the last element of a sequence is
;;; evaluated when only the `first` is asked for. It uses a large
;;; source sequence because many builtin sequence functions precalculate
;;; more than one element when asked for the first. Therefore, if 
;;; the source sequence were something like [1 2], `filter` would count
;;; as eager. The number of elements precalculated is subject to change, but
;;; it's awfully unlikely to be 10,000.

(def eager?
     (fn [filter-function]
       (try
         (not (first (filter-function (fn [elt]
                                        (if (= elt 9999)
                                          (throw (Error.)))
                                        true)
                                      (range 10000))))
         (catch Error e
           true))))
