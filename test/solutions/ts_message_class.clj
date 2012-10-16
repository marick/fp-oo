(ns solutions.ts-message-class
   (:use midje.sweet))

(load-file "sources/consolidation.clj")
(load-file "solutions/pieces/message-class-1.clj")
(load-file "test/solutions/ruby-complete.clj")

(load-file "sources/message-class-exercises.clj")


(fact
  (let [snooper (send-to Snooper :new)]
    (send-to snooper :snoop "an arg") => [:snoop 'Snooper ["an arg"] snooper]))


(load-file "solutions/pieces/message-class-2.clj")
(load-file "test/solutions/ruby-complete.clj")

(fact
  (let [snooper (send-to SubSnooper :new)]
    (activate-method (send-to snooper :snoop "an arg"))
    => [:snoop 'Snooper ["an arg"] snooper]
    (send-to snooper :fail-dramatically) => (throws Error)
    (send-to snooper :to-string) => "Look! {:__left_symbol__ SubSnooper}"))
  



(load-file "solutions/pieces/message-class-3.clj")
(load-file "test/solutions/ruby-complete.clj")

(fact
  (let [snooper (send-to Snooper :new)]
    (send-to snooper :snoop "an arg") => [:snoop 'Snooper ["an arg"] snooper]))


(fact
  (let [snooper (send-to SubSnooper :new)]
    (activate-method (send-to snooper :snoop "an arg"))
    => [:snoop 'Snooper ["an arg"] snooper]
    (send-to snooper :fail-dramatically) => (throws Error)
    (send-to snooper :to-string) => "Look! {:__left_symbol__ SubSnooper}"))
  

(load-file "solutions/pieces/message-class-4.clj")
(load-file "test/solutions/ruby-complete.clj")

(def criminal (send-to Criminal :new))
(def police (send-to Police :new "Biggles"))
(send-to criminal :taunt police)

(load-file "solutions/pieces/message-class-5.clj")
(load-file "test/solutions/ruby-complete.clj")

(fact
  (let [traceful (send-to (send-to Bottom :new "one") :chained-message 1)
        one (send-to Bottom :new "one")
        two (send-to Bottom :new "two")
        trace (send-to traceful :trace)]
    (nth trace 0) => {:target one, :name :chained-message, :args [1]
                      :holder-name 'Bottom, :super-count 0}
    (nth trace 1) => {:target two, :name :secondary-message, :args [10]
                      :holder-name 'Bottom, :super-count 0}
    (nth trace 2) => {:target two, :name :secondary-message, :args [10]
                      :holder-name 'Middle, :super-count 1}
    (nth trace 3) => {:target two, :name :secondary-message, :args [100]
                      :holder-name 'Top, :super-count 2}
    (nth trace 4) => {:target two, :name :tertiary-message, :args [1000]
                      :holder-name 'Middle, :super-count 0}
    (nth trace 5) => {:target two, :name :tertiary-message, :args [10000]
                      :holder-name 'Top, :super-count 1}

    ;; Just check that this doesn't blow up.
    (friendly-trace trace)))


