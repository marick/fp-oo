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



