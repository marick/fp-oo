;;; Exercise 4

(def lons
     (fn [value lector]
       (fn [index]
         (if (zero? index)
           value
           (lector (dec index))))))

