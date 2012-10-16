
;; For exercise 2

(send-to Klass :new
         'Snooper 'Anything
         {
          :snoop
          (fn [& args]
            [
             (send-to *active-message* :name)
             (send-to *active-message* :holder-name)
             (send-to *active-message* :args)
             (send-to *active-message* :target)
            ])
          }
         {})

(def snooper (send-to Snooper :new))
(prn (send-to snooper :snoop "an arg")) ;  => [:snoop 'Snooper ("an arg") snooper])


;; For exercise 3

(comment
  ;; Here are the two methods you are to make work.
  ;; They're commented out so that they don't break my
  ;; automated tests.
(def repeat-to-super
     (fn []
       (activate-method (send-to *active-message* :move-up))))
       
(def send-super
     (fn [& args]
       (activate-method (assoc (send-to *active-message* :move-up)
                          :args args))))

  ;; Since you're replacing `using-method-above`, it's useful to
  ;; define it to something that will blow up if there's still a
  ;; stray call to it.

(def using-message-above :ensure-that-the-function-can-no-longer-be-called)

)


(send-to Klass :new
         'SubSnooper 'Snooper
         {
          :snoop
          (fn [& args]
            ;; Need to try `move-up`
            ;; in the context of a method
            ;; that shadows a method in the
            ;; superclass.
            (send-to *active-message* :move-up))

          :fail-dramatically
          (fn []
            (send-to *active-message* :move-up))

          :to-string
          (fn []
            (str "Look! " (repeat-to-super)))
          }

         {})

(comment
  ;; The following is commented out because of the way my 
  ;; automated tests work. It's in a block comment to make
  ;; it easy for you to copy it into a REPL

(def snooper (send-to SubSnooper :new))
(prn (send-to snooper :snoop "an arg")) ; => {:name :snoop,
                                        ;     :holder-name Snooper,
                                        ;     :args ("an arg"),
                                        ;     :target {:__left_symbol__ SubSnooper},
                                        ;     :__left_symbol__ Message}

(prn (activate-method (send-to snooper :snoop "an arg"))) ; => [:snoop Snooper ("an arg")
                                                         ;     {:__left_symbol__ SubSnooper}]
(send-to snooper :fail-dramatically)    ; throws Error
(prn (send-to snooper :to-string))      ; =>  "Look! {:__left_symbol__ SubSnooper}"

)

;; ;; For exercise 6

(send-to Klass :new
         'Criminal 'Anything
         {
          :taunt
          (fn [copper]
            (let [taunt "Ha ha copper! You'll never catch me!"]
              (println "Criminal:" taunt)
              (send-to copper :be-taunted taunt)))

          :give-yourself-up
          (fn []
            (let [confession "It's a fair cop, but society is to blame."]
              (println "Criminal:" confession)))
          }
         {})

(send-to Klass :new
         'Police 'Anything
         {
          :add-instance-values
          (fn [name]
            (assoc this :name name))

          :name (fn [] (:name this))
          
          :be-taunted
          (fn [taunt]
            (let [evildoer (send-to *active-message* :sender)]
              (cl-format true "Detective ~A: Wot? Who?~%"
                              (send-to this :name))
              (println "<nab/>")
              (send-to evildoer :give-yourself-up)))
          }
         {})

(comment

(def criminal (send-to Criminal :new))
(def police (send-to Police :new "Biggles"))
(send-to criminal :taunt police)
)

;; ;; For Exercise 7

(declare Bottom Middle Top)

(send-to Klass :new
         'Bottom 'Middle
         {
          :add-instance-values
          (fn [value]
            (assoc this :value value))

          :chained-message (fn [n]
                            (send-to (send-to Bottom :new "two")
                                     :secondary-message (* 10 n)))
          :secondary-message (fn [n] (repeat-to-super))
         }
         {})

(send-to Klass :new
         'Middle 'Top
         {
          :secondary-message (fn [n] (send-super (* 10 n)))
          :tertiary-message (fn [n] (send-super (* 10 n)))
         }
         {})


(send-to Klass :new
         'Top 'Anything
         {
          :secondary-message (fn [n] (send-to this :tertiary-message (* 10 n)))
          :tertiary-message (fn [n] *active-message*)
          }
         {})

(comment

;; It might be helpful to start with simple cases and work up.
(def traceful (send-to (send-to Top :new) :tertiary-message 1))
traceful  
(send-to traceful :trace)

(def traceful (send-to (send-to Middle :new) :tertiary-message 1))
traceful  
(pprint (send-to traceful :trace))

(def traceful (send-to (send-to Bottom :new "one") :chained-message 1))
traceful  
(pprint (send-to traceful :trace))

)

;; `friendly-trace can take the output of one of the above lines and
;; print it in a more pleasing form. It shows a few new Clojure
;; features.

;; Optional arguments in Clojure are done in a somewhat awkward way, by
;; giving a list of parameter-list-and-body pairs:
;;
;; (fn ([arglist1] body1...) ([arglist2] body2...))
;;
;; Typically versions with fewer parameters add defaults and call the
;; version with the longest parameter.
;;
;;
;; This example also uses "destructuring args" in a typical way. Consider this
;; `let` expression:
;;
;; (let [ [head & tail] (list 1 2 3)] ...body...)
;;
;; The embeddded vector `[head & tail]` says that the single argument
;; is expected to be a sequence. Its `first` element is bound to `head`, and the
;; `rest` of the sequence is bound to `tail`.


(def target-to-name-map
     "In a trace, we want to print a simple name for the
      target of a message, rather than the default representation.
      We could override the `:to-string` method, but I'm thinking that
      how you want to see an object print is situation-dependent. In
      the case of a trace, what *I* want is a little table of
      contents that shows each separate instance once and gives it a
      name that's thereafter used in the trace. This produces a
      map that's needed to do that."
     (fn
       ([targets]
            (target-to-name-map targets {} {}))

       ([[target & remainder] class-instance-counts result]
            (cond (nil? target)
                  result
                  
                  (contains? result target)
                  (target-to-name-map remainder class-instance-counts result)
                  
                  :else
                  (let [target-class-name (send-to target :class-name)
                        name-suffix (inc (or (get class-instance-counts target-class-name)
                                             0))
                        short-name (str "a-" target-class-name "-" name-suffix)]
                    (target-to-name-map remainder
                                          (assoc class-instance-counts
                                            target-class-name name-suffix)
                                          (assoc result
                                            target short-name)))))))

;; If you use a sequence function with a map, each key-value pair is delivered to
;; as a two-element sequence:
;;
;; user=> (map identity {:a 1 :b 2})
;; ([:a 1] [:b 2])


(def table-of-contents
     "The 'table of contents' displays the mapping between message targets
      and their names. The table is in alphabetical order of names. That
      groups instances of a particular class together, in the order they were
      created."
     (fn [namings]
       (map (fn [[target name]] (str name " =stands=for=> " target))
            (sort-by second namings))))
                     

(def friendly-trace
     "Convert a raw trace into a formatted one, with a
      table of contents at both ends. Delegations to `super`
      are indented."
     (fn [trace]
       (let [namings (target-to-name-map (map :target trace))
             renamed-trace (map (fn [elt]
                                  (assoc elt
                                         :target (get namings (:target elt))))
                                trace)

             format-one-element
             (fn [elt]
               (if (zero? (:super-count elt))
                 (cl-format nil
                            "(send-to ~A ~A.~A ~{~A~})"
                            (:target elt) (:holder-name elt) (name (:name elt)) (:args elt))
                 (cl-format nil
                            "    | delegate ~A to ~A"
                            (:args elt) (:holder-name elt))))]

         (dorun (map println (table-of-contents namings)))
         (println "-------------")
         (dorun (map println (map format-one-element renamed-trace)))
         (println "-------------")
         (dorun (map println (table-of-contents namings))))))
