(use '[clojure.pprint :only [cl-format]])

(def report
     (fn [format-string & args]
       (apply (partial cl-format nil) format-string args)))



(load-file "sources/generic.clj")

(derive ::gaussjammer ::ship)
(derive ::generation-ship ::ship)
(derive ::generation-ship ::asteroid)


(def thing-maker
     (fn [type]
       (fn [name] (with-meta {:name name} {:type type}))))

  
(def ship (thing-maker ::ship))
(def asteroid (thing-maker ::asteroid))
(def gaussjammer (thing-maker ::gaussjammer))
(def generation-ship (thing-maker ::generation-ship))


;;; names-ordered-by
(defgeneric names-ordered-by
  [callable] callable)

(defspecialized names-ordered-by :nothing
  [callable]
  (fn [things]
    (map :name things)))

(defspecialized names-ordered-by :default
  [callable]
  (fn [things]
    ( (names-ordered-by :nothing) (sort-by callable things))))


;;; collide
(defgeneric collide [& things]
  (vec (sort (map type things))))

;; Note that these preferences don't cover the case of one
;; generation ship smashing into another.
(prefer-method collide [::asteroid ::ship] [::ship ::ship])
(prefer-method collide [::asteroid ::ship] [::asteroid ::asteroid])

(defspecialized collide [::ship ::ship]
  [& ships]
  (let [[slower faster] ((names-ordered-by :speed) ships)]
    (report "The ~A smashes through the ~A!" faster slower)))

(defspecialized collide [::asteroid ::asteroid] [& asteroids]
  (let [[name1 name2] ((names-ordered-by :nothing) asteroids)]
    (report "Asteroids ~A and ~A bounce harmlessly off each other." name1 name2)))

(defspecialized collide [::asteroid ::ship]
  [& things]
  (let [[asteroid ship] ((names-ordered-by type) things)]
    (report "Asteroid ~A smashes the ~A!" asteroid ship)))

(defspecialized collide [::gaussjammer ::ship]
  [& things]
  (let [[gaussjammer other] ((names-ordered-by type) things)]
    (report "The poor Gaussjammer ~A was smashed by the ~A." gaussjammer other)))



;;; Test cases

(collide (assoc (ship "Space Beagle") :speed 3)
         (assoc (ship  "Rim Griffon") :speed 8))

(collide (ship "Space Beagle") (asteroid "Malse"))
(collide (asteroid "Abbe") (asteroid "Malse"))
(collide (asteroid "Abbe") (gaussjammer "Lode Trader"))
(collide (ship "Faraway Quest") (gaussjammer "Lode Trader"))

(collide (generation-ship "Yonada") (ship "Orion III"))
(collide (ship "Orion III") (generation-ship "Yonada"))
(collide (generation-ship "Yonada") (asteroid "Abbe"))


;;; type-description

(defgeneric type-description)
(prefer-method type-description ::ship ::asteroid)

(defspecialized type-description ::ship [_] "ship")
(defspecialized type-description ::asteroid [_] "asteroid")
(defspecialized type-description ::gaussjammer [_] "Gaussjammer")
