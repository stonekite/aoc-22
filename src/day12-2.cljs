(require '[clojure.string :as str])

(def fs (js/require "fs"))

(def lines (str/split (.readFileSync fs "./inputs/day12.txt" "utf8") "\n"))

(def start
  (reduce
   (fn [i line]
     (let [ii (reduce
               (fn [ii char]
                 (if (= char "E")
                   (reduced ii)
                   (inc ii)))
               0
               line)]
       (if (< ii (count line))
         (reduced [i ii])
         (inc i))))
   0
   lines))

(def grid
  (vec
   (map
    (fn [line]
      (vec
       (map
        (fn [char]
          (let [is-lowest (= char "S")
                is-start (= char "E")]
            {:cost (when is-start 0)
             :elev (if is-start
                     25
                     (if is-lowest
                       0
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
                               (map #(:elev (get-in grid %)) [cur n])))
                   (and (some? (:cost (get-in grid n)))
                        (apply <=
                               (map #(:cost (get-in grid %)) [n cur])))))))))

(loop [grid grid
       neighbors (get-neighbors grid start)
       cost (:cost (get-in grid start))]
  (let [new-cost (inc cost)]
    (if (some #(= 0 (:elev (get-in grid %))) neighbors)
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
         (inc cost))))))