(ns solutions.ts-scheduling
  (:use midje.sweet)
  (:import java.util.Date))

(load-file "solutions/pieces/scheduling-1.clj")

(fact "note-unavailability"
  (fact "with available instructors"
    (note-unavailability [{:full? true, :empty? false}
                          {:full? false, :empty? true}
                          {:full? false, :empty? false}]
                         3
                         false)
    =>  [{:full? true, :empty? false, :unavailable? true}
         {:full? false, :empty? true, :unavailable? false}
         {:full? false, :empty? false, :unavailable? false}])
  (fact "without available instructors"
    (note-unavailability [{:full? true, :empty? false}
                          {:full? false, :empty? true}
                          {:full? false, :empty? false}]
                         2
                         false)
    =>  [{:full? true, :empty? false, :unavailable? true}
         {:full? false, :empty? true, :unavailable? true}
         {:full? false, :empty? false, :unavailable? false}])
  (fact "managers have a blackout time"
    (note-unavailability [{:full? false, :empty? false, :morning? true}
                          {:full? false, :empty? false, :morning? false}]
                         2
                         true)
    =>  [{:full? false, :empty? false, :morning? true, :unavailable? false}
         {:full? false, :empty? false, :morning? false, :unavailable? true}])
  (fact "non-managers do not"
    (note-unavailability [{:full? false, :empty? false, :morning? true}
                          {:full? false, :empty? false, :morning? false}]
                         2
                         false)
    =>  [{:full? false, :empty? false, :morning? true, :unavailable? false}
         {:full? false, :empty? false, :morning? false, :unavailable? false}]))




(fact "the solution sorts and whatnot"
  (solution [{:course-name "AM1", :morning? true, :limit 5, :registered 3}
             {:course-name "AM2", :morning? true, :limit 5, :registered 2}
             {:course-name "AM3", :morning? true, :limit 5, :registered 5}
             {:course-name "AM4", :morning? true, :limit 5, :registered 0}
             
             {:course-name "PM1", :morning? false, :limit 4, :registered 4}
             {:course-name "PM2", :morning? false, :limit 4, :registered 0}
             {:course-name "PM3", :morning? false, :limit 4, :registered 2}]
            {:manager? false :taking-now ["AM3"]}
            3)
  =>  [ [{:course-name "AM1", :morning? true, :registered 3, :spaces-left 2, :already-in? false}
         {:course-name "AM2", :morning? true, :registered 2, :spaces-left 3, :already-in? false}
         {:course-name "AM3", :morning? true, :registered 5, :spaces-left 0, :already-in? true}]
             
        [{:course-name "PM2", :morning? false, :registered 0, :spaces-left 4, :already-in? false}
         {:course-name "PM3", :morning? false, :registered 2, :spaces-left 2, :already-in? false}]])


(fact "just confirm that managers don't get afternoon courses"
  (solution [{:course-name "PM1", :morning? false, :limit 4, :registered 4}
             {:course-name "PM2", :morning? false, :limit 4, :registered 0}
             {:course-name "PM3", :morning? false, :limit 4, :registered 2}]
            {:manager? true :taking-now []}
            3)
  =>  [ [] [] ])


(load-file "solutions/pieces/scheduling-2.clj")

;; As above
(fact "the solution sorts and whatnot"
  (solution [{:course-name "AM1", :morning? true, :limit 5, :registered 3, :prerequisites []}
             {:course-name "AM2", :morning? true, :limit 5, :registered 2, :prerequisites []}
             {:course-name "AM3", :morning? true, :limit 5, :registered 5, :prerequisites []}
             {:course-name "AM4", :morning? true, :limit 5, :registered 0, :prerequisites []}
             
             {:course-name "PM1", :morning? false, :limit 4, :registered 4, :prerequisites []}
             {:course-name "PM2", :morning? false, :limit 4, :registered 0, :prerequisites []}
             {:course-name "PM3", :morning? false, :limit 4, :registered 2, :prerequisites []}]
            {:manager? false, :taking-now ["AM3"], :previously-taken []}
            3)
  =>  [ [{:course-name "AM1", :morning? true, :registered 3, :spaces-left 2, :already-in? false}
         {:course-name "AM2", :morning? true, :registered 2, :spaces-left 3, :already-in? false}
         {:course-name "AM3", :morning? true, :registered 5, :spaces-left 0, :already-in? true}]
             
        [{:course-name "PM2", :morning? false, :registered 0, :spaces-left 4, :already-in? false}
         {:course-name "PM3", :morning? false, :registered 2, :spaces-left 2, :already-in? false}]])


(fact "prerequisites count"
  (fact "does not have all the prerequisites"
    (solution [{:course-name "AM1", :morning? true, :limit 5, :registered 3, :prerequisites []}
               {:course-name "AM2", :morning? true, :limit 5, :registered 2, :prerequisites []}
               {:course-name "AM3", :morning? true, :limit 5, :registered 1, :prerequisites ["AM1", "AM2"]}]
              {:manager? false, :taking-now [], :previously-taken ["AM1"]}
              3)
    =>  [ [{:course-name "AM1", :morning? true, :registered 3, :spaces-left 2, :already-in? false}
           {:course-name "AM2", :morning? true, :registered 2, :spaces-left 3, :already-in? false}]
          
          []])
  (fact "does have prerequisites"
    (solution [{:course-name "AM1", :morning? true, :limit 5, :registered 3, :prerequisites []}
               {:course-name "AM2", :morning? true, :limit 5, :registered 2, :prerequisites []}
               {:course-name "AM3", :morning? true, :limit 5, :registered 1, :prerequisites ["AM1", "AM2"]}]
              {:manager? false, :taking-now [], :previously-taken ["AM1" "AM2"]}
              3)
    =>  [ [{:course-name "AM1", :morning? true, :registered 3, :spaces-left 2, :already-in? false}
           {:course-name "AM2", :morning? true, :registered 2, :spaces-left 3, :already-in? false}
           {:course-name "AM3", :morning? true, :registered 1, :spaces-left 4, :already-in? false}]
          
          []]))

    
