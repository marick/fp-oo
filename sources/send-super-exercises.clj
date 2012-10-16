;;; For Exercise 2

(send-to Klass :new
         'DynamicPoint 'Point
         {
          :shift
          (fn [xinc yinc]
            (println "Method" current-message "found in" holder-of-current-method)
            (println "It has these arguments:" current-arguments))
         }
         {})

(def point (send-to DynamicPoint :new 1 2))
(send-to point :shift 100 200)


;;; For exercise 3


(def throw-no-superclass-method-error
     (fn []
       (throw (Error. (str "No superclass method `" current-message
                           "` above `" holder-of-current-method
                           "`.")))))



;;; For Exercise 4

(send-to Klass :new
         'ExaggeratingPoint 'Point
         {
          :shift
          (fn [xinc yinc]
            (send-super (* 100 xinc) (* 100 yinc)))
         }
         {})
          
(def braggart (send-to ExaggeratingPoint :new 1 2))
(prn (send-to braggart :shift 1 2))  ;; A point at 101, 202



(send-to Klass :new
                'SuperDuperExaggeratingPoint 'ExaggeratingPoint
                {
                 :shift
                 (fn [xinc yinc]
                   (send-super (* 1234 xinc) (* 1234 yinc)))
                 }
                {})

(def super-braggart (send-to SuperDuperExaggeratingPoint :new 1 2))
(send-to super-braggart :shift 1 2)  ; a point at 123401, 246802


;;; For exercise 5

(send-to Klass :new
         'Upper 'Anything
         {
          :super-exists
          (fn [& args]
            (str "Got these args: " args))
          }
         {})


(send-to Klass :new
         'Lower 'Upper
         {
          :super-exists (fn [& args] (repeat-to-super))
          ;; If you like, you can use this to check whether
          ;; an attempt to repeat to a nonexistent super-method
          ;; correctly errors out.
          :super-missing (fn [& args] (repeat-to-super))
         }
         {})

(send-to Klass :new
         'Lowest 'Upper
         {}
         {})


(def object (send-to Lowest :new))
(println (send-to object :super-exists 1 2 3))

