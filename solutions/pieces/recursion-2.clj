;;; Exercise 2

(def factorial-1 
     (fn [n so-far]
       (if (or (= n 0)
               (= n 1))
         so-far
         (factorial-1 (dec n) (* n so-far)))))

(def factorial
     (fn [n] (factorial-1 n 1)))

(prn (factorial 0))
(prn (factorial 1))
(prn (factorial 5))

