;;; Exercise 3

(def recursive-function
     (fn [something so-far]
       (if (empty? something)
         so-far
         (recursive-function (rest something)
                             (+ (first something) so-far)))))

(prn (recursive-function [1 2 3 4] 0))

