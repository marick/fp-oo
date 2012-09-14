
;;; Exercise 6

(def pattern-classification
     (fn [pattern & rest]
       (cond (symbol? pattern)
             ::symbol

             (sequence-containing? pattern '&)
             ::nested-with-rest

             (sequence-containing? pattern :in)
             ::choice

             (sequence-containing? pattern :when)    ;; <<==
             ::guard

             (sequential? pattern)
             ::nested

             :else
             ::literal)))


(defspecialized match-one? ::guard
  [pattern form]
  (truthy? ( (eval (value-after-keyword pattern :when)) form)))

;; Since both ::choice and ::guard forms treat :bind keywords the
;; same, we decide they have a common supertype.

(derive ::guard ::named)
(derive ::choice ::named)

(defspecialized match-map ::named
  [pattern form]
  (if (sequence-containing? pattern :bind)
    { (value-after-keyword pattern :bind) form}
    {}))
 

