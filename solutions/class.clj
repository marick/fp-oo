;;; Exercise 1

(def method-from-message
     (fn [message class]
       (message (:__instance_methods__ class))))

;; It's unfortunate that (eval 'Point) and (eval Point) evaluate to
;; the same map, because that allowed me to get away with code that
;; passed the latter instead of the former. That led to a
;; hard-to-debug problem in one of the next chapter's solutions. To
;; avoid that going forward, I'm putting an explicit type check on all
;; the functions that `eval`.

(def class-from-instance
     (fn [instance]
       (assert (map? instance))
       (eval (:__class_symbol__ instance))))

(def apply-message-to
     (fn [class instance message args]
       (apply (method-from-message message class)
              instance args)))

(def a
     (fn [class & args]
       (let [seeded {:__class_symbol__ (:__own_symbol__ class)}]
         (apply-message-to class seeded :add-instance-values args))))

(def send-to
     (fn [instance message & args]
       (apply-message-to (class-from-instance instance)
                         instance message args)))

;; For example:
(prn (send-to (a Point 1 2) :class))
(prn (send-to (a Point 1 2) :shift -1 -2))

;;; Exercise 2

(def Point
{
  :__own_symbol__ 'Point
  :__instance_methods__
  {
    :add-instance-values (fn [this x y]
                           (assoc this :x x :y y))
    ;;                                   vvvvv== New
    :class-name :__class_symbol__    
    :class (fn [this] (class-from-instance this))
    ;;                                   ^^^^^== New
    :shift (fn [this xinc yinc]
             (a Point (+ (:x this) xinc)
                      (+ (:y this) yinc)))
    :add (fn [this other]
           (send-to this :shift (:x other)
                                (:y other)))
   }
 })

(prn (send-to (a Point 1 2) :class-name))
(prn (send-to (a Point 1 2) :class))


;;; Exercise 3


(def my-point (a Point 1 2))

(def Point
{
  :__own_symbol__ 'Point
  :__instance_methods__
  {
    :add-instance-values (fn [this x y]
                           (assoc this :x x :y y))
    ;;                                   vvvvv== New
    :origin (fn [this] (a Point 0 0))
    ;;                                   ^^^^^== New
    :class-name :__class_symbol__    
    :class (fn [this] (class-from-instance this))
    :shift (fn [this xinc yinc]
             (a Point (+ (:x this) xinc)
                      (+ (:y this) yinc)))
    :add (fn [this other]
           (send-to this :shift (:x other)
                                (:y other)))
   }
 })

(prn (send-to my-point :origin))

;; Redefining a class changes the behavior of existing instances
;; because having an instance's :__class_symbol__ be a symbol that's
;; later `eval`ed adds a level of indirection. If the value were a
;; class map itself, changing the binding or association of `Point`
;; would have no effect on existing instances, just new ones.
;;
;; You could see that with this code:
;;    user=> (def Point "the original definition of Point")
;;    user=> (def a-point {:__class_NOT_symbol__ Point})
;;    user=> (def Point "the new definition of Point")
;;    user=> a-point
;;    {:__class_NOT_symbol__ "the original definition of Point"}
;;
;; If that's not clear, apply the substitution rule to the `def` lines.
;; (Note that `def` is another special symbol. It does not evaluate its
;; first argument, just the second.)
;;
;; "All problems in computer science can be solved by another level of
;; indirection." -- David Wheeler
;; 

;;; Exercise 4


(def apply-message-to
     (fn [class instance message args]
       (let [method (or (method-from-message message class)
                        message)]
       (apply method instance args))))

(prn (send-to (a Holder "stuff") :held))
