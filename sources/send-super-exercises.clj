;; Exercise 2 and 3

(send-to Klass :new
         'DynamicPoint 'Point
         {
          :shift
          (fn [xinc yinc]
            (str "Method found in " holder-of-current-method))
         }
         {})

(def point (send-to DynamicPoint :new 1 2))
(println (send-to point :shift 100 200))

;; Exercise 4

(declare send-super) ;; This is just here for my automated tests.

(send-to Klass :new
         'ExaggeratingPoint 'Point
         {
          :shift
          (fn [xinc yinc]
            (send-super (* 100 xinc) (* 100 yinc)))
         }
         {})
          
;; (def braggart (send-to ExaggeratingPoint :new 1 2))
;; (send-to braggart :shift 1 2)



(send-to Klass :new
                'SuperDuperExaggeratingPoint 'ExaggeratingPoint
                {
                 :shift
                 (fn [xinc yinc]
                   (send-super (* 1234 xinc) (* 1234 yinc)))
                 }
                {})

;; (def super-braggart (send-to SuperDuperExaggeratingPoint :new 1 2))
;; (send-to super-braggart :shift 1 2)



;; Exercise 5

;;; This first set of classes checks that `repeat-to-super` both
;;; works after a normal method call and after a previous
;;; `repeat-to-super`.

(declare repeat-to-super) ;; This is just here for my automated tests.


(send-to Klass :new
                'One 'Anything
                {
                 :add-instance-values
                 (fn [value] (assoc this :one value))
                }
                {})

(send-to Klass :new
                'Two 'One
                {
                 :add-instance-values
                 (fn [value]
                   (assoc (repeat-to-super) :two value))
                }
                {})

(send-to Klass :new
                'Three 'Two
                {
                 :add-instance-values
                 (fn [value]
                   (assoc (repeat-to-super) :three value))

                 :reveal
                 (fn []
                   [(:one this) (:two this) (:three this)])
                }
                {})

;; (def object (send-to Three :new 1))
;; (send-to object :reveal) ;; [1 1 1]


;;; This set of classes checks that `repeat-to-super` works
;;; after a `send-super`.

(send-to Klass :new
                'A 'Anything
                {
                 :add-instance-values
                 (fn [value] (assoc this :a value))
                }
                {})

(send-to Klass :new
                'B 'A
                {
                 :add-instance-values
                 (fn [value]
                   (assoc (repeat-to-super) :b value))
                }
                {})

(send-to Klass :new
                'C 'B
                {
                 :add-instance-values
                 (fn [value]
                   (assoc (send-super (* 2 value)) :c value))

                 :reveal
                 (fn []
                   [(:a this) (:b this) (:c this)])
                }
                {})

;; (def object (send-to C :new 1))
;; (send-to object :reveal) ;; (2 2 1)
