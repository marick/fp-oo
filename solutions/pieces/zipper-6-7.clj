;;; Exercise 6

(def skip-to-rightmost-leaf
     (fn [zipper]
       (let [over (zip/rightmost zipper)]
         (if (or (not (zip/branch? over))
                 (empty? (zip/children over)))
           over
           (-> over
               zip/down
               skip-to-rightmost-leaf)))))



;;; Exercise 7

(def transform
     (fn [form]
       (letfn [(advancing [flow]
                          (-> (flow) zip/next do-node))
               (do-node [zipper]
                        (cond (zip/end? zipper)
                              zipper
                        
                              (at? zipper 'fact 'facts)
                              (advancing (fn [] (zip/replace zipper 'do)))

                              (above? zipper 'quote)
                              ;; The following could be written like this:
                              ;; (if (nil? (zip/right zipper))
                              ;;   zipper
                              ;;   (-> zipper zip/right do-node))
                              ;; ... instead of using `skip-to-rightmost-leaf`. 
                              ;; I prefer the consistency of having all the `cond`
                              ;; clauses use `advancing`. 
                              ;; In any case, in the real Midje code,
                              ;; skip-to-rightmost-leaf is used in other
                              ;; places.
                              (advancing (fn [] (-> zipper zip/down skip-to-rightmost-leaf)))
                              
                              (at? zipper '=>)
                              (advancing 
                               (fn []
                                 (let [replacement (list 'expect
                                                         (-> zipper zip/left zip/node)
                                                         (-> zipper zip/node)
                                                         (-> zipper zip/right zip/node))]
                                   (-> zipper
                                       zip/left
                                       (zip/replace replacement)
                                       zip/right
                                       zip/remove
                                       zip/next
                                       zip/remove))))
                              :else
                              (advancing (constantly zipper))))]
         (-> form zip/seq-zip do-node zip/root))))


