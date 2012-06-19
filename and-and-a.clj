(def point {:x 1, :y 2})

(def Point
     (fn [x y]
       {:x x, :y y}))

(def shifted
     (fn [this xinc yinc]
       (Point (+ (:x this) xinc)
              (+ (:y this) yinc))))
