(require '[clojure.string :as str])

(def fs (js/require "fs"))

(def inputs (str/split (.readFileSync fs "./inputs/day5.txt" "utf8") "\n\n"))

(def crates-strs (str/split (first inputs) "\n"))

(def moves (str/split (last inputs) "\n"))

(def stacks (vec
             (map
              vec
              (filter
               (fn [line]
                 (some
                  (fn [char]
                    (let [cc (.charCodeAt char 0)]
                      (and (> cc 64) (< cc 91))))
                  line))
               (map
                (fn [line]
                  (filter
                   (fn [char] (not= " " char))
                   line))
                (apply map list (rest (reverse crates-strs))))))))

(apply
 +
 (map
  peek
  (reduce (fn [cur-stacks move]
            (let [[num from to]
                  (map-indexed
                   (fn [i item]
                     (let [parsed-item (js/parseInt item)]
                       (if
                        (> i 0)
                         (- parsed-item 1)
                         parsed-item)))
                   (re-seq #"\d+" move))]
              (vec
               (map-indexed
                (fn [i stack]
                  (if
                   (= i from)
                    (subvec stack 0 (- (count stack) num))
                    (if
                     (= i to)
                      (vec (concat
                            stack
                            (reverse
                             (subvec
                              (get cur-stacks from)
                              (-
                               (count (get cur-stacks from))
                               num)))))
                      stack)))
                cur-stacks))))
          stacks
          moves)))
