(ns todogo-cljs.local-storage)

(defn ls-get [key]
  (.getItem (.-localStorage js/window) key))

(defn ls-set [key val]
  (.setItem (.-localStorage js/window) key, val))

(defn ls-del [key]
  (.removeItem (.-localStorage js/window) key))
