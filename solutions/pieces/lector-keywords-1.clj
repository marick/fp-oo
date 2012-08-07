

(def lector
     (fn []
       (fn [arg]
         (cond (= arg :count)
               0

               (= arg :first)
               nil

               (= arg :rest)
               nil
               
               :else
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
           
(def f
     (fn [a b]
       (with-local-vars [mapping {}]
         (fn [key]
           (if (= key :set)
             (var-set mapping
                      (assoc (var-get mapping) :a 1))
             (var-get mapping))))))


(def f
     (fn [a b]
       (with-local-vars [mapping {}]
         (prn mapping)
         (prn (var-get mapping))
         (fn []
           (prn mapping)
           (prn (var-get mapping))
           (var-get mapping)))))
