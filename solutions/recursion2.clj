(def my-flatten-1
     (fn [something so-far]
       (if (not (sequential? something))
         (cons something so-far)
         (apply concat (map (fn [elt] (my-flatten-1 elt []))
                            something)))))

(def my-flatten 
     (fn [something] (my-flatten-1 something [])))

(prn (my-flatten [  [1 [2 [3 4] 5] 6] ]))



;; OR

(def my-flatten-1
     (fn [something so-far]
       (if (not (sequential? something))
         (cons something so-far)
         (mapcat (fn [elt] (my-flatten-1 elt []))
                 something))))

(def my-flatten 
     (fn [something] (my-flatten-1 something [])))

(prn (my-flatten [  [1 [2 [3 4] 5] 6] ]))



