;; /char/test/char/test/get.clj

(ns char.test.get
  ;; (:use clojure.test
  ;;       ring.mock.request
  ;;       char.handler)
  (:require
   [char.get                      ]
   [char.util         :as util    ]
   [cheshire.core                 ]
   [clj-http.client   :as client  ]
   [clj-time.coerce               ]
   [clojure.data.csv  :as csv     ]
   [clojure.data.json :as json    ]
   [clojure.pprint    :as pp      ]
   [clojure.test      :refer :all ]
   )
  )


(defn- -mmut [m k f] (assoc m k (f (get m k))))


;; take string, return Long
(defn- -parlon [s] (Long/parseLong s))


;; take some number of seconds since 1970-01-01T00:00Z, return DateTime
;; representation
(defn- -dati [o]
  (clj-time.coerce/from-long
   (* 1000
      (if (number? o) o (Long/parseLong o)))))


(deftest format-csv

  (let [d (char.get/departures)]

    (is (map? d))

    ;; (println)
    ;; (pp/pprint (dissoc d :body))

    (is (= [:body
            :headers
            :orig-content-encoding
            :request-time
            :status
            :trace-redirects]
           (sort (keys d))))

    (let [body (:body d)]

      (is (string? body))

      ;; (pp/pprint
      ;;  (remove #(empty? (get % "Track"))
      ;;          (-os (csv/read-csv body))))

      (let [ar (csv/read-csv body)]

        (is (sequential? ar))
        (is (every? sequential? ar))
        (is (every? #(every? string? %) ar))

        ;; (println (first ar))

        (let [ws (util/cwidths ar)]
          ;; (println ws)
          ;; (pp/pprint (map -fornat [1 1 2 3 5] (repeat "x")))
          (println
           (util/stripose "\n"
                          (map #(util/stripose " " (map util/fornat %1 %2))
                               (repeat ws) ar)))
          )

        (let [epoch 0
              now (System/currentTimeMillis)]
          (pp/pprint ["epoch" epoch])
          (pp/pprint ["now" now])
          (pp/pprint ["(-dati now)" (clj-time.coerce/from-long now)])
          (pp/pprint ["(-dati epoch)" (clj-time.coerce/from-long epoch)])
          (pp/pprint ["(-dati epoch)" (-dati epoch)])
          )

        (let [os (util/objectify ar)
              f (fn [s] (->> s
                             (Long/parseLong)
                             (* 1000)
                             (clj-time.coerce/from-long)))]

          (is (sequential? os))
          (is (every? map? os))
          ;; (is (every? #(every? string)map? os))

          ;; ;; (pp/pprint (filter #(= "North Station" (get % "Origin")) os))
          ;; (pp/pprint (remove #(empty? (get % "Track")) os))
          ;; (pp/pprint os)

          (pp/pprint
           (->> os
                (map #(-mmut % "TimeStamp" f))
                (map #(-mmut % "ScheduledTime" f))
                ))

          ;; (dorun (map println (cons (keys (first os)) (map vals os))))

          ;; (println (reduce str (interpose "\n" (map -dati (repeat w) os))))

          ;; (map (fn [k] (-fornat (get )))

          ;; (map (fn [w o] (map (-fornat ws o))

          )
        )
      )
    )
  )
