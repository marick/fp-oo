;;; 1

(def map-concat
     (fn [function seq]
       (apply concat (map function seq))))

;;; 2

(map (comp inc inc) [1 2 3])
(map (partial + 2) [1 2 3])

;;; 3

(map (fn [n] (+ 2 n)) [1 2 3])

;;; 4

user> x ;;; produces an error
user> (myfun) ;;; produces 3.

;; By substitution, x is replaced by 3 inside the body of the let.

;;; 5

(def myfun
     ( (fn [x]
         (fn [] x))
       3))

;;; 6

(def my-atom (atom 0))
(swap! my-atom (fn [anything] 33))

(swap! my-atom (constantly 34))

(def always
     (fn [value]
       (fn [& anything] value)))
