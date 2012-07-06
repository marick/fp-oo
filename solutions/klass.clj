(install (basic-class 'Klass,
                      :left 'MetaKlass,
                      :up 'Anything,
                      {
                       :new
                       (fn [class & args]
                         (let [seeded {:__class_symbol__ (:__own_symbol__ class)}]
                           (apply-message-to class seeded :add-instance-values args)))

                       :to-string                          ; <<<<= new
                       (fn [class]
                         (str "class " (:__own_symbol__ class)))
                      }))



;; I'll mark classes invisible by tagging them with metadata.

(def invisible
     (fn [class]
       (assoc class :__invisible__ true)))

(def invisible?
     (fn [class-symbol] (:__invisible__ (eval class-symbol))))

;; If you look left of a Point, you see 'Point. If you look left of class
;; Point, you see `MetaPoint`.
;; 

;; My solution builds on `lineage`. We can easily find the bottom of a
;; lineage by following the class symbol left. So, for example,
;; left of a Point is 'Point, and left of 'Point is `MetaPoint.
;; If the class is an invisible class, you have to go up. Rather than
;; introduce an `if` expression, I'll 

(def Klass
     (assoc-in Klass
               [:__instance_methods__ :ancestors]
               (fn [class]
                 (remove invisible?
                         (reverse (lineage (:__own_symbol__ class)))))))

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

(def MetaKlass
     (assoc-in MetaKlass
               [:__instance_methods__ :new]
                (fn [this
                     new-class-symbol superclass-symbol
                     instance-methods class-methods]
                  ;; Metaclass
                  (install
                   ;; VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV     new
                   (invisible
                    ;; ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ 
                    (basic-class (metasymbol new-class-symbol)
                                 :left 'Klass
                                 :up 'MetaAnything
                                 class-methods)))
                  ;; Class
                  (install
                   (basic-class new-class-symbol
                                :left (metasymbol new-class-symbol)
                                :up superclass-symbol
                                instance-methods)))))

(def MetaAnything (invisible MetaAnything))
(def MetaKlass (invisible MetaKlass))
(def MetaPoint (invisible MetaPoint))

(send-to Klass :new
         'ColoredPoint 'Point
         {
          :color :color
         
          :add-instance-values
          (fn [this x y color]
            (assoc (send-super this :add-instance-values x y)
              :color color))
         }
         {
          :origin (fn [class]
                    (send-to class :new 0 0 'white))
          })

(prn (send-to Anything :class-name))     
(prn (send-to Klass :class-name))     
(prn (send-to Point :class-name))
(prn (send-to ColoredPoint :class-name))

