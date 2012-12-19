;;; Exercise 3

(def equal-triangles? =)  ;; See the postscript.


(prn (equal-triangles? right-triangle right-triangle))       ; true
(prn (equal-triangles? right-triangle equal-right-triangle)) ; true
(prn (equal-triangles? right-triangle different-triangle))   ; false

