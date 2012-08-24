
;; Step 2

(def zbranch? (comp seq? znode))

;; Here's a solution using `cond`:
(def znext
     (fn [zipper]
       (cond (and (zbranch? zipper)
                  (not (nil? (zdown zipper))))
             (zdown zipper)

             :else
             (zright zipper))))

;; However, having the second `cond` form repeat the last clause of the preceding test is
;; a hint that we should maybe take advantage of the way that boolean operations return
;; the last value evaluated and turn the whole thing into an `or`:

(def znext
     (fn [zipper]
       (or (and (zbranch? zipper)
                (zdown zipper))
           (zright zipper))))

