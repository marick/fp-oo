












(def left-up-from-instance
     (fn [instance]
       (assert (map? instance))
       (eval (:__up_symbol__ (left-from-instance instance)))))

(def send-super
     (fn [instance message & args]
       (apply-message-to (left-up-from-instance instance)
                         instance message args)))

(send-to Klass :new
         'ExaggeratingPoint 'Point
         {
          :shift
          (fn [xinc yinc]
            (send-super this :shift (* 100 xinc) (* 100 yinc)))
         }
         {})
          
(def braggart (send-to ExaggeratingPoint :new 1 2))
(send-to braggart :shift 1 2)



(send-to Klass :new
                'SuperDuperExaggeratingPoint 'ExaggeratingPoint
                {
                 :shift
                 (fn [xinc yinc]
                   (send-super this :shift (* 1234 xinc) (* 1234 yinc)))
                 }
                {})



(def super-braggart (send-to SuperDuperExaggeratingPoint :new 1 2))
(send-to super-braggart :shift 1 2)
