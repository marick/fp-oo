(use '[clojure.string :only [split]])

;;; Reversed-digits shows two Clojure features that are outside the
;;; scope of this book:
;;;
;;; 1. Clojure has regular expressions, which are written like this: #"string".
;;; 2. Java interoperability allows you to call Java methods---both instance
;;;    methods and class methods. `Integer/parseInt` refers to a class method.

(def reversed-digits
     (fn [string]
       (reverse
        (map (fn [digit-string] (Integer/parseInt digit-string))
             (rest (split string #""))))))

;; (isbn? "0131774115")
;; (isbn? "0977716614")
;; (isbn? "1934356190")


;; (upc? "074182265830")
;; (upc? "731124100023")
;; (upc? "722252601404") ;; This one is incorrect.


