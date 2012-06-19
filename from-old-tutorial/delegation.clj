(def find-delegate
     (fn [fn-keyword methods-name]
       (let [methods-object (named-object methods-name)]
         (cond (fn-keyword methods-object)
               (fn-keyword methods-object)

               (:more-methods methods-object)
               (find-delegate fn-keyword
                              (:more-methods methods-object))

               :else
               (fn [& args]
                 (throw (new Error "Method missing.")))))))

 
(def dispatch
     (fn [keyword & args]
       (let [instance (first args)
             function (find-delegate keyword (:methods instance))]
         (apply function args))))


(def LastMethods {
   :name 'LastMethods
   :string (fn [object] (str object))
})
     
(def Point {
   :name 'Point
   :new (fn [this x y] {:x x, :y y, :methods 'Point})
   :origin (fn [this] (dispatch :new this 0 0))
   :origin-area (fn [this] (* (:x this) (:y this)))
   :more-methods 'LastMethods
})

(def point {:x 3 :y 4 :methods 'Point})

(def var-from-name ;; You don't need to understand this.
     (fn [name]
       (intern *ns* name)))

(def named-object ;; You don't need to understand this.
     (fn [name]
       (deref (var-from-name name))))

(def redefine-var-from-name ;; You don't need to understand this.
     (fn [name new-value]
       (alter-var-root (intern *ns* name)
                       (constantly new-value))))




;; For problem 1
(def odd-point {
  :x 1
  :y 2
  :methods 'Point
  :string (fn [this] "odd point 1-2")})

;; For problem 4

(def Flippable {
   :origin-flip
   (fn [this]
     (dispatch :new Point
               (- (:x this))
               (- (:y this))))
})                
