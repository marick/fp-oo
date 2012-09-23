(ns sources.t-javascript
  (:use midje.sweet))

(load-file "sources/javascript.clj")

(fact "js-apply"
  (js-apply (fn [x] [this x])
            {:an :object}
            [1]) => [{:an :object} 1])


(fact "js-call"
  (js-call (fn [x] [this x])
           {:an :object}
           1) => [{:an :object} 1])

;; send-to
(def object {:some-property 5,
             :some-method
             (fn [x] (+ x (dot this :some-property)))})

(fact (send-to object :some-method 3) => 8)

;; plain-call
(def ^:dynamic this {:some-property -3838383838
                     :value-returner
                      (fn [] (dot this :some-property))})

(def object {:value-returner
             (fn [] "SHOULD NOT BE CALLED")
             :some-method
              (fn [] (plain-call :value-returner))})

(fact (send-to object :some-method) => -3838383838)

;; js-new
(def Point
     (fn [x y]
       (assoc this
         :x x, :y y,
         :get-x (fn [] (dot this :x)))))
(def point (js-new Point 1 2))

(fact (send-to point :get-x) => 1)


(fact "make-function"
  (make-function identity) => {:__function__ identity }
  (make-function identity :key :value) => {:__function__ identity, :key :value})


