(def add
     (fn [this other]
       (shift this (x other) (y other))))

(prn (add (Point 1 2) (Point 3 4)))

