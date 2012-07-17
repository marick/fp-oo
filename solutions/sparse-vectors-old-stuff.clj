;;; Exercise 1

(def fmerge
     (fn [fap map]
       (reduce (fn [so-far key]
                 (fassoc so-far key (key map)))
               fap
               (keys map))))
                 
;;; Exercise 2

;; In this solution, I start with an `fassoc-base` that
;; knows how to add on only one key/value pair. `fmerge`
;; uses that, along with `reduce`. The multi-pair `fassoc`
;; creates a map from its arguments and merges it.

(def fassoc-base
     (fn [fap new-key value]
       (fn [lookup-key]
         (if (= lookup-key new-key)
           value
           (fap lookup-key))))
)

(def fmerge
     (fn [fap map]
       (reduce (fn [so-far key]
                 (fassoc-base so-far key (key map)))
               fap
               (keys map))))

(def fassoc
     (fn [fap & pairs]
       (fmerge fap (apply hash-map pairs))))
;;; Exercise 2 (another version)

;; Rather than have `fassoc-base` be `def`ed itself, I'll define it in
;; a let. There's no need for it to be globally visible. It's also
;; interesting to note that the `let` can surround a `def`. (In Ruby,
;; a method definition can't see bindings established outside it, but
;; the lesser-used `define-method` can.)


(let [fassoc-base
      (fn [fap new-key value]
        (fn [lookup-key]
          (if (= lookup-key new-key)
            value
            (fap lookup-key))))]
  (def fassoc
       (fn [fap & key-values]
         (let [pairs (partition 2 key-values)]
           (reduce (fn [so-far pair]
                     (apply fassoc-base so-far pair))
                   fap
                   pairs)))))

(def fmerge
     (fn [fap to-add]
       (let [map-to-arguments (partial mapcat identity)]
         (apply fassoc fap (map-to-arguments to-add)))))


(def fist
     (fn []
       (fn [index]
         (throw (new IndexOutOfBoundsException)))))

(def consf
     (fn [value a-fist]
       (fn [index]
         (if (= index 0)
            value
            (a-fist (dec index))))))

(def index-value-rest
     (fn [index value rest]
       (consf index (consf value (consf rest (fist))))))

(def index-value-fectorfun index-value-rest)

(def fector
     (fn []
       (index-value-fectorfun -1 :no-value
                              (fn [] (throw (new IndexOutOfBoundsException))))))

(def fappend
     (fn [a-fector value]
       (index-value-fectorfun (inc (a-fector 0))
                              value
                              (constantly a-fector))))
        
(def fnth
     (fn [a-fector desired-index]
       (if (= (a-fector 0) desired-index)
         (a-fector 1)
         (fnth ((a-fector 2)) desired-index))))
       
====

* Make #'optional-fap take an argument that is a function to execute
when it is called. That function should take the key as an argument. Implement `fap` in terms of optional-fap.

* Define sparse-vector as a function of no arguments that ruturns a fap with :next-index 0 and a :fap as a fap that blows up with a index-out-of-bounds exception.

* Define `append fap value) to add a next index with 0.

* Define `value-of index `

* Define `fassoc`



(def sparse-vector
     (fn [] (consf 0 (fap))))




(fector [0 
