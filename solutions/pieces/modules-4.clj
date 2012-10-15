;;; Exercise 4

;; This version just adds a flag so it's easy to tell that
;; the stub is a stub.
(install
 (method-holder 'Module
                :left 'MetaModule
                :up 'Anything
                {
                 :include
                 (fn [this module]
                   (let [module-name (:__own_symbol__ module)
                         stub-name (gensym module-name)
                         stub {:__own_symbol__ stub-name
                               :__up_symbol__ (:__up_symbol__ this)
                               :__left_symbol__ module-name
                               :__module_stub?__ true}]              ;;<<== New
                     (install (assoc this :__up_symbol__ stub-name))
                     (install stub)))
               }))


(def names-module-stub?
     (fn [symbol]
       (:__module_stub?__ (eval symbol))))

(def method-holder-symbol-to-left
     (fn [symbol]
       (assert (symbol? symbol))
       (:__left_symbol__ (eval symbol))))

(def lineage-1
     (fn [symbol so-far]
       (cond (nil? symbol)
             so-far

             (names-module-stub? symbol)
             (lineage-1 (method-holder-symbol-above symbol)
                        (concat (lineage (method-holder-symbol-to-left symbol))
                                so-far))

             :else 
             (lineage-1 (method-holder-symbol-above symbol)
                        (cons symbol so-far)))))

