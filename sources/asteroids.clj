(use '[clojure.pprint :only [cl-format]])

(load-file "sources/generic.clj")


;;; Objects of interest
(def make
     (fn [type value-map]
       (with-meta value-map {:type type})))

(def rim-griffon (make ::starship {:name "Rim Griffon", :speed 1000}))
(def malse (make ::asteroid {:name "Malse", :speed 0.1, :gravitational-pull 0.5}))

;;; A hierarchy

(derive ::asteroid ::named)
(derive ::starship ::named)

;;; OO-style single arguments

(def oo-style 
  (fn [this & args] (type this)))

;; Nudge
(defgeneric nudge oo-style)

(def with-speed
     (fn [speed thing]
       (assoc thing :speed speed)))

(def stopped
     (fn [thing]
       (with-speed 0 thing)))

(def speed-up-by
     (fn [delta thing]
       (with-speed (+ delta (:speed thing)) thing)))

(defspecialized nudge ::starship
  (fn [this delta]
    (speed-up-by delta this)))

(defspecialized nudge ::asteroid
  (fn [this delta]
    this))

;; Description
(defgeneric description oo-style)
(defspecialized description ::named
         (fn [this] (str "the " (name (type this)) " " (:name this))))

(defspecialized description ::starship
         (fn [this] (str "the spritely " (:name this))))


;; Gravitational pull
(defgeneric gravitational-pull oo-style)

(defspecialized gravitational-pull ::asteroid
  (fn [this] (or (:gravitational-pull this) 0.0)))


;; Drive type
(defgeneric drive-type oo-style)

(defspecialized drive-type ::starship
  (fn [this] (or (:drive-type this) "Mannschenn")))

;; Collide in OO style

(defgeneric collide-oo oo-style)
(prefer-method collide-oo ::asteroid ::starship)
(defgeneric collide-with-starship oo-style)
(prefer-method collide-with-starship ::asteroid ::starship)
(defgeneric collide-with-asteroid oo-style)
(prefer-method collide-with-asteroid ::asteroid ::starship)

(defspecialized collide-oo ::starship
  (fn [this other]
    (collide-with-starship other this)))

(defspecialized collide-oo ::asteroid
  (fn [this other]
    (collide-with-asteroid other this)))


(defspecialized collide-with-starship ::starship
  (fn [& starships]
    (let [speed (apply max (map :speed starships))]
      (map (partial with-speed speed) starships))))
           
(defspecialized collide-with-starship ::asteroid
  (fn [asteroid starship]
    [(stopped starship) asteroid]))

(defspecialized collide-with-asteroid ::asteroid
  (fn [& asteroids] (reverse asteroids)))

(defspecialized collide-with-asteroid ::starship
  (fn [starship asteroid]
    [asteroid (stopped starship)]))



;;; Collide with multi-arg dispatch

(defgeneric collide (fn [one two] [(type one) (type two)]))
         
(defspecialized collide [::asteroid ::asteroid]
  (fn [& asteroids] asteroids))

(defspecialized collide [::asteroid ::starship]
  (fn [asteroid starship]
    [asteroid (stopped starship)]))

(defspecialized collide [::starship ::asteroid]
  (fn [starship asteroid]
    [(stopped starship) asteroid]))

(defspecialized collide [::starship ::starship]
  (fn [& starships]
    (let [speed (apply max (map :speed starships))]
      (map (partial with-speed speed) starships))))



;;; This is the solution for an exercise I didn't put in the text:

;; Gaussjammers were an early form of interstellar starships. That is:

(derive ::gaussjammer ::starship)

;; They are extremely delicate. If they collide with *anything*, they are
;; destroyed and their velocity drops to zero. Implement this behavior
;; with two `defspecialized` expressions.

(def lode-trader (make ::gaussjammer {:name "Lode Trader" :speed 30}))

;; Now *this* is annoying. Using `:default` doesn't work in the "anything" part of the
;; specializations. I have to create a shared supertype just for this. Or am I getting
;; something wrong?

(derive ::starship ::thing)
(derive ::asteroid ::thing)

;; And then [::gaussjammer ::gaussjammer] is seen as ambiguous between
;; [::gaussjammer ::thing] and [::starship ::starship::]???
(prefer-method collide [::gaussjammer ::thing] [::starship ::starship])
(prefer-method collide [::thing ::gaussjammer] [::starship ::starship])
(prefer-method collide [::thing ::gaussjammer] [::asteroid ::starship])

(defspecialized collide [::thing ::gaussjammer]
  (fn [thing gaussjammer]
    [thing (stopped gaussjammer)]))

(defspecialized collide [::gaussjammer ::thing]
  (fn [gaussjammer thing]
    [(stopped gaussjammer) thing]))

(defspecialized collide [::gaussjammer ::gaussjammer]
  (fn [one two]
    [(stopped one) (stopped two)]))


;;; An example of `call-super`. It assumes only one parent.

;;; A gaussjammer gets a big speedup from being nudged.

(def call-super
     (fn [function this & args]
       (let [real-type (type this)
             super-type (first (parents real-type))
             object-as-super-type (vary-meta this assoc :type super-type)
             resulting-object (apply function object-as-super-type args)]
         (prn resulting-object)
         (vary-meta resulting-object
                     assoc :type real-type))))

(defspecialized nudge ::gaussjammer
  (fn [this speed]
    (let [what-would-happen-to-a-normal-ship (call-super nudge this speed)]
      (speed-up-by (:speed what-would-happen-to-a-normal-ship) what-would-happen-to-a-normal-ship))))
