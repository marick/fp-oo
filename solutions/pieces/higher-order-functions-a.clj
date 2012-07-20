
;;; 10

(def number-checker
     (fn [digit-function divisor]
       (fn [candidate]
         (let [digits (reversed-digits candidate)
               check-sum (apply +
                                (map digit-function
                                     (range 1 (inc (count digits)))
                                     digits))]
           (zero? (rem check-sum divisor))))))

(def isbn? (number-checker * 11))
(def upc? (number-checker
           (fn [position digit] (* digit (if (odd? position) 1 3)))
           10))
             
