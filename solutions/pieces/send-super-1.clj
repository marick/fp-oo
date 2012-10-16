(load-file "sources/dynamic.clj")

;;; Exercise 1

;; Some notes:
;; * The results of `lineage` need to be reversed because `lineage`
;;   returns `Anything` first, then subclasses in descending order.
;;   That was convenient for `method-cache`. Now that we're abandoning
;;   `method-cache`, we should probably change `lineage` to return its
;;   results in the other order, but I'm not doing it in this solution
;;   so as to minimize the number of code changes.
;; * The use of `(first (filter ...))` is a little tricky, as it relies
;;   on the fact that `first` of an empty sequence is `nil`.

(def find-containing-holder-symbol
     (fn [first-candidate message]
       (first (filter (fn [holder-symbol]
                        (message (held-methods holder-symbol)))
                      (reverse (lineage first-candidate))))))

(def apply-message-to
     (fn [method-holder instance message args]
       (let [target-holder (find-containing-holder-symbol (:__own_symbol__ method-holder)
                                                          message)]
         (if target-holder
           (binding [this instance]
             (apply (message (held-methods target-holder)) args))
           (send-to instance :method-missing message args)))))

(def point (send-to Point :new 1 2))
(prn (send-to point :class-name))
(prn (send-to point :x))
(prn (send-to point :shift 100 200))
