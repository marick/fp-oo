(ns sources.t-asteroids
  (:use midje.sweet))

(load-file "sources/asteroids.clj")


(def rim-griffon-speed 1000)
(def load-trader-speed 30)
(def malse-speed 0.1)

(def rim-griffon (make ::starship {:name "Rim Griffon" :speed rim-griffon-speed}))
(def malse (make ::asteroid {:name "Malse" :speed malse-speed :gravitational-pull 1}))
(def lode-trader (make ::gaussjammer {:name "Lode Trader" :speed 30,
                                      :drive-type "Ehrenhaft"}))


(fact "nudging"
  (:speed (nudge rim-griffon 5)) => (+ rim-griffon-speed 5)
  (:speed (nudge malse 5)) => malse-speed)
            
(fact "description"
  (description rim-griffon) => "the spritely Rim Griffon"
  (description malse) => "the asteroid Malse"
  (description lode-trader) => "the spritely Lode Trader")
  
(fact "gravitational pull"
  (gravitational-pull rim-griffon) => (throws Exception)
  (gravitational-pull malse) => (:gravitational-pull malse))

(fact "drive type"
  (drive-type rim-griffon) => "Mannschenn"
  (drive-type malse) => (throws Exception)
  (drive-type lode-trader) => "Ehrenhaft")

(fact "collide in object-oriented style"
  (collide-oo rim-griffon (with-speed 0 rim-griffon)) => [rim-griffon rim-griffon]
  (collide-oo rim-griffon malse) => [(stopped rim-griffon) malse]
  (collide-oo malse rim-griffon) => [malse (stopped rim-griffon)]
  (collide-oo malse (with-speed 8 malse)) => [malse (with-speed 8 malse)])




(fact "collide in multiple dispatch style"
  (collide rim-griffon (with-speed 0 rim-griffon)) => [rim-griffon rim-griffon]
  (collide rim-griffon malse) => [(stopped rim-griffon) malse]
  (collide malse rim-griffon) => [malse (stopped rim-griffon)]
  (collide malse (with-speed 8 malse)) => [malse (with-speed 8 malse)]

  (collide lode-trader rim-griffon) => [(stopped lode-trader) rim-griffon]
  (collide rim-griffon lode-trader) => [rim-griffon (stopped lode-trader)]
  (collide malse lode-trader) => [malse (stopped lode-trader)]
  (collide lode-trader lode-trader) => [(stopped lode-trader) (stopped lode-trader)])

(fact "nudging a lode trader"
  (:speed (nudge lode-trader 1)) => (* 2 (inc (:speed lode-trader))))


