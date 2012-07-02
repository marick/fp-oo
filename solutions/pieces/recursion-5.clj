;;; Exercise 5

(prn (recursive-function (fn [elt so-far]
                           (assoc so-far elt 0))
                         [:a :b :c]
                         {}))

(prn (recursive-function (fn [elt so-far]
                           (assoc so-far elt (count so-far)))
                         [:a :b :c]
                         {}))

