(ns solutions.ts-scheduling-variant
  (:use midje.sweet)
  (:import java.util.Date))

(load-file "solutions/scheduling-variant.clj")

(fact
  (update-with {:a 1 :b [1 2] :c [3 4] :d [5 6]} set [:b :c])
  => {:a 1 :b #{1 2} :c #{3 4} :d [5 6]})


(fact "basic behavior works"
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

(fact "managers don't get afternoon courses"
  (solution [{:course-name "AM1", :morning? true, :limit 5, :registered 3, :prerequisites []}

             {:course-name "PM1", :morning? false, :limit 4, :registered 4, :prerequisites []}
             {:course-name "PM2", :morning? false, :limit 4, :registered 0, :prerequisites []}
             {:course-name "PM3", :morning? false, :limit 4, :registered 2, :prerequisites []}]
            {:manager? true :taking-now [], :previously-taken []}
            3)
  =>  [ [{:course-name "AM1", :morning? true, :registered 3, :spaces-left 2, :already-in? false}]
        []])

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

    
