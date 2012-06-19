;;;; 1


(def classifier
     (fn [thing]
       (cond (string? thing) :string
             (sequential? thing) :sequential
             :else :object)))

(defmulti add classifier)

(defmethod add :sequential [s]
  (fn [document]
    (concat document s)))

(defmethod add :string [s]
  (add (words s)))

(defmethod add :object [o]
  (add [o]))

(def edit
     (fn [new-value]
       (fn [document]
         ((add new-value) (drop-last 1 document)))))


;;;; 2 

(def changes [(add 1) (add "two words") (edit "three") (add [4 5])])


(def make-changes
     (fn [changes document]
       (let [all-changes (apply comp (reverse changes))]
         (all-changes document))))


;;;; 3

(use 'midje.sweet)
(def new-tail
     (fn [map key new-value]
       (assoc map key (concat (key map) new-value))))

(fact
  (new-tail {:a [1 2]} :a [3 4]) => {:a [1 2 3 4]})

;;;; 4


;; Here's a less aggressive removal-of-duplication
;; The seq-constructor knows the data describing the
;; change from its original call. It gets the current
;; value of the sequence from newly-constructed.

(def newly-constructed
     (fn [map key seq-constructor]
       (assoc map key (seq-constructor (key map)))))

(def drop-tail
     (fn [map key count]
       (newly-constructed map key
                          (fn [old] (drop-last count old)))))

(fact "drops some number of elements from the end"
  (drop-tail {:a [11 22 33]} :a 1) => {:a [11 22]})


(def flip-last
     (fn [map key new-value]
       (newly-constructed map key
                          (fn [old] (concat (drop-last old) [new-value])))))

(fact "changes the last element to a new value"
  (flip-last {:a [1 2 3]} :a "three") => {:a [1 2 "three"]})



;;; Here's a more aggressive generalization that relieves the using
;;; function from having to pass the map and key to
;;; the helper function. The helper function passes both the original
;;; value and the value-describing-the-change.

(defn new-maker [special]
  (fn [map key arg]
    (assoc map key (special (key map) arg))))

(def new-tail (new-maker concat))

(fact "adds multiple elements to the end"
  (new-tail {:a [1]} :a [2 3]) => {:a [1 2 3]})

(def drop-tail
     (new-maker (fn [old count] (drop-last count old))))

(fact "drops some number of elements from the end"
  (drop-tail {:a [11 22 33]} :a 1) => {:a [11 22]})

(def flip-last
     (fn [map key new-value]
       (new-tail (drop-tail map key 1)
                 key [new-value])))

(fact "changes the last element to a new value"
  (flip-last {:a [1 2 3]} :a "three") => {:a [1 2 "three"]})

;;; 5

(def empty-document {:contents []
                     :undo "You should not be able to undo past the beginning of time."})

;; For testing
(def one-element-document
     (fn [elt] ((add elt) empty-document)))

(defmethod add :sequential [s]
  (fn [document]
    (assoc (new-tail document :contents s)
           :undo document)))

(fact "elements are concatenated to the end"
  ( (add [2 3]) (one-element-document 1))
  => {:contents [1 2 3] :undo (one-element-document 1)})

;; The other add methods don't have to be redefined because they just
;; call this version.

(fact "words from string are concatenated to the end"
  (  (add "2 3") (one-element-document 1))
  => {:contents [1 "2" "3"] :undo (one-element-document 1)})

(fact "single elements are added to the end"
  (  (add 'ELT) (one-element-document 1))
  => {:contents [1 'ELT] :undo (one-element-document 1)})


(def edit
     (fn [new-value]
       (fn [document]
         (assoc (flip-last document :contents new-value)
                :undo document))))

(fact "edit replaces the last element with a new one"
  ( (edit 33) (one-element-document 1))
  => {:contents [33] :undo (one-element-document 1)})
  

(def undo
     (fn [document] (:undo document)))

(fact "undo restores previous document"
  (let [original (one-element-document 1)
        changed ( (add [2 3]) original)]
    (undo changed) => original))
    

(def changes
     [(add 1) (add "two words") (edit "three") (add [4 5])
      undo undo])


(fact "end-to-endish"
  (:contents (make-changes changes empty-document))
  => [1 "two" "words"])

  
