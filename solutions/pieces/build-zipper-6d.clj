
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

