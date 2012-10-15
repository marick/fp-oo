;;; Exercise 2

(install
 (invisible
  (method-holder 'MetaModule
               :left 'Klass
               :up 'Klass
               {
                :new
                (fn [this name methods]
                  (install
                   (method-holder name
                                  ;; We move left to find `:install`.
                                  ;; That means the class `Module` must be in
                                  ;; the "up" chain of the leftward object.
                                  ;; Since we don't have a need for a Meta
                                  ;; version of this new module, "left" can
                                  ;; point directly at `Module`. 
                                  :left 'Module

                                  ;; If `:up` pointed to, say, `Anything`, then
                                  ;; the methods from `Anything` would get
                                  ;; inserted into the inheritance chain earlier than
                                  ;; they would otherwise be, preventing other classes
                                  ;; from overriding them.
                                  :up nil
                                  
                                  methods)))
                })))

