(ns solutions.ts-asteroids
  (:use midje.sweet))

(load-file "solutions/pieces/asteroids-1.clj")

(fact
  (collide (assoc (ship "Space Beagle") :speed 3)
           (assoc (ship  "Rim Griffon") :speed 8))
  => "The Rim Griffon smashes through the Space Beagle!"

  (collide (ship "Space Beagle") (asteroid "Malse"))
  => "Asteroid Malse smashes the Space Beagle!"

  (collide (asteroid "Malse") (ship "Space Beagle"))
  => "Asteroid Malse smashes the Space Beagle!")

;;; 

(load-file "solutions/pieces/asteroids-2-3.clj")

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

(load-file "solutions/pieces/asteroids-4.clj")


(fact 
  (collide (asteroid "Abbe") (gaussjammer "Lode Trader"))
  => "Asteroid Abbe smashes the Lode Trader!"

  (collide (gaussjammer "Lode Trader") (asteroid "Abbe"))
  => "Asteroid Abbe smashes the Lode Trader!"

  (collide (assoc (ship "Out of Band II") :speed 5)
           (assoc (gaussjammer "Lode Trader") :speed 55555))
  => "The poor Gaussjammer Lode Trader was smashed by the Out of Band II."

    (collide (assoc (gaussjammer "Lode Trader") :speed 55555)
             (assoc (ship "Out of Band II") :speed 5))
  => "The poor Gaussjammer Lode Trader was smashed by the Out of Band II.")


