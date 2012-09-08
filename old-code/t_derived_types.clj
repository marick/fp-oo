(ns sources.t-derived-types
  (:use midje.sweet))

(load-file "sources/derived-types.clj")

(fact
  (collide (assoc (ship "Space Beagle") :speed 3)
           (assoc (ship  "Rim Griffon") :speed 8))
  => "The Rim Griffon smashes through the Space Beagle!"

  (collide (ship "Space Beagle") (asteroid "Malse"))
  => "Asteroid Malse smashes the Space Beagle!"

  (collide (asteroid "Malse") (ship "Space Beagle"))
  => "Asteroid Malse smashes the Space Beagle!"

  (collide (asteroid "Abbe") (asteroid "Malse"))
  => "Asteroids Abbe and Malse bounce harmlessly off each other.")
