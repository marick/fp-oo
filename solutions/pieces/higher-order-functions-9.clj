
;;; 9

(def check-sum
     (fn [sequence]
       (apply + (map (fn [position digit]
                       (* digit (if (odd? position) 1 3)))
                     (range 1 (inc (count sequence)))
                     sequence))))


(def upc?
     (fn [candidate]
       (zero? (rem (check-sum (reversed-digits candidate)) 10))))

