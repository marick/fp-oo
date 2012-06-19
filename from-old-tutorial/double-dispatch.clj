
(def type-dispatch
     (fn [instance & args] (:type instance)))

(defmulti collide type-dispatch)
(defmulti collide-with-ship type-dispatch)
(defmulti collide-with-asteroid type-dispatch)

(defmethod collide :asteroid [asteroid thing]
   (collide-with-asteroid thing asteroid))

(defmethod collide :ship [ship thing]
   (collide-with-ship thing ship))

(defmethod collide-with-asteroid :asteroid [this-asteroid that-asteroid]
  (str "A bump in the night."))

(defmethod collide-with-asteroid :ship [ship asteroid]
  (str "Ship '" (:name ship) "' has been destroyed by an asteroid!"))


(defmethod collide-with-ship :asteroid [asteroid ship]
  (str "Ship '" (:name ship) "' has been destroyed by an asteroid!"))

(defmethod collide-with-ship :ship [this-ship that-ship]
  (str "Ships '" (:name this-ship) "' and '" (:name that-ship)
           "' have destroyed each other!"))






(defmulti collide (fn [& objects] (map :type objects)))

(defmethod collide [:asteroid :asteroid] [ast1 ast2]
  (str "A bump in the night."))

(defmethod collide [:asteroid :ship] [asteroid ship]
  (str "Ship '" (:name ship) "' has been destroyed by an asteroid!"))

(defmethod collide [:ship :asteroid] [ship asteroid]
  (collide asteroid ship))

(defmethod collide-with-ship [:ship :ship] [ship1 ship2]
  (str "Ships '" (:name ship1) "' and '" (:name ship2)
       "' have destroyed each other!"))




