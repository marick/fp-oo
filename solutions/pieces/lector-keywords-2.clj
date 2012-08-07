(def lons
     (fn [value lector]
       (fn [arg]
         (cond (= arg :count)
               (inc (lector :count))

               (zero? arg)
               value

               :else
               (lector (dec arg))))))
           
