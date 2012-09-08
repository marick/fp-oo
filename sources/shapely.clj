(load-file "sources/generic.clj")

(def truthy?
     (fn [val] (not (not val))))

(def pattern-classification
     (fn [pattern & rest]
       (cond 

             :else
             ::literal)))
             
(defgeneric match-one? #'pattern-classification)

(defspecialized match-one? ::literal
  [pattern form]
  ;; ...
  )

(defgeneric match-map #'pattern-classification)


(defspecialized match-map ::literal
  [pattern form]
  ;; ...
  )
