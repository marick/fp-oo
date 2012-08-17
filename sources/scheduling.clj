(use 'clojure.set) ;; Not actually used here, but will be in exercises.


(def answer-annotations
     (fn [courses registrants-courses]
       (let [checking-set (set registrants-courses)]
         (map (fn [course]
                (assoc course
                       :spaces-left (- (:limit course)
                                       (:registered course))
                       :already-in? (contains? checking-set
                                               (:course-name course))))
              courses))))

(def domain-annotations
     (fn [courses]
       (map (fn [course]
              (assoc course
                :empty? (zero? (:registered course))
                :full? (zero? (:spaces-left course))))
            courses)))

(def note-unavailability
     (fn [courses instructor-count]
       (let [out-of-instructors?
             (= instructor-count
                (count (filter (fn [course] (not (:empty? course)))
                               courses)))]
         (map (fn [course]
                (assoc course
                       :unavailable? (or (:full? course)
                                         (and out-of-instructors?
                                              (:empty? course)))))
              courses))))

(def annotate
     (fn [courses registrants-courses instructor-count]
       (note-unavailability (domain-annotations
                             (answer-annotations courses registrants-courses))
                            instructor-count)))
                             
(def annotate
     (fn [courses registrants-courses instructor-count]
       (let [answers (answer-annotations courses registrants-courses)
             domain (domain-annotations answers)]
         note-unavailability domain instructor-count)))

(def annotate
     (fn [courses registrants-courses instructor-count]
       (-> courses
           (answer-annotations registrants-courses)
           domain-annotations
           (note-unavailability instructor-count))))




(def separate
     (fn [pred sequence]
       [(filter pred sequence) (remove pred sequence)]))


(def visible-courses
     (fn [courses]
       (let [[guaranteed possibles] (separate :already-in? courses)]
         (concat guaranteed (remove :unavailable? possibles)))))

(def final-shape
     (fn [courses]
       (let [desired-keys [:course-name :morning? :registered :spaces-left :already-in?]]
         (map (fn [course]
                (select-keys course desired-keys))
              courses))))



(def half-day-solution
     (fn [courses registrants-courses instructor-count]
       (-> courses
           (annotate registrants-courses instructor-count)
           visible-courses
           ((fn [courses] (sort-by :course-name courses)))
           final-shape)))

(def solution
     (fn [courses registrants-courses instructor-count]
       (map (fn [courses]
              (half-day-solution courses registrants-courses instructor-count))
            (separate :morning? courses))))

