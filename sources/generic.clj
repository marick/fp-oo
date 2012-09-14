

(def classifiers (atom {}))
(def specializations (atom {}))


(defmacro defgeneric [name classifier]
  (ns-unmap *ns* name)
  (let [specializations-to-rerun (@specializations name)]
    (swap! specializations dissoc name)
    `(do
       (defmulti ~name ~classifier)
         (swap! classifiers assoc '~name ~classifier)
         ~@specializations-to-rerun
         '~name)))
     

(defmacro defspecialized [name selector [_ args & body]]
  (swap! specializations
         (fn [current-map]
           (merge-with concat current-map {name [&form]})))
  `(do
     (defmethod ~name ~selector ~args ~@body)
     '~name))

(defn dispatch-value [symbol & args]
  (apply ((deref classifiers) symbol) args))

(defn forget [symbol]
  (ns-unmap *ns* symbol)
  (swap! classifiers dissoc symbol)
  (swap! specializations dissoc symbol)
  symbol)



