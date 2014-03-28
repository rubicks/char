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
   [clojure.data.csv  :as csv     ]
   [clojure.data.json :as json    ]
   [clojure.pprint    :as pp      ]
   [clojure.test      :refer :all ]
   )
  )


(defn- -os [ar] (map zipmap (repeat (first ar)) (rest ar)))


(deftest format-csv

  (let [d (char.get/departures)]

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

      ;; (pp/pprint
      ;;  (remove #(empty? (get % "Track"))
      ;;          (-os (csv/read-csv body))))

      (let [ar (map rest (csv/read-csv body))]

        ;; (println (first ar))

        (let [ws (util/cwidths ar)]

          ;; (println ws)

          ;; (pp/pprint (map -fornat [1 1 2 3 5] (repeat "x")))
          (println
           (util/stripose "\n"
                          (map #(util/stripose " " (map util/fornat %1 %2))
                               (repeat ws) ar)))
          ;; (println)
          ;; (dorun (map println ar))
          ;; (println (reduce str (interpose "\n" (map -foo ar))))

          (let [os (-os ar)
                w (zipmap (first ar) ws)]

            ;; (pp/pprint w)

            ;; ;; (pp/pprint (filter #(= "North Station" (get % "Origin")) os))
            (pp/pprint (remove #(empty? (get % "Track")) os))

            ;; (dorun (map println (cons (keys (first os)) (map vals os))))

            ;; (println (reduce str (interpose "\n" (map -foo (repeat w) os))))

            ;; (map (fn [k] (-fornat (get )))

            ;; (map (fn [w o] (map (-fornat ws o))

            )
          )
        )
      )
    )
  )
