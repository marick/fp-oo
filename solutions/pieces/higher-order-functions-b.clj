
;;; 11

(def lift
     (fn [function-to-compose-with-another]
       (fn [function-whose-return-value-should-be-changed]
         (fn [& args]
           (function-to-compose-with-another
             (apply function-whose-return-value-should-be-changed args))))))
