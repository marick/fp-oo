;;; Exercise 8

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
                              (advancing (fn [] (-> zipper zip/down skip-to-rightmost-leaf)))
                              
                              (above? zipper 'provided)
                              (advancing
                               (fn []
                                 (let [function-call-z (-> zipper zip/down zip/right)
                                       arrow-z (-> function-call-z zip/right)
                                       return-value-z (-> arrow-z zip/right)
                                       addition (list 'fake
                                                      (zip/node function-call-z)
                                                      (zip/node arrow-z)
                                                      (zip/node return-value-z))]
                                   (-> zipper
                                       zip/left
                                       (zip/append-child addition)
                                       zip/right
                                       zip/remove))))
                              
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

              
