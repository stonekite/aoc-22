(require '[clojure.string :as str])

(def fs (js/require "fs"))

(def lines (str/split (.readFileSync fs "./inputs/day3.txt" "utf8") "\n"))

(defn get-points [char-str]
  (let [value (.. char-str (charCodeAt 0))]
    (if (< value 97)
      (- value 38)
      (- value 96))))

(defn solve-line [line]
  (let [[left right] (split-at (quot (count line) 2) line)]
    (get-points (first (filter (fn [l-char]
                                 (some (fn [r-char]
                                         (= l-char r-char))
                                       right))
                               left)))))

(reduce (fn [a line] (+ a (solve-line line)))
        0
        lines)
