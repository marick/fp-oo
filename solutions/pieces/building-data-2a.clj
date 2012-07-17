;;; Exercise 2

;; In this solution, I start with an `fassoc-base` that
;; knows how to add on only one key/value pair. `fmerge`
;; uses that, along with `reduce`. The multi-pair `fassoc`
;; creates a map from its arguments and merges it.

(def fassoc-base
     (fn [fap new-key value]
       (fn [lookup-key]
         (if (= lookup-key new-key)
           value
           (fap lookup-key)))))

(def fmerge
     (fn [fap map]
       (reduce (fn [so-far key]
                 (fassoc-base so-far key (key map)))
               fap
               (keys map))))

(def fassoc
     (fn [fap & pairs]
       (fmerge fap (apply hash-map pairs))))

