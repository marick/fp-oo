(use '[clojure.pprint :only [cl-format]])

(load-file "sources/generic.clj")

(defn ship [name] (with-meta {:name name} {:type ::ship}))
(defn asteroid [name] (with-meta {:name name} {:type ::asteroid}))

(def names-ordered-by
     (fn [callable]
       (fn [things]
         (map :name (sort-by callable things)))))

(def report
     (fn [format-string & args]
       (apply (partial cl-format nil) format-string args)))


(defgeneric collide [& things]
   (sort (map type things)))

(defspecialized collide [::ship ::ship]
  [& ships]
  (let [[slower faster] ((names-ordered-by :speed) ships)]
    (report "The ~A smashes through the ~A!" faster slower)))

(collide (assoc (ship "Space Beagle") :speed 3)
         (assoc (ship  "Rim Griffon") :speed 8))

(defspecialized collide [::asteroid ::asteroid]
  [& asteroids] "asteroid-to-asteroid unimplemented")

(defspecialized collide [::asteroid ::ship]
  [& things] "asteroid-to-ship unimplemented")



;;; This version of `names-ordered-by` is for exercise 2.

(defgeneric names-ordered-by
  [callable] callable)

(defspecialized names-ordered-by :default
  [callable]
  (fn [things]
    (map :name (sort-by callable things))))


;;; For exercise 4

(defgeneric collide [& things]
   (vec (sort (map type things))))
