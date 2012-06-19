(def Rectangle
     (fn [origin width height]
       (let [functions {:origin
                        (fn [] origin)

                        :area
                        (fn [] (* height width))

                        :top-right
                        (fn []
                          (Point (+ (origin :x) width)
                                 (+ (origin :y) height)))}]
         (fn [keyword & args]
           (apply (keyword functions) args)))))
         
