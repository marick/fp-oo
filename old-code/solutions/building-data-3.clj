;;; Exercise 3

(def lector
     (fn []
       (fn [index]
         (throw (new IndexOutOfBoundsException)))))
                 
