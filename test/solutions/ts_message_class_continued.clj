(ns solutions.ts-message-class-continued
   (:use midje.sweet))


(load-file "sources/consolidation.clj")

;;; You should replace this file with your own.
(load-file "solutions/message-class.clj")

(load-file "sources/message-class-exercises.clj")


;; These are the tests for code up to the last
;; exercise set. They should still pass.
(load-file "test/solutions/ruby-complete.clj")

;; Additional tests.

(fact
  (let [snooper (send-to Snooper :new)]
    (send-to snooper :snoop "an arg") => [:snoop 'Snooper ["an arg"] snooper]))


(fact
  (let [snooper (send-to SubSnooper :new)]
    (activate-method (send-to snooper :snoop "an arg"))
    => [:snoop 'Snooper ["an arg"] snooper]
    (send-to snooper :fail-dramatically) => (throws Error)
    (send-to snooper :to-string) => "Look! {:__left_symbol__ SubSnooper}"))
  
