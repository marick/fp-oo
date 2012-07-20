;;; 1

(def variant1
     (map (comp inc inc) [1 2 3]))

(def variant2
     (map (partial + 2) [1 2 3]))

;;; 2

; x         ;;; produces an error
; (myfun)   ;;; produces 3.

;; By substitution, x is replaced by 3 inside the body of the let.

;;; 3

(def myfun
     ( (fn [x]
         (fn [] x))
       3))


;;; 4

(def make-sender
     (fn [target-finder]
       (fn [instance message & args]
       (apply-message-to (target-finder instance)
                         instance message args))))

(def send-to (make-sender class-from-instance))
(def send-super (make-sender superclass-from-instance))

;;; 5

(def my-atom (atom 0))
(swap! my-atom (fn [anything] 33))

(swap! my-atom (constantly 34))

;;; 6

(def always
     (fn [value]
       (fn [& anything] value)))


