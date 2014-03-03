(ns char.test.handler
  (:use clojure.test
        ring.mock.request
        char.handler)
  (:require
   [clojure.pprint    :as pp     ]
   [clj-http.client   :as client ]
   [clojure.data.csv  :as csv    ]
   [clojure.data.json :as json   ]
   [cheshire.core]
   )
  )


(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))
      (is (:body response))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404))))
  )


(defn- -dummy-departures []
  [["TimeStamp", "Origin", "Trip", "Destination", "ScheduledTime", "Lateness", "Track", "Status"],
   ["1393802793", "North Station", "2169", "Newburyport", "1393805700", "0", "", "On Time"],
   ["1393802793", "North Station", "2313", "Lowell", "1393808400", "0", "", "On Time"],
   ["1393802793", "North Station", "2413", "Fitchburg", "1393808400", "0", "", "On Time"],
   ["1393802793", "North Station", "2121", "Rockport", "1393810200", "0", "", "On Time"],
   ["1393802793", "North Station", "2221", "Haverhill", "1393810800", "0", "", "On Time"],
   ["1393802793", "North Station", "2173", "Newburyport", "1393816500", "0", "", "On Time"],
   ["1393802793", "North Station", "2315", "Lowell", "1393821000", "0", "", "On Time"],
   ["1393802793", "North Station", "2415", "Fitchburg", "1393821000", "0", "", "On Time"],
   ["1393802793", "North Station", "2125", "Rockport", "1393821000", "0", "", "On Time"],
   ["1393802793", "North Station", "2225", "Haverhill", "1393821000", "0", "", "On Time"],
   ["1393802793", "South Station", "2813", "Providence", "1393803900", "0", "", "On Time"],
   ["1393802793", "South Station", "2715", "Forge Park / 495", "1393806000", "0", "", "On Time"],
   ["1393802793", "South Station", "2013", "Middleboro/ Lakeville", "1393809000", "0", "", "On Time"],
   ["1393802793", "South Station", "P565", "Framingham", "1393810200", "0", "", "On Time"],
   ["1393802793", "South Station", "2815", "Providence", "1393811100", "0", "", "On Time"],
   ["1393802793", "South Station", "2717", "Forge Park / 495", "1393813200", "0", "", "On Time"],
   ["1393802793", "South Station", "2015", "Middleboro/ Lakeville", "1393817700", "0", "", "On Time"],
   ["1393802793", "South Station", "P567", "Worcester / Union Station", "1393819200", "0", "", "On Time"],
   ["1393802793", "South Station", "2817", "Providence", "1393819800", "0", "", "On Time"],
   ["1393802793", "South Station", "2719", "Forge Park / 495", "1393820400", "0", "", "On Time"]]
  )


(defn- -foobar [o]
  (let [ks ["TimeStamp" "ScheduledTime" "Lateness"]]
    (merge o (zipmap ks (map (fn [k] (Long. (get o k))) ks)))))


(defn- -keyfun [o] (get o "ScheduledTime"))


(deftest departure-board

  (testing "the mbta link"

    ;; (let [got (client/get "http://developer.mbta.com/lib/gtrtfs/Departures.csv")]
    ;;   (is got)
    ;;   (json/pprint (csv/read-csv (:body got)))
    ;;   (let [x (map rest (csv/read-csv (:body got)))]
    ;;     ;; (zipmap (repeat (first x) (rest x)))
    ;;     )
    ;;   )
    )

  (testing "dummy departures"
    (let [x (-dummy-departures)]
      (let [y (map zipmap (repeat (first x)) (rest x))]
        ;; (json/pprint (sort-by -keyfun y))
        (let [z (map -foobar y)]
          (json/pprint (sort-by -keyfun z))
          )
        )
      )
    )


  ;; (testing "departures"
  ;;   (let [response (app (request :get "/departures"))]
  ;;     (is response)
  ;;     (pp/pprint response)
  ;;     )
  ;;   )
  )
