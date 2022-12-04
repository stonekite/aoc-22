(require '[clojure.string :as str])

(def fs (js/require "fs"))

(def lines (str/split (.readFileSync fs "./inputs/day2.txt" "utf8") "\n"))

(def rules {:A 1 :B 2 :C 3 :X 1 :Y 2 :Z 3})

(defn wins [points]
  (let [left (first points)
        right (last points)]
    (case left
      1 (= 3 right)
      2 (= 1 right)
      3 (= 2 right))))

(defn win-with [points left]
  (if left
    [(+ 6 (first points)) (last points)]
    [(first points) (+ 6 (last points))]))

(defn solve-line [line]
  (let [points (map
                #(get rules (keyword %))
                (str/split line " "))]
    (if (= (first points) (last points)) 
      [(+ 3 (first points)) (+ 3 (last points))] 
      (win-with points (wins points)))))

(reduce (fn [a line] (+ a (last (solve-line line))))
        0
        lines)
