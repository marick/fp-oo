;;; Exercise 1

;;; I'm using a new Clojure function: `assoc-in`. It lets you create a
;;; new map with a deeply nested value changes. You give it the path
;;; to the value you want replaced.

(def Klass
     (assoc-in Klass
               [:__instance_methods__ :to-string]
               (fn [class]
                 (str "class " (:__own_symbol__ class)))))


