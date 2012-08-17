(ns solutions.ts-scheduling
  (:use midje.sweet)
  (:import java.util.Date))

(load-file "solutions/scheduling.clj")

(fact "courses which are full can be removed"
  (remove-full [{:spaces-left 0} {:spaces-left 2}])
  => [{:spaces-left 2}])

(facts "courses with no registrants"
  (let [courses [{:registered 1} {:registered 0}]]
    (fact "will be removed if instructors are used up"
      (remove-unbookable courses 1) => [{:registered 1}])
    (fact "will be retained if instructors are not"
    (remove-unbookable courses 2) => courses)))

(facts ":already-in? courses can be forced into the set"
  (add-back-already-in [:existing] [{:already-in? true}]) => #{:existing {:already-in? true}}
  (add-back-already-in [:existing] [{:already-in? false}]) => #{:existing}
  (add-back-already-in [{:already-in? true}] [{:already-in? true}]) => #{ {:already-in? true} })

(facts "assertions on add-back-already-in data"
  (asserty-add-back-already-in-1 [{:already-in? true}] [{:already-in? false}])
  => #{ {:already-in? true}} 
  (asserty-add-back-already-in-1 [{:already-in? true}] [{:already-in? false :foo 1}])
  => (throws java.lang.AssertionError)

  (asserty-add-back-already-in-2 [{:already-in? true}] [{:already-in? false}])
  => #{ {:already-in? true}} 
  (asserty-add-back-already-in-2 [{:already-in? true}] [{:already-in? true :foo 1}])
  => (throws java.lang.AssertionError))

