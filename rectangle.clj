(def Point
     (fn [x y]
       (let [functions {:shifted
                        (fn [xinc yinc]
                          (Point (+ x xinc)
                                 (+ y yinc)))
                        :x (fn [] x)
                        :y (fn [] y)
                        :string (fn [] (str [x y]))}]
         (fn [keyword & args]
           (apply (keyword functions) args)))))
