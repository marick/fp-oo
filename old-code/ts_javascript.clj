(ns solutions.ts-javascript
  (:use midje.sweet)
  (:use clojure.pprint))

(load-file "sources/javascript.clj")
(load-file "solutions/javascript-complete.clj")

(def root (generic-object :up "up"
                          :obscured "obscured"))

(def below (mimic root
                  :obscured "doubly obscured"
                  :down "down"))


(fact "dot searches prototype chain"
  (dot below :up) => "up"
  (dot below :obscured) => "doubly obscured"
  (dot below :down) => "down"
  (dot below :nevermore) => :undefined)
 

;;; The four non-creational functions

(def usa (generic-object :currency-symbol :USD,
                         :tip-rate 1.20,
                         :taxicab-seating :back))

(def pay-this-amount
     (js-new Function (fn [bill-amount]
        (* bill-amount
           (dot *context-object* :tip-rate)))))

(fact
  (apply-in-context usa pay-this-amount [10.0]) => (roughly 12.0)
  (call-in-context  usa pay-this-amount 10.0) => (roughly 12.0))


(let [object (generic-object :x 1
                             :plus-x (js-new Function (fn [n]
                                       (+ n (dot *context-object* :x)))))]
  (fact (dot-call object :plus-x 3) => 4))

(fact (plain-call :default-function) => (dot ground-object :default-property))


;;; The function object

(fact "Function is unique in that its __proto__ and prototype are the same"
  (dot (dot Function :prototype) :call) => (dot (dot Function :__proto__) :call))

(fact "The Function prototype is in the __proto__ chain for all functions"
  (dot Function :call) => (:call (:__proto__ Function))
  (dot Point :call) => (dot Function :call))

(fact "functions in the Function prototype have usable __proto__ chains"
  (dot (dot Function :call) :glorp) => :undefined)
  
(fact "A function constructed by Function can make new objects"
  (def Magnitude (js-new Function
                         (fn [x] (assoc *context-object* :magnitude x
                                                         :documentation "a Magnitude"))
                         :documentation "Make a Magnitude"))
  (dot Magnitude :call) => (dot Function :call)
  (dot Magnitude :documentation) => "Make a Magnitude"
  (let [magnitude (js-new Magnitude 3)]
    (dot magnitude :magnitude) => 3
    (dot magnitude :__proto__) => (generic-object)
    (dot magnitude :call) => :undefined
    (dot magnitude :prototype) => :undefined
    (dot magnitude :documentation) => "a Magnitude"))


;;; Point

(fact "Mimics of Function can use `call` and `apply`."
  (let [object (generic-object :value 5)
        adder (js-new Function (fn [x] (+ (dot *context-object* :value) x)))]
    (dot-call adder :apply object [2]) => 7
    (dot-call adder :call object 2) => 7)

  (let [point (js-new Point 1 2)
        shift (dot point :shift)]
    (dot-call shift :call point 100 200) => (js-new Point 101 202)
    (dot-call shift :apply point [100 200]) => (js-new Point 101 202)))
  
    



(fact
  (:__proto__ Point) => (dot Function :prototype)
  (dot (dot Point :prototype) :call) => :undefined
  (dot (dot Point :prototype) :shift) =not=> :undefined
  (:prototype Point) =not=> (dot Function :prototype)
  (:__proto__ (:prototype Point)) => nil)

(fact "js-new works"
  (let [point (js-new Point 1 2)
        shifted (dot-call point :shift 100 200)]
    (dot point :x) => 1
    (dot point :y) => 2
    (dot shifted :x) => 101
    (dot shifted :y) => 202))
    
