;;; 1


(def ys-and-ns
     (filter (fn [string]
               (or (.startsWith string "y")
                   (.startsWith string "n")))
             (repeatedly prompt-and-read)))

;;; 2

(def counted-sum
     (fn []
       (let [numbers-only
             (map to-integer
                  (filter number-string?
                          (repeatedly prompt-and-read)))
             number-count (first numbers-only)
             numbers (take number-count (rest numbers-only))]
         (apply + numbers))))
             
;;  3

(def prompt-and-read
     (fn [prompt]
       (print (str prompt "> "))
       (.flush *out*)
       (.readLine (new java.io.BufferedReader *in*))))

(def prompted-numbers
     (fn [prompt]
       (map to-integer
            (filter number-string?
                    (repeatedly (fn []
                                  (prompt-and-read prompt)))))))

(def counted-sum
     (fn []
       (let [number-count (first (prompted-numbers "How many?"))
             numbers (take number-count (prompted-numbers "Number"))]
         (apply + numbers))))

;;; 4  - I don't think I want to use this exercise.

(def keep-prompting
     (fn [first-prompt second-prompt]
       (lazy-seq (cons (prompt-and-read first-prompt)
                       (repeatedly (fn [] (prompt-and-read second-prompt)))))))
(def one-number
     (fn [first-prompt second-prompt]
       (to-integer (first (filter number-string?
                                  (keep-prompting first-prompt second-prompt))))))
     
(def many-numbers
     (fn [first-prompt second-prompt]
       (repeatedly (fn [] (one-number first-prompt second-prompt)))))

(def counted-sum
     (fn []
       (let [number-count (one-number "How many?" "A number, please")
             numbers (take number-count (many-numbers "Number" "No, really"))]
         (apply + numbers))))

(def counted-sum
     (fn []
       (let [number-count (one-number "Numbers to sum?" "A number, please")
             numbers (take number-count (many-numbers "Number" "No, really"))]
         (apply + numbers))))

