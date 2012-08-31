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
     (fn [zipper]
       (if (empty? (:parents zipper))
         (znode zipper)
         (zroot (zup zipper)))))


     
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
       (assoc zipper :here subtree)))



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
         


;;; Exercise 6

;; Step 1
(def znext zright)



;; Step 2

(def zbranch? (comp seq? znode))

;; Here's a solution using `cond`:
(def znext
     (fn [zipper]
       (cond (and (zbranch? zipper)
                  (not (nil? (zdown zipper))))   ; (1)
             (zdown zipper)                      ; (2)

             :else
             (zright zipper))))

;; However, having line (2) repeat the `zdown` from line (1) is a hint
;; that we should maybe take advantage of the way that boolean
;; operations return the last value evaluated. That lets us turn the
;; whole `cond` into an `or`:

(def znext
     (fn [zipper]
       (or (and (zbranch? zipper)
                (zdown zipper))
           (zright zipper))))


;; Step 3


(def znext
     (fn [zipper]
       (letfn [(search-up [zipper]
                  (or (-> zipper zup zright)
                      (-> zipper zup search-up)))]
         (or (and (zbranch? zipper)
                  (zdown zipper))
             (zright zipper)
             (search-up zipper)))))


;; Step 4

(def zend? :end?)

(def znext
     (fn [zipper]
       (letfn [(search-up [zipper]
                  (if (nil? (zup zipper))
                    (assoc zipper :end? true)
                    (or (-> zipper zup zright)
                        (-> zipper zup search-up))))]
         (or (and (zbranch? zipper)
                  (zdown zipper))
             (zright zipper)
             (search-up zipper)))))

