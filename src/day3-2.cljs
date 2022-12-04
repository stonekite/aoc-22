(require '[clojure.string :as str])

(def fs (js/require "fs"))

(def lines (str/split (.readFileSync fs "./inputs/day3.txt" "utf8") "\n"))

(defn get-points [char-str]
  (let [value (.. char-str (charCodeAt 0))]
    (if (< value 97) (- value 38) (- value 96))))

(defn solve-chunk [chunk]
  (get-points (first (filter (fn [f-char] (every? (fn [backpack] (some (fn [r-char] (= f-char r-char)) backpack)) (rest chunk))) (first chunk)))))

(let [chunks (partition 3 lines)]
  (reduce (fn [a chunk] (+ a (solve-chunk chunk))) 0 chunks))
