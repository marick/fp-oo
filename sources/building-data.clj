(def fap
     (fn []
       (fn [lookup-key] nil)))

(def fassoc
     (fn [fap new-key value]
       (fn [lookup-key]
         (if (= lookup-key new-key)
           value
           (fap lookup-key)))))

