;;; Exercise 3

(def Anything
     (assoc-in Anything
               [:__instance_methods__ :class-name]
               (fn [this]
                 (first (send-to (eval (:__class_symbol__ this))
                                 :ancestors)))))

(def Anything
     (assoc-in Anything
               [:__instance_methods__ :class]
               (fn [this]
                 (eval (send-to this :class-name)))))



(prn (send-to Anything :class-name))     
(prn (send-to Klass :class-name))     
(prn (send-to Point :class-name))
(prn (send-to ColoredPoint :class-name))

