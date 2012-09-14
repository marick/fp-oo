
;;; Exercise 5

(def pattern-classification
     (fn [pattern & rest]
       (cond (symbol? pattern)
             ::symbol

             (sequence-containing? pattern '&)
             ::nested-with-rest

             (sequence-containing? pattern :in)    ;; <<==
             ::choice

             (sequential? pattern)
             ::nested

             :else
             ::literal)))

(def value-after-keyword
     (fn [sequence keyword]
       (second (drop-while (complement (partial = keyword)) sequence))))

(defspecialized match-one? ::choice
  [pattern form]
  (sequence-containing? (value-after-keyword pattern :in) form))

(defspecialized match-map ::choice
  [pattern form]
  (if (sequence-containing? pattern :bind)
    { (value-after-keyword pattern :bind) form}
    {}))
 

