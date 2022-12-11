(require '[clojure.string :as str])

(def fs (js/require "fs"))

(def lines (vec (str/split (.readFileSync fs "./inputs/day10.txt" "utf8") "\n")))

(let [cycles (->> lines
                  (map (fn [line]
                      (if (= "noop" line)
                        0
                        [0 (-> line
                               (str/split " ")
                               (get 1)
                               js/parseInt)])))
                  flatten
                  vec)]
  (->> cycles
       (map-indexed
        (fn [i _]
          (apply + (subvec cycles 0 i))))
       (partition 40)
       (map
        (fn [line]
          (->> line
               (map-indexed
                (fn [i v] [i (+ 1 v)]))
               (reduce
                (fn [a [i v]]
                  (str a (if (> 2 (abs (- i v)))
                           "#"
                           ".")))
                ""))))))
