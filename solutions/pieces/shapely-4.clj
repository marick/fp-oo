

;;; Exercise 4

;; Here's an example of using a set to discover whether an element
;; is present.
(def sequence-containing?
     (fn [sequence element]
       (contains? (set sequence) element)))

;; This is a more terse example, but notice that it's not a true
;; predicate. That is, (sequence-containing? [1 2 3] 3) produces `3`,
;; which is truthy but not `true?`. Moreover, this form can't be used
;; to test whether nil is in a sequence because (#{1 nil} nil) returns
;; nil, which is the same value returned when an element is not present.
(def sequence-containing?
     (fn [sequence element]
       ((set sequence) element)))

;; A problem with the previous solution is that `set` will eagerly
;; evaluate an entire lazy seq that's given to it. So
;; (sequence-containing? (primes) 3593) would not be a good idea.
;;
;; The idiomatic Clojure way to avoid such evaluation is to use the
;; `some` function. `some` walks down the sequence, testing each
;; element with a callable. When the first one returns a truthy value, that
;; value is returned. (Note: *not* the element.) Therefore:
;; 
;; user=> (some even? [1 2 3])
;; true
;;
;; Since a set, used as a callable, returns a matching element or nil, this
;; is the idiomatic way to ask if a set contains an element:
;;
;; user=> (some #{1} [1 2 3])
;; 1
;;
;; Note that the actual element is returned (as a result of how sets-as-callables work).
;;
;; If you want a true predicate (which I do), you have to convert truthy values into `true` values.
;; Note also that you can't use `some` to check whether a sequence has a `nil` value. 
(def sequence-containing? 
     (fn [sequence element]
       (truthy? (some #{element} sequence))))

;; For our purposes, I don't want to assume that the first argument is
;; really a sequence, so an additional check:

(def sequence-containing? 
     (fn [maybe-sequence element]
       (and (sequential? maybe-sequence)
            (truthy? (some #{element} maybe-sequence)))))

(def pattern-classification
     (fn [pattern & rest]
       (cond (symbol? pattern)
             ::symbol

             (sequence-containing? pattern '&)    ;; <<==
             ::nested-with-rest

             (sequential? pattern)
             ::nested

             :else
             ::literal)))

;; My strategy for handling a rest argument is to decompose it into a `::nested` part and
;; a rest-parameter / excess arguments pair. Like this:
;; 
;; user=> (decompose-rest '[a 1 3 & rest] '(a 1 3 :extra :extra))
;; [[(a 1 3) (a 1 3)]
;;  [rest (:extra :extra)]]
(def decompose-rest
     (fn [pattern form]
       (let [base-pattern (drop-last 2 pattern)
             rest-parameter (last pattern)
             base-form (take (count base-pattern) form)
             rest-form (drop (count base-pattern) form)]
         [ [base-pattern base-form]
           [rest-parameter rest-form] ])))


(defspecialized match-one? ::nested-with-rest
  [pattern form]
  (and
   (sequential? form)
   (apply match-one? (first (decompose-rest pattern form)))))

(defspecialized match-map ::nested-with-rest
  [pattern form]
  (let [ [[base-pattern base-form] [rest-parameter rest-form]]
         (decompose-rest pattern form)]
    (merge (match-map base-pattern base-form)
           {rest-parameter rest-form})))
 





