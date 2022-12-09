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
        (let [neighbors [(reverse (subvec line 0 j))
                         (subvec line (inc j))
                         (reverse (map (fn [l] (get l j)) (subvec lines 0 i)))
                         (map (fn [l] (get l j)) (subvec lines (inc i)))]]
          (apply *
           (map
            (fn [n-vec]
              (reduce (fn [a n] (if (< n v) (inc a) (reduced (inc a)))) 0 n-vec))
            neighbors))))
      line))
   lines))

(last (sort (apply concat scenic-scores)))
