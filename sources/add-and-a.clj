(def point {:x 1, :y 2})

(def Point
     (fn [x y]
       {:x x, :y y}))

(def shifted
     (fn [this xinc yinc]
       (Point (+ (:x this) xinc)
              (+ (:y this) yinc))))

(def Triangle
     (fn [point1 point2 point3]
       {:point1 point1, :point2 point2, :point3 point3}))
