;;; Exercise 1

(def factorial
     (fn [n]
       (if (or (= n 0)
               (= n 1))
         1
         (* n (factorial (dec n))))))
(prn (factorial 0))
(prn (factorial 1))
(prn (factorial 5))

