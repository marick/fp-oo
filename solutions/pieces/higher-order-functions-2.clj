
;;; 7

(def check-sum
     (fn [sequence]
       (apply + (map *
                     (range 1 (inc (count sequence)))
                     sequence))))

;;; 8

(def isbn?
     (fn [candidate]
       (zero? (rem (check-sum (reversed-digits candidate)) 11))))

