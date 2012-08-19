
(load-file "sources/scheduling-variant.clj")

;;; This is a handy helper function. It produces a map that is the
;;; same as the `source-map`, except that the given function has been
;;; applied to the keys and the results put in the result map. For example:
;;;
;;; (update-with {:a 1, :b [1 2]} set [:b])
;;; {:a 1, :b #{1 2}}

 (def update-with
     (fn [source-map function keys]
       (reduce (fn [so-far key]
                 (assoc so-far key (function (key source-map))))
               source-map
               keys)))

;;; The registrant is now a map, containing the course's she's
;;; `:taking-now` and has `:previously-taken`, and whether she's
;;; a `:manager?`. 

(def solution
     (fn [courses registrant instructor-count]
       (let [registrant
             (update-with registrant
                          set
                          [:taking-now :previously-taken])

             core-flow
             (fn [courses]
               (-> courses
                   (annotate registrant instructor-count)
                   half-day-solution
                   final-shape))]
       
         (map core-flow
              (separate :morning? courses)))))

;;; Note that one annoyance with nested functions is that if you're writing a
;;; book and want a runnable file with a changed function, you show a lot of subfunctions
;;; that haven't changed.

(def annotate
     (fn [courses registrant instructor-count]
       (let [out-of-instructors?
             (= instructor-count
                (count (filter (fn [course]
                                 (> (:registered course) 0))
                               courses)))

             answer-annotations
             (fn [course]
               (assoc course
                 :spaces-left (- (:limit course)
                                 (:registered course))
                 :already-in? (contains? (:taking-now registrant) ; minor change
                                         (:course-name course))))

             domain-annotations
             (fn [course]
               (assoc course
                 :empty? (zero? (:registered course))
                 :full? (zero? (:spaces-left course))))

             note-unavailability
             (fn [course]
               (assoc course
                 :unavailable? (or (:full? course)
                                   (and out-of-instructors?
                                        (:empty? course))
                                   (and (:manager? registrant)     ;; New code
                                        (not (:morning? course)))
                                   (not (superset? (:previously-taken registrant)
                                                   (set (:prerequisites course)))))))
                 

             annotate-one
             (fn [course]
               (-> course
                   answer-annotations
                   domain-annotations
                   note-unavailability))]

         (map annotate-one courses))))
                      
;;; No changes to half-day-solution or final-shape
