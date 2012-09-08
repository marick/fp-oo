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
