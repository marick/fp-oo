;;; Reversed-digits shows one Clojure feature that is outside the
;;; scope of this book:
;;;
;;; 1. Java interoperability allows you to call Java methods---both instance
;;;    methods and class methods. `Integer.` refers to the Integer constructor.

(def reversed-digits
     (fn [string]
       (map (fn [digit-char]
              (-> digit-char str Integer.))
            (reverse string))))

;; (isbn? "0131774115")
;; (isbn? "0977716614")
;; (isbn? "1934356190")


;; (upc? "074182265830")
;; (upc? "731124100023")
;; (upc? "722252601404") ;; This one is incorrect.


