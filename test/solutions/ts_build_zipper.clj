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
  (-> '(((a)) b c) seq-zip zdown zdown zdown zroot) => '(((a)) b c)
  (-> (seq-zip '(a b c)) zdown zright znode) => 'b
  (-> (seq-zip '(a b c)) zdown zup znode) => '(a b c)
  (-> (seq-zip '(a b c)) zdown zright zright zleft znode) => 'b
  (-> (seq-zip '(a b c)) zdown zleft) => nil
  (-> (seq-zip '(a b c)) zdown zright zright zright) => nil
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
  (-> (seq-zip '(() b)) znext znext znode) => 'b)

