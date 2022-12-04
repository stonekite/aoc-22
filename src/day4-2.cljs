(require '[clojure.string :as str])

(def fs (js/require "fs"))

(def lines (str/split (.readFileSync fs "./inputs/day4.txt" "utf8") "\n"))

(defn solve-line [line]
  (let [[left right] (sort-by count (map (fn [group]
                                           (let [[lower upper] (str/split group "-")]
                                             (range
                                              (#(js/parseInt %) lower)
                                              (+ 1 (#(js/parseInt %) upper)))))
                                         (str/split line ",")))]
    (some
     (fn [l-id] (some
                 (fn [r-id] (= l-id r-id))
                 right))
     left)))

(count (filter solve-line lines))
