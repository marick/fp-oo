;;; This version of `names-ordered-by` is for exercise 2.

(defgeneric names-ordered-by
  [callable] callable)

(defspecialized names-ordered-by :default
  [callable]
  (fn [things]
    (map :name (sort-by callable things))))

