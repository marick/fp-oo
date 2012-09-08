;;; Exercise 2


(def pattern-classification
     (fn [pattern & rest]
       (cond (symbol? pattern)
             ::symbol

             :else
             ::literal)))

(defspecialized match-one? ::symbol
  [pattern form]
  true)

(defspecialized match-map ::symbol
  [pattern form]
  {pattern form})

