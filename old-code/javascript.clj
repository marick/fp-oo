(def mimic
     (fn [object & property-pairs]
       (assoc (apply hash-map property-pairs)
         :__proto__ object)))

(declare ^:dynamic *context-object*)


(def generic-object
     (fn [& property-pairs] 
       (apply mimic nil property-pairs)))

(def dot
     (fn [object key]
       (cond (nil? object)
          :undefined
          
          (contains? object key)
          (get object key)
          
          :else
          (dot (:__proto__ object) key))))


(def ^:dynamic *context-object* (generic-object))


;;; The five kinds of Javascript functions

(def apply-in-context
     (fn [context-object javascript-function arguments]
       (binding [*context-object* context-object]
         (apply (dot javascript-function :__fn__) arguments))))

(def call-in-context
     (fn [context-object javascript-function & arguments]
       (apply-in-context context-object javascript-function arguments)))

;; I made this check errors because the behavior when
;; :undefined is applied-in-context can be confusing.
(def dot-call
     (fn [object function-name & arguments]
       (let [javascript-function (dot object function-name)]
         (if (= javascript-function :undefined)
           (throw (Error. (str "No such function: " function-name)))
           (apply-in-context object
                             (dot object function-name)
                             arguments)))))

(declare ground-object)
(def plain-call
     (fn [function-name & arguments]
       (apply dot-call ground-object function-name arguments)))


(def js-new
     (fn [javascript-constructor & arguments]
       (apply-in-context (mimic (dot javascript-constructor :prototype))
                         javascript-constructor
                         arguments)))

;;; Core objects
(def Function
     {:__fn__
      (fn [constructor-fn & property-pairs]
        ;; At this point, *context-object* is normally a
        ;; mimic of a constructor's prototype
        (apply assoc *context-object*
               :__fn__ constructor-fn
               :prototype (generic-object)
               property-pairs))
      :prototype (generic-object)})

(def prototype-for-Function
     (generic-object :apply
                     (js-new Function (fn [] "see exercises"))
                     :call
                     (js-new Function (fn [] "see exercises"))))

(def Function (assoc Function
                     :__proto__ prototype-for-Function
                     :prototype prototype-for-Function))


(def ground-object
     (generic-object
      :default-property 5
      :default-function (js-new Function
                                (fn [] (dot *context-object* :default-property)))))




;;; ======= Starting code for exercises

(def Point
     (js-new Function 
          (fn [x y] (assoc *context-object* :x x, :y y))

          :documentation "The constructor for Point"
          
          :prototype
          (generic-object
           :shift (js-new Function
                          (fn [xinc yinc]
                            (assoc *context-object*
                                   :x (+ xinc (dot *context-object* :x))
                                   :y (+ yinc (dot *context-object* :y))))))))


(def ColoredPoint
     (js-new Function
             (fn [x y color]
               (assoc (call-in-context *context-object* Point x y)
                 :color color))
             :prototype (dot Point :prototype)))

