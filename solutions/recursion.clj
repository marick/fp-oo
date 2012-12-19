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

;;; Exercise 3

(def recursive-function
     (fn [something so-far]
       (if (empty? something)
         so-far
         (recursive-function (rest something)
                             (+ (first something) so-far)))))

(prn (recursive-function [1 2 3 4] 0))

;;; Exercise 4

(def recursive-function
     (fn [something so-far]
       (if (empty? something)
         so-far
         (recursive-function (rest something)
                             (* (first something) so-far)))))

(prn (recursive-function [1 2 3 4] 1))

(def recursive-function
     (fn [combiner something so-far]
       (if (empty? something)
         so-far
         (recursive-function combiner
                             (rest something)
                             (combiner (first something)
                                       so-far)))))

(prn (recursive-function * [1 2 3 4] 1))
(prn (recursive-function + [1 2 3 4] 0))

;;; Exercise 5

(prn (recursive-function (fn [elt so-far]
                           (assoc so-far elt 0))
                         [:a :b :c]
                         {}))

(prn (recursive-function (fn [elt so-far]
                           (assoc so-far elt (count so-far)))
                         [:a :b :c]
                         {}))

;;; Exercise 6

; I can't help you pat yourself on the back.
(reduce * 1 [1 2 3 4])
24
(reduce (fn [so-far val] (assoc so-far val (count so-far)))
        {}
        [:a :b :c])
{:c 0, :b 0, :a 0}
(reduce (fn [so-far val] (assoc so-far val (count so-far)))
        {}
        [:a :b :c])
{:c 2, :b 1, :a 0}
