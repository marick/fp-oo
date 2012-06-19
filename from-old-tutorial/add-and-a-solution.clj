(def add
     (fn [this other]
       (Point (+ (:x this) (:x other))
              (+ (:y this) (:y other)))))

(def add
     (fn [this other]
       (shifted this (:x other) (:y other))))

(def a
     (fn [type arg1 arg2]  ;; Requires two arguments.
       (type arg1 arg2)))

(def a
     (fn [type & args]
       (apply type args)))

(def Triangle
     (fn [point1 point2 point3]
       (if (not (= 3
                   (count (set [point1 point2 point3]))))
         (throw (new Error (str "Not a triangle:" point1 point2 point3)))
         {:point1 point1, :point2 point2, :point3 point3})))
