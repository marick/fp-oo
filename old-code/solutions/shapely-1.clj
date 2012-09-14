(load-file "sources/generic.clj")
(load-file "sources/shapely.clj")

;;; Exercise 1

(defspecialized match-one? ::literal
  [pattern form]
  (= pattern form))

(defspecialized match-map ::literal
  [pattern form]
  {})


