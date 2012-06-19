(defn tails [seq]
  (map drop
       (range (inc (count seq)))
       (replicate (inc (count seq)) seq)))

(defn prefix-of? [candidate seq]
  (= (take (count candidate) seq)
     candidate))

