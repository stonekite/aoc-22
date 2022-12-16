(require '[clojure.string :as str])

(def fs (js/require "fs"))

(def lines (str/split (.readFileSync fs "./inputs/day12.txt" "utf8") "\n"))

(def v-points
  (map
   (fn [v-char]
     (reduce
      (fn [i line]
        (let [ii (reduce
                  (fn [ii char]
                    (if (= char v-char)
                      (reduced ii)
                      (inc ii)))
                  0
                  line)]
          (if (< ii (count line))
            (reduced [i ii])
            (inc i))))
      0
      lines))
   ["S" "E"]))

(def grid
  (vec
   (map
    (fn [line]
      (vec
       (map
        (fn [char]
          (let [is-start (= char "S")
                is-stop (= char "E")]
            {:cost (when is-start 0)
             :elev (if is-start
                     0
                     (if is-stop
                       25
                       (- (.charCodeAt char 0) 97)))}))
        line)))
    lines)))

(def steps [[-1 0] [0 -1] [1 0] [0 1]])

(defn get-neighbors [grid [c-i c-ii :as cur]]
  (->> steps
       (map
        (fn [[i ii]]
          [(+ c-i i) (+ c-ii ii)]))
       (filter
        (fn [n]
          (not (or (nil? (get-in grid n))
                   (< 1 (apply -
                               (map #(:elev (get-in grid %)) [n cur])))
                   (and (some? (:cost (get-in grid n)))
                        (apply <=
                               (map #(:cost (get-in grid %)) [n cur])))))))))

(let [[start end] v-points]
  (loop [grid grid
         neighbors (get-neighbors grid start)
         cost (:cost (get-in grid start))]
    (let [new-cost (inc cost)]
      (if (some #(= end %) neighbors)
        new-cost
        (when (seq neighbors)
          (recur
           (reduce
            (fn [a n]
              (update-in a n (constantly new-cost)))
            grid
            neighbors)
           (->> neighbors
                (map #(get-neighbors grid %))
                (apply concat)
                (distinct))
           (inc cost)))))))