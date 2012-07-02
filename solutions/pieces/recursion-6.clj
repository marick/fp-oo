;;; Exercise 6

; I can't help you pat yourself on the back.
(reduce * 1 [1 2 3 4])
24
(reduce (fn [so-far val] (assoc so-far val (count so-far)))
        {}
        [:a :b :c])
{:c 0, :b 0, :a 0}
(reduce (fn [so-far val] (assoc so-far val (count so-far)))
        {}
        [:a :b :c])
{:c 2, :b 1, :a 0}
