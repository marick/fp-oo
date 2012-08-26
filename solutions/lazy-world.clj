;;; The solution to 1 is in the text

;;; 2

(def ys-and-ns
     (filter (fn [string]
               (or (.startsWith string "y")
                   (.startsWith string "n")))
             (repeatedly prompt-and-read)))

;;; 3

(def counted-sum
     (fn []
       (let [numbers-only
             (map to-integer
                  (filter number-string?
                          (repeatedly prompt-and-read)))
             number-count (first numbers-only)
             numbers (take number-count (rest numbers-only))]
         (apply + numbers))))
             
