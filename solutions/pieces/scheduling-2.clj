;;; Exercise 2

;; A new registrant map:

{:manager? true
 :course-names ["zig" "zag"]
 :previously-taken ["updating"]}

;; Another field for a course:

{:morning? true
 ; ...
 :prerequisites ["zigging"]}

(def note-unavailability
     (fn [courses instructor-count registrant]
       (let [out-of-instructors?
             (= instructor-count
                (count (filter (fn [course] (not (:empty? course)))
                               courses)))]
         (map (fn [course]
                (assoc course
                       :unavailable? (or (:full? course)
                                         (and out-of-instructors?
                                              (:empty? course))
                                         (and (:manager? registrant)
                                              (not (:morning? course)))
                                         (not (superset? (set (:previously-taken registrant))
                                                         (set (:prerequisites course)))))))
              courses))))

;;; Also have to change `annotate` to call `note-unavailability` with the right argument.

(def annotate
     (fn [courses registrant instructor-count]
       (-> courses
           (answer-annotations (:course-names registrant))
           domain-annotations
           (note-unavailability instructor-count registrant))))

