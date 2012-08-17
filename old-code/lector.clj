(def lector
     (fn []
       (fn [arg]
         (if (= arg :count)
           0
           (throw (new IndexOutOfBoundsException))))))

(def lons
     (fn [value lector]
       (fn [arg]
         (cond (= arg :count)
               (inc (lector :count))

               (zero? arg)
               value

               :else
               (lector (dec arg))))))
           

(def lector
     (fn []
       (fn [arg & more-args]
         (cond (= arg :count)
               0

               (= arg :empty?)     ; <<== New
               true                ; <<== New

               :else
               (throw (new IndexOutOfBoundsException))))))

(def lons
     (fn [value lector]
       (fn [arg]
         (cond (= arg :count)
               (inc (lector :count))

               (= arg :empty?)     ; <<== New
               false               ; <<== New

               (zero? arg)
               value

               :else
               (lector (dec arg))))))
           

