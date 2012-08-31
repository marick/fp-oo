

;;; Exercise 4

;;; In the my first solution to this exercise, it produced `(- 1 2)`. I think the `-` should
;;; then be recursively handled by `tumult`, which would give us `(- 1 2 55555)`.

;;; For my solution, I decided to produce an alternative to `advancing`. Instead of
;;; calling for `do-node` to work on the next node, it re-applies it to the current node.

(def tumult
     (fn [form]
       (letfn [(advancing [flow] (-> (flow) zip/next do-node))
               (redo [flow] (-> (flow) do-node))                ;; <<== 
               (do-node [zipper]
                        (cond (zip/end? zipper)
                              zipper
                              
                              (at? zipper '+)
                              (advancing (fn [] (zip/replace zipper 'PLUS)))

                              (above? zipper '-)
                              (advancing (fn [] (zip/append-child zipper 55555)))

                              ;; After replacing the *, we need to back up so that
                              ;; the - can be processed.
                              (above? zipper '*)
                              (redo (fn [] (zip/replace zipper '(- 1 2))))  ;; <<==
                              
                              (at? zipper '/)
                              (advancing (fn []
                                           (-> zipper
                                               zip/right
                                               zip/remove
                                               zip/right
                                               zip/remove
                                               (zip/insert-right (-> zipper zip/right zip/node))
                                               (zip/insert-right (-> zipper zip/right zip/right zip/node))
                                               zip/next
                                               do-node)))
                              
                              :else
                              (advancing (constantly zipper))))]
         (-> form zip/seq-zip do-node zip/root))))


