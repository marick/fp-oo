;;; Exercise 3

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
                               :__left_symbol__ module-name}]
                     ;; This now points up to the included stub:
                     (install (assoc this :__up_symbol__ stub-name))
                     ;; And the included stub points to the real module:
                     (install stub)))
               }))

