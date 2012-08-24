;;; Exercise 3

(def seq-zip
     (fn [tree]
       {:here tree
        :parents '()
        :lefts '()
        :rights '()}))

(def zdown
     (fn [zipper]
       (if (empty? (:here zipper))
         nil
         (assoc zipper 
           :here (first (:here zipper))
           :lefts '()
           :rights (rest (:here zipper))
           :parents (cons zipper (:parents zipper))))))

(def zright
     (fn [zipper]
       (if (empty? (:rights zipper))
         nil
         (assoc zipper
           :here (first (:rights zipper))
           :lefts (concat (:lefts zipper) (list (:here zipper)))
           :rights (rest (:rights zipper))))))

(def zleft
     (fn [zipper]
       (if (empty? (:lefts zipper))
         nil
         (assoc zipper
           :here (last (:lefts zipper))
           :lefts (butlast (:lefts zipper))
           :rights (cons (last zipper) (:rights zipper))))))



