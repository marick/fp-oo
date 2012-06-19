
;; This imports a function from a different "namespace"
(use '[clojure.string :only [split]])

;; Split a string on whitespace. The #"\s" notation is how
;; you write regular expressions in Clojure.
(def words
     (fn [string]
       (split string #"\s")))

(def empty-document [])
