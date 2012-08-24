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
     (fn [zipper] (znode (last (:parents zipper)))))


     
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



;;; Exercise 4

(def zreplace
     (fn [zipper subtree]
       (assoc zipper
         :here subtree)))


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
         
;;; Exercise 6

;; Step 1
(def znext zright)



;; Step 2

(def zbranch? (comp seq? znode))

;; Here's a solution using `cond`:
(def znext
     (fn [zipper]
       (cond (and (zbranch? zipper)
                  (not (nil? (zdown zipper))))
             (zdown zipper)

             :else
             (zright zipper))))

;; However, having the second `cond` form repeat the last clause of the preceding test is
;; a hint that we should maybe take advantage of the way that boolean operations return
;; the last value evaluated and turn the whole thing into an `or`:

(def znext
     (fn [zipper]
       (or (and (zbranch? zipper)
                (zdown zipper))
           (zright zipper))))

