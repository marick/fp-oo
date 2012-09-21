(ns sources.t-javascript
  (:use midje.sweet))

(load-file "sources/pieces/javascript-1.clj")
(load-file "sources/pieces/javascript-2.clj")

(fact "js-apply"
  (js-apply (make-function (fn [x] [this x]))
            {:an :object}
            [1]) => [{:an :object} 1])


(fact "js-call"
  (js-call (make-function (fn [x] [this x]))
           {:an :object}
           1) => [{:an :object} 1])

;; send-to
(def object {:some-property 5,
             :some-method
             (make-function
              (fn [x] (+ x (dot this :some-property))))})

(fact (send-to object :some-method 3) => 8)

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

(fact (send-to object :some-method) => -3838383838)

;; js-new
(def Point
     (make-function
      (fn [x y]
        (assoc this
          :x x, :y y,
          :get-x (make-function (fn [] (dot this :x)))))))
(def point (js-new Point 1 2))

(fact (send-to point :get-x) => 1)

(load-file "sources/pieces/javascript-3.clj")

(fact "make-function"
  (make-function identity) => {:__function__ identity }
  (make-function identity :key :value) => {:__function__ identity, :key :value})

(load-file "sources/pieces/javascript-4.clj")


(def this {})
(fact "Function constructor"
  (Function identity :key :value) => (make-function identity :key :value))
