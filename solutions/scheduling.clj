;;; Exercise 1

(def remove-full
     (fn [courses]
       (remove (fn [course]
                 (zero? (:spaces-left course)))
               courses)))

;;; Exercise 2

(def remove-unbookable
     (fn [courses instructor-count]
       (let [registrants?
             (fn [course] (not (zero? (:registered course))))

             with-registrants    (filter registrants? courses)
             without-registrants (remove registrants? courses)]

         (prn "++++++++++++++++++++")
         (clojure.pprint/pprint with-registrants)
         (clojure.pprint/pprint without-registrants)
         (if (< (count with-registrants) instructor-count)
           courses
           with-registrants))))
                                     
;;; Exercise 3

(use 'clojure.set)

(def add-back-already-in
     (fn [courses original-courses]
       (prn "================")
       (clojure.pprint/pprint courses)
       (clojure.pprint/pprint original-courses)
       (clojure.pprint/pprint (filter :already-in? original-courses))
       
       (union (set courses)
              (set (filter :already-in? original-courses)))))

;;; Exercise 4

;;; I have two solutions, one using `=`, one using a set.

(def asserty-add-back-already-in-1
     (fn [courses original-courses]
       (let [keys (map set (map keys (concat courses original-courses)))]
         (assert (apply = keys)))
       (add-back-already-in courses original-courses)))

;;; Notice that I use (map set (map keys ...)) instead of
;;; (map (fn [course] (set (keys course))) ...)
;;;
;;; That might seem excessively inefficient---two list traversals instead of one. In fact,
;;; though, as you'll see in the chapter on Laziness, it really gets compiled into a single
;;; list traversal. 

(def asserty-add-back-already-in-2
     (fn [courses original-courses]
       (let [keys (map set (map keys (concat courses original-courses)))]
         (assert (= 1 (count (set keys)))))
       (add-back-already-in courses original-courses)))
