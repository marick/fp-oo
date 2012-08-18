(ns sources.t-scheduling-variant
  (:use midje.sweet)
  (:import java.util.Date))

(load-file "sources/scheduling-variant.clj")

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
