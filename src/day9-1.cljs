(require '[clojure.string :as str] '[goog.math :as math])

(def fs (js/require "fs"))

(def lines (str/split (.readFileSync fs "./inputs/day9.txt" "utf8") "\n"))

(defn coord-diff [a b]
  (map (fn [i] (- (get a i) (get b i))) (range 2)))

(defn distance [diff]
  (last (sort (map abs diff))))

(defn sum-elements [a b]
  (vec (map
        (fn [i] (+ (get a i) (get b i)))
        (range (count a)))))

(def dir-changes {:U [0 1]
                  :R [1 0]
                  :D [0 -1]
                  :L [-1 0]})

(count
 (set
  (last
   (reduce
    (fn [a line]
      (let [[dir num] (vec (map-indexed
                            (fn [i v]
                              (if (= i 0) v (js/parseInt v)))
                            (str/split line " ")))]
        (reduce
         (fn [aa _]
           (reduce (fn [aaa i]
                     (let [[head tail] (map first (subvec aaa (- i 1) (inc i)))
                           new-head (if (= 1 i)
                                      (sum-elements head (get dir-changes (keyword dir)))
                                      head)
                           diff (coord-diff new-head tail)
                           new-tail (if (< 1 (distance diff))
                                      (sum-elements tail
                                                    (vec (map
                                                          (fn [c] (math/clamp c -1 1))
                                                          diff)))
                                      tail)]
                       (vec (concat
                             (subvec aaa 0 (- i 1))
                             [(if (= 1 i)
                                (conj (get aaa (- i 1)) new-head)
                                (get aaa (- i 1)))
                              (conj (get aaa i) new-tail)]
                             (subvec aaa (inc i))))))
                   aa
                   (rest (range (count aa)))))
         a
         (range num))))
    (vec (repeat 10 (list [0 0])))
    lines))))
