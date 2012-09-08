

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


