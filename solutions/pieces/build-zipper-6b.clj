
;; Step 2

(def zbranch? (comp seq? znode))

;; Here's a solution using `cond`:
(def znext
     (fn [zipper]
       (cond (and (zbranch? zipper)
                  (not (nil? (zdown zipper))))   ; (1)
             (zdown zipper)                      ; (2)

             :else
             (zright zipper))))

;; However, having line (2) repeat the `zdown` from line (1) is a hint
;; that we should maybe take advantage of the way that boolean
;; operations return the last value evaluated. That lets us turn the
;; whole `cond` into an `or`:

(def znext
     (fn [zipper]
       (or (and (zbranch? zipper)
                (zdown zipper))
           (zright zipper))))

