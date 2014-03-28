(ns char.util
  (:require [noir.io :as io]
            [markdown.core :as md]))

(defn md->html
  "reads a markdown file from public/md and returns an HTML string"
  [filename]
  (->>
    (io/slurp-resource filename)
    (md/md-to-html-string)))

(defn transpose [lol] (apply map list lol))


(defn- -seq [cont arr] (map cont arr))

(defn- -map [cont obj] (zipmap (keys obj) (-seq cont (vals obj))))

;; kextract recursive
(defn- -kextractr [o k]
  (let [f #(-kextractr % k)]
    (cond
     (sequential? o) (-seq f o)
     (map? o) (if (contains? (set (keys o)) k)
                (hash-map (get o k) (-map f (dissoc o k)))
                (-map f o))
     :default o)))

(defn kextractr [o & ks] (reduce -kextractr o (flatten ks)))


(defn stripose [s coll] (reduce str (interpose s coll)))

(defn fornat [n s] (format (str "%" n "s") s))

(defn countr [o] (if (sequential? o) (map countr o) (count o)))

(defn cwidths [ar] (apply map (partial max) (countr ar)))

