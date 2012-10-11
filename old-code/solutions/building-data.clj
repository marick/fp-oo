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
           (fap lookup-key)))))

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


;;; Exercise 3

(def lector
     (fn []
       (fn [index]
         (throw (new IndexOutOfBoundsException)))))
                 
;;; Exercise 4

(def lons
     (fn [value lector]
       (fn [index]
         (if (zero? index)
           value
           (lector (dec index))))))

