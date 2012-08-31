;;; Exercise 5

(def zreplace
     (fn [zipper subtree]
       (assoc zipper
         :here subtree
         :changed true)))


(def zup
     (fn [zipper]
       (let [unmodified (first (:parents zipper))]
         (cond (nil? unmodified)
               nil

               (not (:changed zipper))
               unmodified 

               :else
               (assoc unmodified
                 :here (concat (:lefts zipper) (list (:here zipper)) (:rights zipper))
                 :changed true)))))

;; The earlier version of zroot already works. Here it is again:
(def zroot
     (fn [zipper]
       (if (empty? (:parents zipper))
         (znode zipper)
         (zroot (zup zipper)))))
         


