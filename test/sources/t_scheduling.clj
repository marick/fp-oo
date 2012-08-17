(ns sources.t-scheduling
  (:use midje.sweet)
  (:import java.util.Date))

(load-file "sources/scheduling.clj")

(fact "it is silly that I keep having to define `separate` wherever I go"
  (separate odd? [1 2 3 4 5]) => [ [1 3 5] [2 4] ])

(fact "basic annotations"
  (answer-annotations [{:limit 4, :registered 3}] [])
  => [{:limit 4, :registered 3, :spaces-left 1, :already-in? false}]

  (answer-annotations [{:course-name "zigging" :limit 4, :registered 3}
                      {:course-name "zagging" :limit 1, :registered 1}]
                     ["zagging"])
  => [{:course-name "zigging" :limit 4, :registered 3, :spaces-left 1, :already-in? false}
      {:course-name "zagging" :limit 1, :registered 1, :spaces-left 0, :already-in? true}])

(fact "some domain annotations"
  (domain-annotations [{:registered 1, :spaces-left 1},
                            {:registered 0, :spaces-left 1},
                            {:registered 1, :spaces-left 0}])
  =>  [{:registered 1, :spaces-left 1, :full? false, :empty? false},
       {:registered 0, :spaces-left 1, :full? false, :empty? true},
       {:registered 1, :spaces-left 0, :full? true, :empty? false}])
                           

(fact "note-unavailability"
  (fact "with available instructors"
    (note-unavailability [{:full? true, :empty? false}
                          {:full? false, :empty? true}
                          {:full? false, :empty? false}]
                         3)
    =>  [{:full? true, :empty? false, :unavailable? true}
         {:full? false, :empty? true, :unavailable? false}
         {:full? false, :empty? false, :unavailable? false}])
  (fact "without available instructors"
    (note-unavailability [{:full? true, :empty? false}
                          {:full? false, :empty? true}
                          {:full? false, :empty? false}]
                         2)
    =>  [{:full? true, :empty? false, :unavailable? true}
         {:full? false, :empty? true, :unavailable? true}
         {:full? false, :empty? false, :unavailable? false}]))

(fact "end-to-end annotate"
  (annotate [{:course-name "Zipping", :morning? true, :limit 5, :registered 3}] [] 8)
  => [{:course-name "Zipping", :morning? true, :limit 5, :registered 3,
       :empty? false, :full? false, :spaces-left 2, :already-in? false, :unavailable? false}])

(fact "available courses use already-in? and unavailable?"
  (visible-courses [{:already-in? true, :unavailable? true}
                      {:already-in? true, :unavailable? false}
                      {:already-in? false, :unavailable? true}
                      {:already-in? false, :unavailable? false}])
  =>  [{:already-in? true, :unavailable? true}
       {:already-in? true, :unavailable? false}
       {:already-in? false, :unavailable? false}])

(fact "final shape removes keys"
  (let [desired {:course-name "Zigging",
                 :morning? true,
                 :registered 3,
                 :spaces-left 2,
                 :already-in? true}]
    (final-shape [(assoc desired :extraneous :stuff)]) => [desired]))
  
(fact "the solution sorts and whatnot"
  (solution [{:course-name "AM1", :morning? true, :limit 5, :registered 3}
             {:course-name "AM2", :morning? true, :limit 5, :registered 2}
             {:course-name "AM3", :morning? true, :limit 5, :registered 5}
             {:course-name "AM4", :morning? true, :limit 5, :registered 0}
             
             {:course-name "PM1", :morning? false, :limit 4, :registered 4}
             {:course-name "PM2", :morning? false, :limit 4, :registered 0}
             {:course-name "PM3", :morning? false, :limit 4, :registered 2}]
            ["AM3"]
            3)
  =>  [ [{:course-name "AM1", :morning? true, :registered 3, :spaces-left 2, :already-in? false}
         {:course-name "AM2", :morning? true, :registered 2, :spaces-left 3, :already-in? false}
         {:course-name "AM3", :morning? true, :registered 5, :spaces-left 0, :already-in? true}]
             
        [{:course-name "PM2", :morning? false, :registered 0, :spaces-left 4, :already-in? false}
         {:course-name "PM3", :morning? false, :registered 2, :spaces-left 2, :already-in? false}]])


(group-by odd? (range 10))
