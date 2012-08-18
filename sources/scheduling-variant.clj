(use 'clojure.set) ;; Not actually used here, but will be in exercises.

(def separate
     (fn [pred sequence]
       [(filter pred sequence) (remove pred sequence)]))


;;; Three main components
(declare annotate half-day-solution final-shape)

(def solution
     (fn [courses registrants-courses instructor-count]
       (let [core-flow
             (fn [courses]
               (-> courses
                   (annotate (set registrants-courses) instructor-count)
                   half-day-solution
                   final-shape))]
         (map core-flow
              (separate :morning? courses)))))

(def annotate
     (fn [courses registrants-courses instructor-count]
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
                 :already-in? (contains? registrants-courses
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
                                        (:empty? course)))))

             annotate-one
             (fn [course]
               (-> course
                   answer-annotations
                   domain-annotations
                   note-unavailability))]

         (map annotate-one courses))))
                      

(def half-day-solution
     (fn [courses]
       (let [visible-courses
             (fn [courses]
               (let [[guaranteed possibles] (separate :already-in? courses)]
                 (concat guaranteed (remove :unavailable? possibles))))

             sort-by-name
             (fn [courses] (sort-by :course-name courses))]

         (-> courses visible-courses sort-by-name))))

(def final-shape
     (fn [courses]
       (let [desired-keys [:course-name :morning? :registered :spaces-left :already-in?]]
         (map (fn [course]
                (select-keys course desired-keys))
              courses))))
