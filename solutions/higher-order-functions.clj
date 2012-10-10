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

(def make-sender
     (fn [target-finder]
       (fn [instance message & args]
       (apply-message-to (target-finder instance)
                         instance message args))))

(def send-to (make-sender class-from-instance))
(def send-super (make-sender superclass-from-instance))

;;; 6

(def my-atom (atom 0))
(swap! my-atom (fn [anything] 33))

;;; 7

(def always
     (fn [value]
       (fn [& anything] value)))

(swap! my-atom (always 8))

;; or

(swap! my-atom (constantly 8))


;;; 8

(def check-sum
     (fn [sequence]
       (apply + (map *
                     (range 1 (inc (count sequence)))
                     sequence))))

;;; 9

(def isbn?
     (fn [candidate]
       (zero? (rem (check-sum (reversed-digits candidate)) 11))))

;;; 10

(def check-sum
     (fn [sequence]
       (apply + (map (fn [position digit]
                       (* digit (if (odd? position) 1 3)))
                     (range 1 (inc (count sequence)))
                     sequence))))


(def upc?
     (fn [candidate]
       (zero? (rem (check-sum (reversed-digits candidate)) 10))))


;;; 11

(def number-checker
     (fn [digit-function divisor]
       (fn [candidate]
         (let [digits (reversed-digits candidate)
               check-sum (apply +
                                (map digit-function
                                     (range 1 (inc (count digits)))
                                     digits))]
           (zero? (rem check-sum divisor))))))

(def isbn? (number-checker * 11))
(def upc? (number-checker
           (fn [position digit] (* digit (if (odd? position) 1 3)))
           10))
             
