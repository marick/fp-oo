;; Taken from http://www.fatvat.co.uk/2009/04/understanding-y-combinator.html
;; Thanks to Jeff Foster

;; The mysterious and brain-shattering Y combinator!
(defn Y [r]
  ((fn [f] (f f))
   (fn [f]
     (r (fn [x] ((f f) x))))))

;; Defining factorial with Y
(Y (fn [factorial]
      (fn [n]
        (if (or (= 0 n) (= 1 n))
          n
          (* n (factorial (dec n)))))))


