(install (basic-class 'Klass,
                      :left 'MetaKlass,
                      :up 'Anything,
                      {
                       :new
                       (fn [class & args]
                         (let [seeded {:__class_symbol__ (:__own_symbol__ class)}]
                           (apply-message-to class seeded :add-instance-values args)))

                       :to-string                          ; <<<<= new
                       (fn [class]
                         (str "class " (:__own_symbol__ class)))
                      }))



