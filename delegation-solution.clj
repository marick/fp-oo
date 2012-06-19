;;; 1

(def dispatch
     (fn [keyword & args]
       (let [instance (first args)
             function (or (keyword instance)  ;; this is the change
                           (find-delegate keyword (:methods instance)))]
         (apply function args))))

(dispatch :string point) ;; [3,4]
(dispatch :string odd-point) ;; "1-2"

;;; 2

; :origin and :new work because they're instance methods on the object Point.

;;; 3

(def Point {
   :name 'Point
   :origin-area (fn [this] (* (:x this) (:y this)))
   :more-methods 'LastMethods
   :methods 'MetaPoint     ;; <== This is all!
})

(def MetaPoint {
   :name 'MetaPoint
   :new (fn [this x y] {:x x, :y y, :methods 'Point})
   :origin (fn [this] (dispatch :new this 0 0))
})


;;; 4

(def extend-Point
     (fn [module]
       (def Point (merge Point module))))


;;; 5

(def extend-class
     (fn [extended module]
       (let [linked-module (assoc module
                                  :more-methods extended
                                  :methods (:methods extended))]
         (redefine-var-from-name
          (:name extended) linked-module))))
     
