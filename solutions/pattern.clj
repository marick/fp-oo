(use 'patterned.sweet)


;;; Exercise 1

(defpatterned count-sequence
  [so-far [           ] ] so-far
  [so-far [head & tail] ] (count-sequence (inc so-far) tail)
  [      sequence       ] (count-sequence 0 sequence))

;;; Exercise 2

(defpatterned pattern-reduce
  [function so-far [] ]
  so-far

  [function so-far [head & tail]]
  (pattern-reduce function (function so-far head) tail))


