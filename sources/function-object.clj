(use '[clojure.pprint :only [cl-format]])

(def Point
     (fn [xval yval]
       (letfn [(x [] xval)
               (y [] yval)
               (shift [xinc yinc] (Point (+ xval xinc) (+ yval yinc)))
               (add [other] (shift (other :x) (other :y)))
               (tostring [] (cl-format nil "Point [~A, ~A]"))]
         (let [methods {:x x, :y y, :shift shift, :add add}]
           (fn [message & args]
             (apply (methods message) args))))))
