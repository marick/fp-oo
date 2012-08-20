(ns solutions.ts-primes
  (:use midje.sweet))

(load-file "solutions/primes.clj")

;; Exercise 1

(fact
  (multiples 2)
  => [4 6 8 10 12 14 16 18 20 22 24 26 28 30 32 34 36 38 40 42 44 46 48 50 52 54
      56 58 60 62 64 66 68 70 72 74 76 78 80 82 84 86 88 90 92 94 96 98 100]
(multiples 3)
  => [6 9 12 15 18 21 24 27 30 33 36 39 42 45 48 51 54 57 60 63 66 69 72 75 78 81
 84 87 90 93 96 99])

;; Exercise 2

(fact
  ((set nonprimes) 4) => truthy
  ((set nonprimes) 5) => falsey
  ((set nonprimes) 100) => truthy)

;; Exercise 3

(fact
  primes => [2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97])


