(ns solutions.ts-build-zipper
  (:use midje.sweet))

(load-file "solutions/pieces/build-zipper-1.clj")

(fact
  (-> '(a b c) seq-zip zdown znode) => 'a
  (-> '( (+ 1 2) 3 4) seq-zip zdown znode) => '(+ 1 2)
  (-> '( (+ 1 2) 3 4) seq-zip zdown zdown znode) => '+)

(load-file "solutions/pieces/build-zipper-2.clj")

(fact
  (-> '(a b c) seq-zip zdown znode) => 'a
  (-> '( (+ 1 2) 3 4) seq-zip zdown znode) => '(+ 1 2)
  (-> '( (+ 1 2) 3 4) seq-zip zdown zdown znode) => '+
  (-> '(a b c) seq-zip znode) => '(a b c)
  (-> '() seq-zip zdown) => nil
  (-> '(a b c) seq-zip zdown zup znode) => '(a b c)
  (-> '(a b c) seq-zip zup) => nil
  (-> '() seq-zip zroot) => '()
  (-> '(a) seq-zip zroot) => '(a)
  (-> '(((a)) b c) seq-zip zdown zdown zdown zroot) => '(((a)) b c))

(load-file "solutions/pieces/build-zipper-3.clj")

(fact
  (-> '(a b c) seq-zip zdown znode) => 'a
  (-> '( (+ 1 2) 3 4) seq-zip zdown znode) => '(+ 1 2)
  (-> '( (+ 1 2) 3 4) seq-zip zdown zdown znode) => '+
  (-> '(a b c) seq-zip znode) => '(a b c)
  (-> '(a b c) seq-zip zdown zup znode) => '(a b c)
  (-> '(a b c) seq-zip zup) => nil
  (-> '() seq-zip zdown) => nil
  (-> '() seq-zip zroot) => '()
  (-> '(a) seq-zip zroot) => '(a)
  (-> '(((a)) b c) seq-zip zdown zdown zdown zroot) => '(((a)) b c)
  (-> (seq-zip '(a b c)) zdown zright znode) => 'b
  (-> (seq-zip '(a b c)) zdown zup znode) => '(a b c)
  (-> (seq-zip '(a b c)) zdown zright zright zleft znode) => 'b
  (-> (seq-zip '(a b c)) zdown zleft) => nil
  (-> (seq-zip '(a b c)) zdown zright zright zright) => nil)
  

(load-file "solutions/pieces/build-zipper-4.clj")

(fact
  (-> '(a b c) seq-zip zdown znode) => 'a
  (-> '( (+ 1 2) 3 4) seq-zip zdown znode) => '(+ 1 2)
  (-> '( (+ 1 2) 3 4) seq-zip zdown zdown znode) => '+
  (-> '(a b c) seq-zip znode) => '(a b c)
  (-> '(a b c) seq-zip zdown zup znode) => '(a b c)
  (-> '(a b c) seq-zip zup) => nil
  (-> '() seq-zip zroot) => '()
  (-> '(a) seq-zip zroot) => '(a)
  (-> '(((a)) b c) seq-zip zdown zdown zdown zroot) => '(((a)) b c)
  (-> '() seq-zip zdown) => nil
  (-> (seq-zip '(a b c)) zdown zright znode) => 'b
  (-> (seq-zip '(a b c)) zdown zup znode) => '(a b c)
  (-> (seq-zip '(a b c)) zdown zright zright zleft znode) => 'b
  (-> (seq-zip '(a b c)) zdown zleft) => nil
  (-> (seq-zip '(a b c)) zdown zright zright zright) => nil
  (-> (seq-zip '(a b c)) zdown zright (zreplace 3) znode) => 3
  (-> (seq-zip '(a b c)) zdown zright (zreplace 3) zright zleft znode) => 3
  (-> (seq-zip '(a b c)) zdown zright (zreplace 3) zleft zright zright znode) => 'c)

(load-file "solutions/pieces/build-zipper-5.clj")

(fact
  (-> '(a b c) seq-zip zdown znode) => 'a
  (-> '( (+ 1 2) 3 4) seq-zip zdown znode) => '(+ 1 2)
  (-> '( (+ 1 2) 3 4) seq-zip zdown zdown znode) => '+
  (-> '() seq-zip zdown) => nil
  (-> '(a b c) seq-zip znode) => '(a b c)
  (-> '(a b c) seq-zip zdown zup znode) => '(a b c)
  (-> '(a b c) seq-zip zup) => nil
  (-> '() seq-zip zroot) => '()
  (-> '(a) seq-zip zroot) => '(a)
  (-> '(((a)) b c) seq-zip zdown zdown zdown zroot) => '(((a)) b c)
  (-> (seq-zip '(a b c)) zdown zright znode) => 'b
  (-> (seq-zip '(a b c)) zdown zup znode) => '(a b c)
  (-> (seq-zip '(a b c)) zdown zright zright zleft znode) => 'b
  (-> (seq-zip '(a b c)) zdown zleft) => nil
  (-> (seq-zip '(a b c)) zdown zright zright zright) => nil
  (-> (seq-zip '(a)) zdown (zreplace 3) zup zup) => nil
  (-> (seq-zip '(a b c)) zdown zright (zreplace 3) znode) => 3
  (-> (seq-zip '(a b c)) zdown zright (zreplace 3) zright zleft znode) => 3
  (-> (seq-zip '(a b c)) zdown zright (zreplace 3) zleft zright zright znode) => 'c
  (-> (seq-zip '(a b c)) zdown zright (zreplace 3) zup znode) => '(a 3 c)
  (-> (seq-zip '(a b c)) zdown zright (zreplace 3) zright (zreplace 4) zup znode) => '(a 3 4)
  (-> (seq-zip '(a (b) c)) zdown zright zdown (zreplace 3) zroot) => '(a (3) c)
  (-> (seq-zip '(a (b) c)) zdown zright zdown (zreplace 3) zup zright (zreplace 4) zroot) => '(a (3) 4))

(load-file "solutions/pieces/build-zipper-6a.clj")

(fact
  (-> (seq-zip '(a b)) zdown znext znode) => 'b
  (-> (seq-zip '(a ((b)))) zdown znext znode) => '((b)))
  

(load-file "solutions/pieces/build-zipper-6b.clj")

(fact
  (-> (seq-zip '(a b)) zdown znext znode) => 'b
  (-> (seq-zip '(a ((b)))) zdown znext znode) => '((b))
  (-> (seq-zip '(a b)) znext znode) => 'a
  (-> (seq-zip '(() b)) znext znext znode) => 'b
  (-> (seq-zip '(((a)))) znext znode) => '((a))
  (-> (seq-zip '(((a)))) znext znext znode) => '(a))

  

(load-file "solutions/pieces/build-zipper-6c.clj")

(fact
  (-> (seq-zip '(a b)) zdown znext znode) => 'b
  (-> (seq-zip '(a ((b)))) zdown znext znode) => '((b))
  (-> (seq-zip '(a b)) znext znode) => 'a
  (-> (seq-zip '(() b)) znext znext znode) => 'b
  (-> (seq-zip '(((a)))) znext znode) => '((a))
  (-> (seq-zip '(((a)))) znext znext znode) => '(a)
  (-> (seq-zip '((a) b)) zdown zdown znext znode) => 'b
  (-> (seq-zip '(((a)) b)) zdown zdown zdown znext znode)  => 'b)

  

(load-file "solutions/pieces/build-zipper-6d.clj")

(fact
  (-> (seq-zip '(a b)) zdown znext znode) => 'b
  (-> (seq-zip '(a ((b)))) zdown znext znode) => '((b))
  (-> (seq-zip '(a b)) znext znode) => 'a
  (-> (seq-zip '(() b)) znext znext znode) => 'b
  (-> (seq-zip '(((a)))) znext znode) => '((a))
  (-> (seq-zip '(((a)))) znext znext znode) => '(a)
  (-> (seq-zip '((a) b)) zdown zdown znext znode) => 'b
  (-> (seq-zip '(((a)) b)) zdown zdown zdown znext znode)  => 'b
  (-> (seq-zip '((a) b)) znext znext znext znode) => 'b
  (-> (seq-zip '((a) b)) znext znext znext znext zend?) => truthy
  (-> (seq-zip '()) znext zend?) => truthy
  (-> (seq-zip '(a)) znext znext zend?) => truthy
  (-> (seq-zip '()) zend?) => falsey
  (-> (seq-zip '()) znext zend?) => truthy
  (let [z (-> (seq-zip '(a)) znext (zreplace 5) znext)]
    (zend? z) => truthy
    (zroot z) => '(5)))

(fact "old stuff still works"
  (-> '(a b c) seq-zip zdown znode) => 'a
  (-> '( (+ 1 2) 3 4) seq-zip zdown znode) => '(+ 1 2)
  (-> '( (+ 1 2) 3 4) seq-zip zdown zdown znode) => '+
  (-> '() seq-zip zdown) => nil
  (-> '(a b c) seq-zip znode) => '(a b c)
  (-> '(a b c) seq-zip zdown zup znode) => '(a b c)
  (-> '(a b c) seq-zip zup) => nil
  (-> '() seq-zip zroot) => '()
  (-> '(a) seq-zip zroot) => '(a)
  (-> '(((a)) b c) seq-zip zdown zdown zdown zroot) => '(((a)) b c)
  (-> (seq-zip '(a b c)) zdown zright znode) => 'b
  (-> (seq-zip '(a b c)) zdown zup znode) => '(a b c)
  (-> (seq-zip '(a b c)) zdown zright zright zleft znode) => 'b
  (-> (seq-zip '(a b c)) zdown zleft) => nil
  (-> (seq-zip '(a b c)) zdown zright zright zright) => nil
  (-> (seq-zip '(a)) zdown (zreplace 3) zup zup) => nil
  (-> (seq-zip '(a b c)) zdown zright (zreplace 3) znode) => 3
  (-> (seq-zip '(a b c)) zdown zright (zreplace 3) zright zleft znode) => 3
  (-> (seq-zip '(a b c)) zdown zright (zreplace 3) zleft zright zright znode) => 'c
  (-> (seq-zip '(a b c)) zdown zright (zreplace 3) zup znode) => '(a 3 c)
  (-> (seq-zip '(a b c)) zdown zright (zreplace 3) zright (zreplace 4) zup znode) => '(a 3 4)
  (-> (seq-zip '(a (b) c)) zdown zright zdown (zreplace 3) zroot) => '(a (3) c)
  (-> (seq-zip '(a (b) c)) zdown zright zdown (zreplace 3) zup zright (zreplace 4) zroot) => '(a (3) 4))




;;; Rerun tests from earlier in the chapter, using our homemade zippers.


(def all-vectors
     (fn [tree]
       (letfn [(helper [so-far zipper]
                 (cond (zend? zipper)
                       so-far
                       
                       (zbranch? zipper)
                       (helper so-far (znext zipper))

                       (vector? (znode zipper))
                       (helper (cons (znode zipper) so-far)
                               (znext zipper))

                       :else
                       (helper so-far (znext zipper))))]
         (reverse (helper '() (seq-zip tree))))))

(def first-vector
     (fn [tree]
       (letfn [(helper [zipper]
                  (cond (zend? zipper)
                        nil
                       
                       (vector? (znode zipper))
                       (znode zipper)
                       
                       :else
                       (helper (znext zipper))))]
         (helper (seq-zip tree)))))



(fact
  (all-vectors '(fn [a b] (concat [a] [b]))) => '([a b] [a] [b])
  (first-vector '(fn [a b] (concat [a] [b]))) => '[a b]
  (first-vector '(+ 1 (* 3 4))) => nil)


(def at?
     (fn [zipper & subtrees]
       (not (empty? (filter (partial = (znode zipper)) subtrees)))))

(def above?
     (fn [zipper subtree]
       (and (zbranch? zipper)
            (at? (zdown zipper) subtree))))


(def tumult
     (fn [form]
       (letfn [(advancing [flow] (-> (flow) znext do-node))
               (redo [flow] (-> (flow) do-node))                ;; <<== 
               (do-node [zipper]
                        (cond (zend? zipper)
                              zipper
                              
                              (at? zipper '+)
                              (advancing (fn [] (zreplace zipper 'PLUS)))

                              ;; After replacing the *, we need to back up so that
                              ;; the - can be processed.
                              (above? zipper '*)
                              (redo (fn [] (zreplace zipper '(- 1 2))))  ;; <<==
                              
                              :else
                              (advancing (constantly zipper))))]
         (-> form seq-zip do-node zroot))))

(fact
  (tumult '(- ( (+ 3)))) => '(- ( (PLUS 3)))
  (tumult '(+ (- 3 4 (- 5 (+ 6))))) => '(PLUS (- 3 4 (- 5 (PLUS 6))))
  (tumult '(* 1 2)) => '(- 1 2))
