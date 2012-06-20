;;; Exercise 1

(def second (fn [vec] (first (rest vec))))


;;; Exercise 2:
(def third (fn [vec] (first (rest (rest vec)))))

(def third (fn [vec] (second (rest vec))))


;;; Exercise 3:

(def add-squares
     (fn [& numbers]
       (apply + (map * numbers numbers))))

;;; Exercise 4:

(def factorial
     (fn [n]
       (apply * (range 1 (inc n)))))


;;; Exercise 5:

;; FILTER
;; Pick the first three even numbers from a list
(take 3 (filter even? '(8 3 9 2 38 1)))

;; DISTINCT
;; How many elements in a vector are redundant (copies)?
(- (count           [1 1 1 2 3 4 4 1 1])
   (count (distinct [1 1 1 2 3 4 4 1 1])))
;; Duplicating the vector is annoying, but we don't know yet
;; how to create local variables. We *do* know how to create
;; functions with arguments, though:

(def redundancies
     (fn [x]
       (- (count x)
          (count (distinct x)))))
(redundancies [1 1 1 2 3 4 4 1 1])

;; CONCAT
;; You're given three lists. You want a single list that is
;; the first element of the first, followed by the first two elements
;; of the second, followed by the first three elements of the third.

(concat (take 1 '(A _ _ _))
        (take 2 '(B C _ _))
        (take 3 '(D E F _ _))) 
;; Notice that we can put symbols in these lists because the quote
;; prevents them from being evaluated.

;; REPLICATE
;; Given a list L. Suppose its third and fifth elements are
;; A and B. Produce this list:
;;    ( (A B) (A B) (A B) (A B) )

(def solution
     (fn [L]
       (replicate 4 (list (nth L 2) (nth L 4)))))
(solution '(_ _ A _ B _ _))

;; INTERLEAVE
;; Separate a bunch of numbers with underscore symbols:
(def solution
     (fn [s]
       (take (dec (* 2 (count s)))
             (interleave s (replicate (count s) _)))))
(solution [1 3 5 7])

;; DROP and DROP-LAST
;; Return the middlemost 2 elements of an even-element sequence.
(def trim-count
     (fn [seq] (/ (- (count seq) 2) 2)))
(def middlemost
     (fn [seq]
       (drop-last (trim-count seq)
                  (drop (trim-count seq)
                        seq))))

;; FLATTEN
;; Add the elements of a sequence of elements without using `cons`
(eval (flatten [+ [1 2 3 4]]))

;; PARTITION
;; Convert (1 2 3 4) into (3 4 1 2)
(flatten (reverse (partition 2 '(1 2 3 4))))


;; EVERY?
;; Is ( () () () )  a list of empty lists?
(every? empty? '( () () () ))

;; REMOVE
;; Remove all the nil values from a sequence
(remove (fn [v] (= v nil)) [1 nil 3 nil]) 
;; Note this could also be done like this:
(remove nil? [1 nil 3 nil]) 

;;; Exercise 6:

(def prefix-of?
     (fn [candidate seq]
       (= (take (count candidate) seq)
          candidate)))

(def tails
     (fn [seq]
       (map drop
            (range (inc (count seq)))
            (replicate (inc (count seq)) seq))))


