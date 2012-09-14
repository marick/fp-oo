
;;; For exercise 4

(defgeneric collide [& things]
   (vec (sort (map type things))))
