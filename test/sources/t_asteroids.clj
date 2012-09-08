(ns sources.t-asteroids
  (:use midje.sweet))

(load-file "sources/pieces/asteroids-1.clj")
(load-file "sources/pieces/asteroids-1.clj")

(fact
  (collide (assoc (ship "Space Beagle") :speed 3)
           (assoc (ship  "Rim Griffon") :speed 8))
  => "The Rim Griffon smashes through the Space Beagle!")

(load-file "sources/pieces/asteroids-2.clj")

(fact
  ( (names-ordered-by :name) [{:name "z"} {:name "a"}]) => ["a" "z"])



(fact
  (collide (assoc (ship "Space Beagle") :speed 3)
           (assoc (ship  "Rim Griffon") :speed 8))
  => "The Rim Griffon smashes through the Space Beagle!")


