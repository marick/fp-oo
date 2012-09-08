(use '[clojure.pprint :only [cl-format]])

(defn ship [name] (with-meta {:name name} {:type ::ship}))
(defn asteroid [name] (with-meta {:name name} {:type ::asteroid}))

(def classify-colliding-things
     (fn [thing1 thing2]
       (sort [(type thing1) (type thing2)])))

(defmulti collide classify-colliding-things)


(defmulti names-ordered-by (fn [callable] callable))

(defmethod names-ordered-by :nothing
  [callable]
  (fn [things]
    (map :name things)))

(defmethod names-ordered-by :default
  [callable]
  (fn [things]
    ( (names-ordered-by :nothing) (sort-by callable things))))

(def report
     (fn [format-string & args]
       (apply (partial cl-format nil) format-string args)))

(defmethod collide [::asteroid ::asteroid] [& asteroids]
  (let [[name1 name2] ((names-ordered-by :nothing) asteroids)]
    (report "Asteroids ~A and ~A bounce harmlessly off each other." name1 name2)))

(defmethod collide [::asteroid ::ship]
  [& things]
  (let [[asteroid ship] ((names-ordered-by type) things)]
    (report "Asteroid ~A smashes the ~A!" asteroid ship)))

(defmethod collide [::ship ::ship]
  [& ships]
  (let [[slower faster] ((names-ordered-by :speed) ships)]
    (report "The ~A smashes through the ~A!" faster slower)))

