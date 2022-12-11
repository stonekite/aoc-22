(require '[clojure.string :as str])

(def fs (js/require "fs"))

(def lines (vec (str/split (.readFileSync fs "./inputs/day10.txt" "utf8") "\n")))

(def req-cycles (map (fn [i] (+ 19 (* 40 i))) (range 6)))

(->> lines
     (map
      (fn [line]
        (if (= "noop" line)
          0
          [0 (js/parseInt (get (str/split line " ") 1))])))
     flatten
     (map-indexed
      (fn [i v] [i v]))
     (reduce
      (fn [a [i v]]
        (merge-with + a
                    {:value v
                     :sum (let [cycle (nth (filter
                                            (fn [cycle] (= i cycle))
                                            req-cycles)
                                           0
                                           nil)]
                            (if (not= nil cycle)
                              (* (:value a) (+ 1 cycle))
                              0))}))
      {:value 1
       :sum 0})
     :sum)
