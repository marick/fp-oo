(use 'clojure.set) ;; Not actually used here, but will be in exercises.

(def separate
     (fn [pred sequence]
       [(filter pred sequence) (remove pred sequence)]))



(def solution
     (fn [courses registrants-courses instructor-count]

       (let [answer-annotations
             (fn [courses]
               (let [checking-set (set registrants-courses)]
                 (map (fn [course]
                        (assoc course
                          :spaces-left (- (:limit course)
                                          (:registered course))
                          :already-in? (contains? checking-set
                                                  (:course-name course))))
                      courses)))

             domain-annotations
             (fn [courses]
               (map (fn [course]
                      (assoc course
                        :empty? (zero? (:registered course))
                        :full? (zero? (:spaces-left course))))
                    courses))

             note-unavailability
             (fn [courses]
               (let [out-of-instructors?
                     (= instructor-count
                        (count (filter (fn [course] (not (:empty? course)))
                                       courses)))]
                 (map (fn [course]
                        (assoc course
                          :unavailable? (or (:full? course)
                                            (and out-of-instructors?
                                                 (:empty? course)))))
                      courses)))

             annotate
             (fn [courses]
               (-> courses
                   answer-annotations
                   domain-annotations
                   note-unavailability))

             visible-courses
             (fn [courses]
               (let [[guaranteed possibles] (separate :already-in? courses)]
                 (concat guaranteed (remove :unavailable? possibles))))

             final-shape
             (fn [courses]
               (let [desired-keys [:course-name :morning? :registered :spaces-left :already-in?]]
                 (map (fn [course]
                        (select-keys course desired-keys))
                      courses)))

             half-day-solution
             (fn [courses]
               (-> courses
                   annotate
                   visible-courses
                   ((fn [courses] (sort-by :course-name courses)))
                   final-shape))]

         (map (fn [courses]
                (half-day-solution courses))
              (separate :morning? courses)))))

