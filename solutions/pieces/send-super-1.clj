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

;;; Exercise 2 

(def ^:dynamic holder-of-current-method nil)

(def apply-message-to
     (fn [method-holder instance message args]
       (let [holder (find-containing-holder-symbol (:__own_symbol__ method-holder) message)]
         (if holder
           (binding [this instance
                     holder-of-current-method holder]
             (apply (message (held-methods holder)) args))
           (send-to instance :method-missing message args)))))


