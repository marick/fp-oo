(def Triangle
     (fn [& points]
       (set points)))


;; Triangles should work with `make`:

(prn (make Triangle (make Point 1 2)
                    (make Point 1 3)
                    (make Point 3 1)))


;; I'll leave off the excess verbiage of `make`:

(def right-triangle (Triangle (Point 0 0)
                              (Point 0 1)
                              (Point 1 0)))

(def equal-right-triangle (Triangle (Point 0 0)
                                    (Point 0 1)
                                    (Point 1 0)))

(def different-triangle (Triangle (Point 0 0)
                                  (Point 0 10)
                                  (Point 10 0)))

