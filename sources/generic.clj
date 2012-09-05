

(def dispatch-functions (atom {}))
(def specializations (atom {}))




(defmacro defgeneric [name params & body]
  (ns-unmap *ns* name)
  (let [specializations-to-rerun (@specializations name)]
    (swap! specializations dissoc name)
    `(do
       (let [dispatch-function# (fn ~params ~@body)]
         (defmulti ~name dispatch-function#)
         (swap! dispatch-functions assoc '~name dispatch-function#)
         ~@specializations-to-rerun
         '~name))))
     

(defmacro defspecialized [name selector args & body]
  (swap! specializations
         (fn [current-map]
           (merge-with concat current-map {name [&form]})))
  `(do
     (defmethod ~name ~selector ~args ~@body)
     '~name))

(defn dispatch-value [symbol & args]
  (apply ((deref dispatch-functions) symbol) args))

(defn forget [symbol]
  (ns-unmap *ns* symbol)
  (swap! dispatch-functions dissoc symbol)
  (swap! specializations dissoc symbol)
  symbol)
