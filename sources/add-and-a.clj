(def point {:x 1, :y 2})

(def Point
     (fn [x y]
       {:x x,
        :y y
        :class 'Point}))

(def shifted
     (fn [this xinc yinc]
       (Point (+ (:x this) xinc)
              (+ (:y this) yinc))))

(def Triangle
     (fn [point1 point2 point3]
       {:point1 point1, :point2 point2, :point3 point3
        :class 'Triangle}))


(def right-triangle (Triangle (Point 0 0)
                              (Point 0 1)
                              (Point 1 0)))

(def equal-right-triangle (Triangle (Point 0 0)
                                    (Point 0 1)
                                    (Point 1 0)))

(def different-triangle (Triangle (Point 0 0)
                                  (Point 0 10)
                                  (Point 10 0)))


