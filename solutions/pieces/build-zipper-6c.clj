
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

