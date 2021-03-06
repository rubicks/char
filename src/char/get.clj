;; char/src/char/get.clj

(ns char.get
  (:use compojure.core)
  (:require
   [char.util         :as util]
   [char.views.layout :as layout]
   [cheshire.core]
   [clj-http.client   :as client]
   [clojure.core.memoize ]
   [clojure.data.csv  :as csv]
   [clojure.pprint    :as pp ]
   [compojure.core    :refer :all]
   [liberator.core    :refer [resource defresource]]
   ))


(def url
  {:departures "http://developer.mbta.com/lib/gtrtfs/Departures.csv"}
  )


(defn departures [] (client/get (:departures url)))

;; (def departures
;;   (clojure.core.memoize/ttl
;;    #((let [dummy (println"\n\n* * ** *** ***** HIT ***** *** ** * *\n\n")]
;;        (client/get (:departures url)) {} :ttl/threshold 10))))

