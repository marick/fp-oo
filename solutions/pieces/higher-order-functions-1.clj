;;; 1

(def variant1
     (map (comp inc inc) [1 2 3]))

(def variant2
     (map (partial + 2) [1 2 3]))

;;; 2

(def separate (juxt filter remove))

;;; 3

; x         ;;; produces an error
; (myfun)   ;;; produces 3.

;; By substitution, x is replaced by 3 inside the body of the let.

;;; 4

(def myfun
     ( (fn [x]
         (fn [] x))
       3))


;;; 5

(def my-atom (atom 0))
(swap! my-atom (fn [anything] 33))

;;; 6

(def always
     (fn [value]
       (fn [& anything] value)))

(swap! my-atom (always 8))

;; or

(swap! my-atom (constantly 8))

