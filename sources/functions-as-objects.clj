(use '[clojure.pprint :only [cl-format]])

(declare Point)

(def Point
     (fn [x y]
       (fn [] (cl-format nil "Point [~A, ~A]" x y))))



(def fassoc-base
     (fn [fap new-key value]
       (fn [lookup-key]
         (if (= lookup-key new-key)
           value
           (fap lookup-key))))
)

(def fmerge
     (fn [fap map]
       (reduce (fn [so-far key]
                 (fassoc-base so-far key (key map)))
               fap
               (keys map))))

(def fassoc
     (fn [fap & pairs]
       (fmerge fap (apply hash-map pairs))))

(def Point
     (fn [x y]
       (let [public-methods
             (fassoc (fap)
               :to-string 
               (fn []
                 (cl-format nil "Point [~A, ~A]" x y))
               
               :shift
               (fn [xinc yinc]
                 (Point (+ x xinc) (+ y yinc))))]
         (fn [message & args]
           (apply (public-methods message) args)))))
                 
(def Point
     (fn [x y]
       (let [public-methods
             (fassoc (fap)
               :to-string 
               (fn []
                 (cl-format nil "Point [~A, ~A]" x y))
               
               :shift
               (fn [xinc yinc]
                 (Point (+ x xinc) (+ y yinc)))

               :x (fn [] x)                ; <<= New
               :y (fn [] y)                ; <<= New
               :add                        ; <<= New
               (fn [other]                 ; <<= New
                 ( (public-methods :shift) ; <<= New
                     (other :x)            ; <<= New
                     (other :y))))]        ; <<= New
         (fn [message & args]
           (apply (public-methods message) args)))))
                 
(def Point
     (fn [x y]
       (let [to-string 
             (fn []
               (cl-format nil "Point [~A, ~A]" x y))

             shift
             (fn [xinc yinc]
               (Point (+ x xinc) (+ y yinc)))
             
             add
             (fn [other]
               (shift (other :x) (other :y))) ; <= 1

             public-methods                   ; <= 2
             (fassoc (fap)
               :to-string to-string
               :x (fn [] x), :y (fn [] y)
               :add add)]                     ; <= 3
         (fn [message & args]
           (apply (public-methods message) args)))))
                 





         (cl-format nil "Point [~A, ~A]" x y))))

               :else
               (throw (new IllegalArgumentException
                           (str "Message missing:" message)))))))




(def Point
     (fn [x y]
       (letfn [(to-string [] (cl-format nil "Point [~A, ~A]" x y))
               (shift [xinc yinc]
                      (Point (+ x xinc) (+ y yinc)))]
         (let [publics {:to-string to-string
                        :shift shift}]
           (fn [message & args]
             (apply (publics message) args))))))
