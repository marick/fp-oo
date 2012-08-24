;;; Exercise 5

(def zreplace
     (fn [zipper subtree]
       (assoc zipper
         :here subtree
         :changed true)))


(def zup
     (fn [zipper]
       (let [unmodified (first (:parents zipper))]
         (if (not (:changed zipper))
           unmodified
           (assoc unmodified
               :here (concat (:lefts zipper) (list (:here zipper)) (:rights zipper))
               :changed true)))))

(def zroot
     (fn [zipper]
       (if (empty? (:parents zipper))
         (znode zipper)
         (zroot (zup zipper)))))
         
