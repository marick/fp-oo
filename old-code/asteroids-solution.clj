
(load-file "sources/pieces/asteroids-1.clj")

;;; Exercise 1

(defspecialized collide [::asteroid ::ship]
  [& things]
  (let [[asteroid ship] ((names-ordered-by type) things)]
    (report "Asteroid ~A smashes the ~A!" asteroid ship)))

(collide (ship "Space Beagle") (asteroid "Malse"))



;;; Exercise 2

(load-file "sources/pieces/asteroids-2.clj")
  
(defspecialized names-ordered-by :nothing
  [callable]
  (fn [things]
    (map :name things)))

(defspecialized names-ordered-by :default
  [callable]
  (fn [things]
    ( (names-ordered-by :nothing) (sort-by callable things))))


;;; Exercise 3

(defspecialized collide [::asteroid ::asteroid] [& asteroids]
  (let [[name1 name2] ((names-ordered-by :nothing) asteroids)]
    (report "Asteroids ~A and ~A bounce harmlessly off each other." name1 name2)))

(collide (asteroid "Abbe") (asteroid "Malse"))


;;; Exercise 4

(load-file "sources/pieces/asteroids-4.clj")

(derive ::gaussjammer ::ship)

(def gaussjammer
     (fn [name]
       (with-meta {:name name} {:type ::gaussjammer})))

(defspecialized collide [::gaussjammer ::ship]
  [& things]
  (let [[gaussjammer other] ((names-ordered-by type) things)]
    (report "The poor Gaussjammer ~A was smashed by the ~A." gaussjammer other)))


(collide (asteroid "Abbe") (gaussjammer "Lode Trader"))
(collide (ship "Faraway Quest") (gaussjammer "Lode Trader"))
