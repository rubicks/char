;; char/src/char/routes/home.clj

(ns char.routes.home
  (:use compojure.core)
  (:require
   [compojure.core    :refer :all]
   [char.views.layout :as layout]
   [char.util         :as util]
   [liberator.core    :refer [resource defresource]]
   [cheshire.core]
   [clj-http.client   :as client]
   [clojure.data.csv  :as csv]
   ))


(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))


(defn about-page []
  (layout/render "about.html"))


(defresource foo
  :handle-ok "This is the foo page."
  :etag      "fixed-etag"
  :available-media-types ["text/plain"])


(defresource bar
  :service-available? true
  :allowed-methods [:get]
  :handle-ok (cheshire.core/generate-string {"foo" 42 "bar" 43} {:pretty true})
  :etag      "fixed-etag"
  :available-media-types ["application/json"])


(defn- -departures []
  (let [got (client/get "http://developer.mbta.com/lib/gtrtfs/Departures.csv")]
    (cheshire.core/generate-string
     (identity (csv/read-csv (:body got))) {:pretty true})
    )
  )


(defresource departures
  :allowed-methods [:get]
  :handle-ok (-departures)
  :etag "fixed-etag"
  :available-media-types ["application/json"]
  )


(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (ANY "/foo" request foo)
  (ANY "/bar" request bar)
  (ANY "/departures" request departures)
  )
