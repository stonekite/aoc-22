(require '[clojure.string :as str])

(def fs (js/require "fs"))

(def lines (vec
            (map
             (fn [line]
               (vec
                (map #(js/parseInt %) (vec line))))
             (str/split (.readFileSync fs "./inputs/day8.txt" "utf8") "\n"))))

(def scenic-scores
  (map-indexed
   (fn [i line]
     (map-indexed
      (fn [j v]
        (let [neighbors (concat
                         [(subvec line 0 j) (subvec line (inc j))]
                         [(map (fn [l] (get l j)) (subvec lines 0 i))
                          (map (fn [l] (get l j)) (subvec lines (inc i)))])]
          (some
           (fn [invis] (not= true invis))
           (map
            (fn [n-vec]
              (some (fn [n] (>= n v)) n-vec))
            neighbors))))
      line))
   lines))

(count (filter true? (apply concat scenic-scores)))
