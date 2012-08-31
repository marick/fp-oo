;;; Exercise 5

;; This is an extended version of `at` that can match more than one subtree.
(def at?
     (fn [zipper & subtrees]
       (not (empty? (filter (partial = (zip/node zipper)) subtrees)))))

(def transform
     (fn [form]
       (letfn [(advancing [flow]
                          (-> (flow) zip/next do-node))
               (do-node [zipper]
                        (cond (zip/end? zipper)
                              zipper
                        
                              (at? zipper 'fact 'facts)
                              (advancing (fn [] (zip/replace zipper 'do)))

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

