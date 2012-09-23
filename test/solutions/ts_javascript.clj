(ns solutions.ts-javascript
  (:use midje.sweet))

(load-file "solutions/javascript.clj")

;;; Exercise 1

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



;;; Exercise 2

(def ^:dynamic this {})
(fact "Function constructor"
  (js-new Function identity :key :value) => (make-function identity :key :value))

(def Point
     (js-new Function
             (fn [x y]
               (assoc this :x x, :y y))))

(fact (js-new Point 1 2) => {:y 2, :x 1})

(def Point
     (js-new Function
             (fn [x y]
               (assoc this :x x, :y y))
             :a-property-for-Point 5))
(fact
  Point => (contains {:a-property-for-Point 5})
  (js-new Point 1 2) => {:y 2, :x 1})

(def Point (js-call Function
                    ;; Function, do your thing with the following as `this`:
                    {:a-property-for-Point 5}
                    ;; Function, here is the Clojure function to use:
                    (fn [x y] (assoc this :x x :y y))
                    ;; Here are properties to add to the resulting `Point`:
                    :another-property "here"))

(fact
  Point => (contains {:a-property-for-Point 5 :another-property "here"})
  (js-new Point 1 2) => {:y 2, :x 1})

