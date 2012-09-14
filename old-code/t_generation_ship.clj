(ns sources.t-generation-ship
  (:use midje.sweet))

(load-file "sources/generation-ship.clj")


(fact 
  (collide (asteroid "Malse") (asteroid "Abbe") )
  => "Asteroids Malse and Abbe bounce harmlessly off each other."

  (collide (asteroid "Abbe") (gaussjammer "Lode Trader"))
  => "Asteroid Abbe smashes the Lode Trader!"

  (collide (gaussjammer "Lode Trader") (asteroid "Abbe"))
  => "Asteroid Abbe smashes the Lode Trader!"

  (collide (assoc (ship "Out of Band II") :speed 5)
           (assoc (gaussjammer "Lode Trader") :speed 55555))
  => "The poor Gaussjammer Lode Trader was smashed by the Out of Band II."

  (collide (assoc (gaussjammer "Lode Trader") :speed 55555)
           (assoc (ship "Out of Band II") :speed 5))
  => "The poor Gaussjammer Lode Trader was smashed by the Out of Band II."

  (collide (generation-ship "Yonada") (ship "Orion III"))
  => "Asteroid Yonada smashes the Orion III!"

  (collide (ship "Orion III") (generation-ship "Yonada") )
  => "Asteroid Yonada smashes the Orion III!"

  (collide (generation-ship "Yonada") (asteroid "Abbe") )
  => "Asteroid Abbe smashes the Yonada!"

  (collide (asteroid "Abbe") (generation-ship "Yonada") )
  => "Asteroid Abbe smashes the Yonada!")
    
(fact
  (type-description (ship "A")) => "ship"
  (type-description (asteroid "A")) => "asteroid"
  (type-description (gaussjammer "A")) => "Gaussjammer"
  (type-description (generation-ship "A")) => "ship")

