(ns solutions.ts-shapely
  (:use midje.sweet))

(load-file "solutions/pieces/shapely-1.clj")

(fact 
  (pattern-classification 1 :etc...) => ::literal
  (pattern-classification :a :etc...) => ::literal
  (pattern-classification "foo" :etc...) => ::literal)

(fact
  (match-one? 1 1) => true
  (match-one? 1 2) =>  false
  (match-one? '(1) '(1)) => true)

(fact 
  (match-map 1 1) => {})

(load-file "solutions/pieces/shapely-2.clj")

(fact 
  (pattern-classification 1 :etc...) => ::literal
  (pattern-classification :a :etc...) => ::literal
  (pattern-classification "foo" :etc...) => ::literal
  (pattern-classification 'n :etc...) => ::symbol
  (pattern-classification 'title :etc...) => ::symbol
  )

(fact
  (match-one? 1 1) => true
  (match-one? 1 2) =>  false
  (match-one? '(1) '(1)) => true
  (match-one? 'n 3) => true
  )

(fact 
  (match-map 1 1) => {}
  (match-map 'a 1) => '{a 1}
  )


(load-file "solutions/pieces/shapely-3.clj")

(fact 
  (pattern-classification 1 :etc...) => ::literal
  (pattern-classification :a :etc...) => ::literal
  (pattern-classification "foo" :etc...) => ::literal
  (pattern-classification 'n :etc...) => ::symbol
  (pattern-classification 'title :etc...) => ::symbol
  (pattern-classification '[n] :etc...) => ::nested
  (pattern-classification '[so-far n] :etc...) => ::nested
  )

(fact
  (match-one? 1 1) => true
  (match-one? 1 2) =>  false
  (match-one? '(1) '(1)) => true
  (match-one? 'n 3) => true
  (match-one? '[1] 1) => false
  (match-one? '[1] [2]) => false
  (match-one? '[1] [1]) => true
  (match-one? '[a] [1]) => true
  (match-one? '[1] [1 2]) => false
  (match-one? '[1 2] [1]) => false
  (match-one? '[[a] b 3] [[1] 2 3]) => true
  (match-one? '[] []) => true
  (match-one? '[1] '(1)) => true

  ;; old duplicates
  (match-one? [1 2] 2) => false
  (match-one? [1 2] [1]) => false
  (match-one? [1 2] [1 2]) => true
  (match-one? '[1 n] [1 2]) => true
  (match-one? '[1 n] [1 2]) => true
  (match-one? '[1 n] [1 2 3]) => false
  (match-one? '[1 m [n]] [1 2 [3]]) => true
  (match-one? '[1 m [n]] [1 2 [3]]) => true
  )

(fact 
  (match-map 1 1) => {}
  (match-map 'a 1) => '{a 1}
  (match-map [1] [1]) => {}
  (match-map '[[a] b 3] [[1] 2 3]) => '{a 1, b 2}

  ;; old duplicates
  (match-map '[a 1 b] ["foo" 1 "bar"]) => '{a "foo", b "bar"}
  (match-map '[[a] 1 [[b 1]]] [["foo"] 1 [["bar" 1]]]) => '{a "foo", b "bar"}
  )

(load-file "solutions/pieces/shapely-4.clj")

(fact 
  (pattern-classification 1 :etc...) => ::literal
  (pattern-classification :a :etc...) => ::literal
  (pattern-classification "foo" :etc...) => ::literal
  (pattern-classification 'n :etc...) => ::symbol
  (pattern-classification 'title :etc...) => ::symbol
  (pattern-classification '[n] :etc...) => ::nested
  (pattern-classification '[so-far n] :etc...) => ::nested
  (pattern-classification '[so-far & rest] :etc...) => ::nested-with-rest
  )

(fact
  (match-one? 1 1) => true
  (match-one? 1 2) =>  false
  (match-one? '(1) '(1)) => true
  (match-one? 'n 3) => true
  (match-one? '[1] 1) => false
  (match-one? '[1] [2]) => false
  (match-one? '[1] [1]) => true
  (match-one? '[a] [1]) => true
  (match-one? '[1] [1 2]) => false
  (match-one? '[1 2] [1]) => false
  (match-one? '[[a] b 3] [[1] 2 3]) => true
  (match-one? '[] []) => true
  (match-one? '[1] '(1)) => true
  (match-one? '[1 & rest] [1]) => true
  (match-one? '[1 2 & rest] [1]) => false
  (match-one? '[1 2 & rest] [1 2 3]) => true
  (match-one? '[& rest] []) => true
  (match-one? '[& rest] [1]) => true
  (match-one? '[[a & rest] b 3] [[1 "HI!"] 2 3]) => true
  (match-one? '[[a & rest] b 3] [[1 "HI!"] 2 3]) => true
  (match-one? '[[a & rest] b 3 & rest2] [[1 "HI!"] 2 3 "THERE"]) => true


  ;; old duplicates
  (match-one? [1 2] 2) => false
  (match-one? [1 2] [1]) => false
  (match-one? [1 2] [1 2]) => true
  (match-one? '[1 n] [1 2]) => true
  (match-one? '[1 n] [1 2]) => true
  (match-one? '[1 n] [1 2 3]) => false
  (match-one? '[1 m [n]] [1 2 [3]]) => true
  (match-one? '[1 m [n]] [1 2 [3]]) => true
  (match-one? '[1 2 & rest] [1]) => false
  (match-one? '[1 n & rest] [1]) => false
  (match-one? '[1 2 & rest] [1 2]) => true
  (match-one? '[1 2 & rest] [1 3]) => false
  (match-one? '[1 2 & rest] [1 2 3]) => true
  (match-one? '[1 n & rest] [1 2 3]) => true
  )

(fact 
  (match-map 1 1) => {}
  (match-map 'a 1) => '{a 1}
  (match-map [1] [1]) => {}
  (match-map '[[a] b 3] [[1] 2 3]) => '{a 1, b 2}
  (match-map '[1 & rest] [1]) => '{rest ()}
  (match-map '[1 2 & rest] [1 2 3]) => '{rest (3)}
  (match-map '[& rest] []) => '{rest ()}
  (match-map '[& rest] [1]) => '{rest (1)}
  (match-map '[[a & rest] b 3] [[1 "HI!"] 2 3]) => '{a 1, rest ("HI!"), b 2}
  (match-map '[[a & rest] b 3 & rest2] [[1 "HI!"] 2 3 "THERE"]) => '{a 1, rest ("HI!"), b 2, rest2 ("THERE")}

  ;; old duplicates
  (match-map '[a 1 b] ["foo" 1 "bar"]) => '{a "foo", b "bar"}
  (match-map '[[a] 1 [[b 1]]] [["foo"] 1 [["bar" 1]]]) => '{a "foo", b "bar"}
  (match-map '[a 2 & rest] [1 2]) => '{a 1, rest []}
  (match-map '[a 2 & rest] [1 2 3]) => '{a 1, rest [3]}
  (match-map '[[a & b] 2 & rest] [[1 2] 2 3 4]) => '{a 1, b [2] rest [3 4]}
  )



(load-file "solutions/pieces/shapely-5.clj")

(fact 
  (pattern-classification 1 :etc...) => ::literal
  (pattern-classification :a :etc...) => ::literal
  (pattern-classification "foo" :etc...) => ::literal
  (pattern-classification 'n :etc...) => ::symbol
  (pattern-classification 'title :etc...) => ::symbol
  (pattern-classification '[n] :etc...) => ::nested
  (pattern-classification '[so-far n] :etc...) => ::nested
  (pattern-classification '[so-far & rest] :etc...) => ::nested-with-rest
  (pattern-classification '(:in [1 2]) :etc...) => ::choice
  (pattern-classification '(:in [1 2] :bind n) :etc...) => ::choice
  )

(fact
  (match-one? 1 1) => true
  (match-one? 1 2) =>  false
  (match-one? '(1) '(1)) => true
  (match-one? 'n 3) => true
  (match-one? '[1] 1) => false
  (match-one? '[1] [2]) => false
  (match-one? '[1] [1]) => true
  (match-one? '[a] [1]) => true
  (match-one? '[1] [1 2]) => false
  (match-one? '[1 2] [1]) => false
  (match-one? '[[a] b 3] [[1] 2 3]) => true
  (match-one? '[] []) => true
  (match-one? '[1] '(1)) => true
  (match-one? '[1 & rest] [1]) => true
  (match-one? '[1 2 & rest] [1]) => false
  (match-one? '[1 2 & rest] [1 2 3]) => true
  (match-one? '[& rest] []) => true
  (match-one? '[& rest] [1]) => true
  (match-one? '[[a & rest] b 3] [[1 "HI!"] 2 3]) => true
  (match-one? '[[a & rest] b 3] [[1 "HI!"] 2 3]) => true
  (match-one? '[[a & rest] b 3 & rest2] [[1 "HI!"] 2 3 "THERE"]) => true
  (match-one? '(:in []) 1) => false
  (match-one? '(:in [1 2]) 1) => true
  (match-one? '(:in [1 2]) 2) => true
  (match-one? '(:in [1 2]) 3) => false
  (match-one? '(:bind n :in [1 2]) 3) => false
  (match-one? '(:in [1 n]) 'n) => true ; symbols in "in" are not treated as variables.


  ;; old duplicates
  (match-one? [1 2] 2) => false
  (match-one? [1 2] [1]) => false
  (match-one? [1 2] [1 2]) => true
  (match-one? '[1 n] [1 2]) => true
  (match-one? '[1 n] [1 2]) => true
  (match-one? '[1 n] [1 2 3]) => false
  (match-one? '[1 m [n]] [1 2 [3]]) => true
  (match-one? '[1 m [n]] [1 2 [3]]) => true
  (match-one? '[1 2 & rest] [1]) => false
  (match-one? '[1 n & rest] [1]) => false
  (match-one? '[1 2 & rest] [1 2]) => true
  (match-one? '[1 2 & rest] [1 3]) => false
  (match-one? '[1 2 & rest] [1 2 3]) => true
  (match-one? '[1 n & rest] [1 2 3]) => true
  (match-one? '(:in [1 2]) 1) => true
  (match-one? '(:in [1 2]) 2) => true
  (match-one? '(:in [1 2]) 3) => false
  )

(fact 
  (match-map 1 1) => {}
  (match-map 'a 1) => '{a 1}
  (match-map [1] [1]) => {}
  (match-map '[[a] b 3] [[1] 2 3]) => '{a 1, b 2}
  (match-map '[1 & rest] [1]) => '{rest ()}
  (match-map '[1 2 & rest] [1 2 3]) => '{rest (3)}
  (match-map '[& rest] []) => '{rest ()}
  (match-map '[& rest] [1]) => '{rest (1)}
  (match-map '[[a & rest] b 3] [[1 "HI!"] 2 3]) => '{a 1, rest ("HI!"), b 2}
  (match-map '[[a & rest] b 3 & rest2] [[1 "HI!"] 2 3 "THERE"]) => '{a 1, rest ("HI!"), b 2, rest2 ("THERE")}
  (match-map '(:in [1 2]) 2) => {}
  (match-map '(:in [1 2] :bind n) 2) => '{n 2}
  (match-map '(:bind n :in [1 2]) 2) => '{n 2}

  ;; old duplicates
  (match-map '[a 1 b] ["foo" 1 "bar"]) => '{a "foo", b "bar"}
  (match-map '[[a] 1 [[b 1]]] [["foo"] 1 [["bar" 1]]]) => '{a "foo", b "bar"}
  (match-map '[a 2 & rest] [1 2]) => '{a 1, rest []}
  (match-map '[a 2 & rest] [1 2 3]) => '{a 1, rest [3]}
  (match-map '[[a & b] 2 & rest] [[1 2] 2 3 4]) => '{a 1, b [2] rest [3 4]}
  )



(load-file "solutions/pieces/shapely-6.clj")

(fact 
  (pattern-classification 1 :etc...) => ::literal
  (pattern-classification :a :etc...) => ::literal
  (pattern-classification "foo" :etc...) => ::literal
  (pattern-classification 'n :etc...) => ::symbol
  (pattern-classification 'title :etc...) => ::symbol
  (pattern-classification '[n] :etc...) => ::nested
  (pattern-classification '[so-far n] :etc...) => ::nested
  (pattern-classification '[so-far & rest] :etc...) => ::nested-with-rest
  (pattern-classification '(:in [1 2]) :etc...) => ::choice
  (pattern-classification '(:in [1 2] :bind n) :etc...) => ::choice
  (pattern-classification '(:when odd?) :etc...) => ::guard
  (pattern-classification '(:when odd? :bind n) :etc...) => ::guard
  (pattern-classification '(:when odd? :bind n) :etc...) => ::guard
  )

(fact
  (match-one? 1 1) => true
  (match-one? 1 2) =>  false
  (match-one? '(1) '(1)) => true
  (match-one? 'n 3) => true
  (match-one? '[1] 1) => false
  (match-one? '[1] [2]) => false
  (match-one? '[1] [1]) => true
  (match-one? '[a] [1]) => true
  (match-one? '[1] [1 2]) => false
  (match-one? '[1 2] [1]) => false
  (match-one? '[[a] b 3] [[1] 2 3]) => true
  (match-one? '[] []) => true
  (match-one? '[1] '(1)) => true
  (match-one? '[1 & rest] [1]) => true
  (match-one? '[1 2 & rest] [1]) => false
  (match-one? '[1 2 & rest] [1 2 3]) => true
  (match-one? '[& rest] []) => true
  (match-one? '[& rest] [1]) => true
  (match-one? '[[a & rest] b 3] [[1 "HI!"] 2 3]) => true
  (match-one? '[[a & rest] b 3] [[1 "HI!"] 2 3]) => true
  (match-one? '[[a & rest] b 3 & rest2] [[1 "HI!"] 2 3 "THERE"]) => true
  (match-one? '(:in []) 1) => false
  (match-one? '(:in [1 2]) 1) => true
  (match-one? '(:in [1 2]) 2) => true
  (match-one? '(:in [1 2]) 3) => false
  (match-one? '(:bind n :in [1 2]) 3) => false
  (match-one? '(:in [1 n]) 'n) => true ; symbols in "in" are not treated as variables.
  (match-one? '(:when odd?) 1) => true
  (match-one? '(:when odd?) 2) => false
  (match-one? '(:when odd? :bind n) 1) => true
  (match-one? '(:when (partial = 1) :bind n) 1) => true
  (match-one? '(:bind n :when (partial = 1)) 1) => true
  (match-one? '(:bind n :when #{1 2 3}) 1) => true


  ;; old duplicates
  (match-one? [1 2] 2) => false
  (match-one? [1 2] [1]) => false
  (match-one? [1 2] [1 2]) => true
  (match-one? '[1 n] [1 2]) => true
  (match-one? '[1 n] [1 2]) => true
  (match-one? '[1 n] [1 2 3]) => false
  (match-one? '[1 m [n]] [1 2 [3]]) => true
  (match-one? '[1 m [n]] [1 2 [3]]) => true
  (match-one? '[1 2 & rest] [1]) => false
  (match-one? '[1 n & rest] [1]) => false
  (match-one? '[1 2 & rest] [1 2]) => true
  (match-one? '[1 2 & rest] [1 3]) => false
  (match-one? '[1 2 & rest] [1 2 3]) => true
  (match-one? '[1 n & rest] [1 2 3]) => true
  (match-one? '(:in [1 2]) 1) => true
  (match-one? '(:in [1 2]) 2) => true
  (match-one? '(:in [1 2]) 3) => false
  (match-one? '(:when odd?) 1) => true
  (match-one? '(:when odd? :bind n) 1) => true
  (match-one? '(:when odd?) 2) => false
  )

(fact 
  (match-map 1 1) => {}
  (match-map 'a 1) => '{a 1}
  (match-map [1] [1]) => {}
  (match-map '[[a] b 3] [[1] 2 3]) => '{a 1, b 2}
  (match-map '[1 & rest] [1]) => '{rest ()}
  (match-map '[1 2 & rest] [1 2 3]) => '{rest (3)}
  (match-map '[& rest] []) => '{rest ()}
  (match-map '[& rest] [1]) => '{rest (1)}
  (match-map '[[a & rest] b 3] [[1 "HI!"] 2 3]) => '{a 1, rest ("HI!"), b 2}
  (match-map '[[a & rest] b 3 & rest2] [[1 "HI!"] 2 3 "THERE"]) => '{a 1, rest ("HI!"), b 2, rest2 ("THERE")}
  (match-map '(:in [1 2]) 2) => {}
  (match-map '(:in [1 2] :bind n) 2) => '{n 2}
  (match-map '(:bind n :in [1 2]) 2) => '{n 2}
  (match-map '(:when odd?) 1) => {}
  (match-map '(:when odd? :bind n) 1) => '{n 1}
  (match-map '(:bind n :when odd?) 1) => '{n 1}



  ;; old duplicates
  (match-map '[a 1 b] ["foo" 1 "bar"]) => '{a "foo", b "bar"}
  (match-map '[[a] 1 [[b 1]]] [["foo"] 1 [["bar" 1]]]) => '{a "foo", b "bar"}
  (match-map '[a 2 & rest] [1 2]) => '{a 1, rest []}
  (match-map '[a 2 & rest] [1 2 3]) => '{a 1, rest [3]}
  (match-map '[[a & b] 2 & rest] [[1 2] 2 3 4]) => '{a 1, b [2] rest [3 4]}
  )
