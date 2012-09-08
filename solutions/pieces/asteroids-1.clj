
(load-file "sources/pieces/asteroids-1.clj")

;;; Exercise 1

(defspecialized collide [::asteroid ::ship]
  [& things]
  (let [[asteroid ship] ((names-ordered-by type) things)]
    (report "Asteroid ~A smashes the ~A!" asteroid ship)))

(collide (ship "Space Beagle") (asteroid "Malse"))

