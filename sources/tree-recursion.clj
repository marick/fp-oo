(use '[clojure.pprint :only [cl-format]])

(def increment-tree
     (fn [tree]
       (if (sequential? tree)
         (map increment-tree tree)
         (inc tree))))

(def sum-tree
     (fn [tree]
       (if (sequential? tree)
         (apply + (map sum-tree tree))
         tree)))
                        

(def collecting-sum-tree
     (fn [so-far tree]
       (if (sequential? tree)
         (reduce collecting-sum-tree so-far tree)
         (+ so-far tree))))

(def flattenize
     (fn [so-far tree]
       (if (sequential? tree)
         (reduce flattenize so-far tree)
         (cons tree so-far))))

(def flattenize-1
     (fn [so-far tree]
       (if (sequential? tree)
         (reduce flattenize-1 so-far tree)
         (cons tree so-far))))

(def flattenize
     (fn [tree]
       (reverse (flattenize-1 '() tree))))



; height of a tree
