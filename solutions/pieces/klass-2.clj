;;; Exercise 2


;; I'll mark classes invisible by tagging them with metadata.

(def invisible
     (fn [class]
       (assoc class :__invisible__ true)))

(def invisible?
     (fn [class-symbol] (:__invisible__ (eval class-symbol))))

;; Change the already-defined metaclasses to be invisible:

(def MetaAnything (invisible MetaAnything))
(def MetaKlass (invisible MetaKlass))
(def MetaPoint (invisible MetaPoint))

;; Ancestors just removes invisible classes from the
;; reversed lineage.

(def Klass
     (assoc-in Klass
               [:__instance_methods__ :ancestors]
               (fn [class]
                 (remove invisible?
                         (reverse (lineage (:__own_symbol__ class)))))))

;; New metaclasses need to be created to be invisible.

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


;; Test data:

(send-to Klass :new
         'ColoredPoint 'Point
         {
          :color :color
         
          :add-instance-values
          (fn [this x y color]
            ;; This is a hack because we haven't implemented
            ;; `send-super` yet.
            (merge (send-to Point :new x y)
                   (assoc this :color color)))
         }
         {
          :origin (fn [class]
                    (send-to class :new 0 0 'white))
          })

(prn (send-to Anything :ancestors))     
(prn (send-to Klass :ancestors))     
(prn (send-to Point :ancestors))
(prn (send-to ColoredPoint :ancestors))


