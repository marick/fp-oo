;;; Exercise 4

;; The previous definition already works. Clojure's `=` can take one
;; or more arguments.

(prn (equal-triangles? right-triangle equal-right-triangle different-triangle))  ; false
