;;; Exercise 1

(def seq-zip
     (fn [tree] tree))

(def zdown
     (fn [zipper]
       (first zipper)))

(def znode
     (fn [zipper]
       zipper))

(-> '(a b c) seq-zip zdown znode)



