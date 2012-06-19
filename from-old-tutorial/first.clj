;; user> (tails '(1 2 3 4))
;; ((1 2 3 4) (2 3 4) (3 4) (4) ())

(defn tails [seq]
  (map drop
       (range (inc (count seq)))
       (replicate (inc (count seq)) seq)))
;; user> (prefix-of? '(1 3) [1 2 3])
;; true
;; user> (prefix-of? '(1 3) [1 2 3])
;; false

(defn prefix-of? [candidate seq]
  (= (take (count candidate) seq)
     candidate))


