
(load-file "sources/scheduling-variant.clj")


 (def update-with
     (fn [source-map function keys]
       (reduce (fn [so-far key]
                 (assoc so-far key (function (key source-map))))
               source-map
               keys)))

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
                 :already-in? (contains? (:taking-now registrant)
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
                                   (and (:manager? registrant)
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
