;;; Exercise 3

;; Before:
;; (send-to MetaAnything :to-string)     ; Stack overflow error, sometimes repl crash
;; (send-to MetaPoint :new)              ; Stack overflow error, sometimes repl crash

;; Reasoning:
;; * Any object in the system must have a class.
;;
;; * Any object's class must be descended from Anything so that it has
;;   access to methods like `:class` and `:method-missing`.
;;
;; * MetaAnything is an object.
;;
;; * Therefore, its class link must point to Anything or something
;;   that's descended from it.
;;
;; * There are two choices: Anything or MetaAnything. 
;;     * If MetaAnything were its own class, you'd be able to send
;;       MetaAnything :new. But there's no point to having the
;;       ability to make new MetaAnythings. It seems like trying to
;;       do that should be an error -- a :method-missing error.
;;
;;      * That leaves Anything.
;;
;; The same thinking implies that MetaPoint's class should be Anything.

(def MetaAnything
     (assoc MetaAnything :__class_symbol__ 'Anything))

(def MetaPoint
     (assoc MetaPoint :__class_symbol__ 'Anything))

;; After
(send-to MetaAnything :to-string)

(try (send-to MetaPoint :new)
(catch Error e))          ; a method-missing error
