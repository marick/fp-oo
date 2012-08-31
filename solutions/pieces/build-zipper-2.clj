;;; Exercise 2

(def seq-zip
     (fn [tree]
       {:here tree
        :parents '()}))

(def znode :here)

(def zdown
     (fn [zipper]
       (if (empty? (:here zipper))
         nil
         (assoc zipper 
           :here (first (:here zipper))
           :parents (cons zipper (:parents zipper))))))

(def zup
     (fn [zipper] (first (:parents zipper))));

(def zroot
     (fn [zipper]
       (if (empty? (:parents zipper))
         (znode zipper)
         (zroot (zup zipper)))))


     
