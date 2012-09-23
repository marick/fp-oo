(def dot get)

(def ^:dynamic this {:shift (fn [x y] "the default version of `shift`")})

(def default-this
     (fn [] (.getRawRoot #'this)))

(def js-apply
     (fn [javascript-function object arguments]
       (binding [this object]
         (apply javascript-function arguments))))

(def js-call
     (fn [javascript-function object & args]
       (js-apply javascript-function object args)))

(def send-to
     (fn [object message & arguments]
       (js-apply (get object message) object arguments)))

(def plain-call
     (fn [message & arguments]
       (apply send-to (default-this) message arguments)))

(def js-new
     (fn [constructor & arguments]
       (js-apply constructor {} arguments)))



;;; For Exercise 1

;;; This function (from the text) will be used to bootstrap the
;;; replacement of Clojure functions with Javascript Functions:

(def make-function 
     (fn [fn-value & property-pairs]
       (prn "FOOOO" fn-value property-pairs)
       (merge {:__function__ fn-value}
              (apply hash-map property-pairs))))

;; Here are some test cases to use with the new `make-function` after
;; you implement the new `js-apply`. (Note that they're wrapped in a
;; `comment` form so they don't execute within this file.

(comment

;; js-apply
(println (js-apply (make-function (fn [x] [this x]))
                   {:an :object}
                   [1]))

;; js-call
(println (js-call (make-function (fn [x] [this x]))
                  {:an :object}
                  1))

;; send-to
(def object {:some-property 5,
             :some-method
             (make-function
              (fn [x] (+ x (dot this :some-property))))})
(println (send-to object :some-method 3))

;; plain-call
(def ^:dynamic this {:some-property -3838383838
                     :value-returner
                     (make-function
                      (fn [] (dot this :some-property)))})
(def object {:value-returner
             (make-function
              (fn [] "SHOULD NOT BE CALLED"))
             :some-method
             (make-function
              (fn [] (plain-call :value-returner)))})

(println (send-to object :some-method))

;; js-new
(def Point
     (make-function
      (fn [x y]
        (assoc this
          :x x, :y y,
          :get-x (make-function (fn [] (dot this :x)))))))
(def point (js-new Point 1 2))

(println (send-to point :get-x))

)
