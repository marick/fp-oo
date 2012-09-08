
;; Exercise 3

(def pattern-classification
     (fn [pattern & rest]
       (cond (symbol? pattern)
             ::symbol

             (sequential? pattern)
             ::nested

             :else
             ::literal)))

(defspecialized match-one? ::nested
  [pattern form]
  (and
   (sequential? form)
   (= (count pattern) (count form))
   ;; I could use `true?`, because I expect recursive calls to
   ;; obey the predicate convention (only `true` or `false`),
   ;; but I'll play it safe.
   (every? truthy? (map match-one? pattern form))))

(defspecialized match-map ::nested
  [pattern form]
  (apply merge {} (map match-map pattern form)))


