;;; Exercise 1

(def fmerge
     (fn [fap map]
       (reduce (fn [so-far key]
                 (fassoc so-far key (key map)))
               fap
               (keys map))))
                 
