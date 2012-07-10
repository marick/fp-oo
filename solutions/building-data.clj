;;; Exercise 1

(def fmerge
     (fn [fap map]
       (reduce (fn [so-far key]
                 (fassoc so-far key (key map)))
               fap
               (keys map))))
                 
;;; Exercise 2

(def fassoc-base
     (fn [fap new-key value]
       (fn [lookup-key]
         (if (= lookup-key new-key)
           value
           (fap lookup-key))))
)

(def fmerge
     (fn [fap map]
       (reduce (fn [so-far key]
                 (fassoc-base so-far key (key map)))
               fap
               (keys map))))

(def fassoc
     (fn [fap & pairs]
       (fmerge fap (apply hash-map pairs))))

       
