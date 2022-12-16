(require '[clojure.string :as str])

(def fs (js/require "fs"))

(def blocks (map
             (fn [monkey]
               (map (fn [line] (subs line 2))
                    (rest (str/split monkey "\n"))))
             (str/split
              (.readFileSync fs "./inputs/day11.txt" "utf8")
              "\n\n")))

(def ops {:+ (fn [a b]
               (+ a (js/parseInt b)))
          :* (fn [a b]
              (if (= "old" b)
                (* a a)
                (* a (js/parseInt b))))})

(def monkeys
  (->> blocks
       (reduce
        (fn [a block]
          (conj
           a
           (-> block
               (->> (map-indexed
                     (fn [i v]
                       [i v]))
                    (reduce
                     (fn [aa [i line]]
                       (let [split (str/split line " ")]
                         (merge
                          aa
                          (case i
                            0 {:items (-> line
                                          (str/replace #"[^\d,]" "")
                                          (str/split ",")
                                          (->> (map #(js/parseInt %)))
                                          vec)}
                            1 {:op (let [[op num] (take-last 2 split)]
                                     (fn [x] (-> x
                                                 (((keyword op) ops) num)
                                                 (quot 3))))}
                            2 {:test (fn [x]
                                       (= 0
                                          (rem x (js/parseInt (last split)))))}
                            3 {:true (js/parseInt (last split))}
                            4 {:false (js/parseInt (last split))}))))
                     (hash-map)))
               (assoc :business 0))))
        [])))

(->> (reduce
      (fn [a _] ;; rounds
        (reduce
         (fn [aa i] ;; monkeys
           (reduce
            (fn [aaa item] ;; items
              (let [cur (nth aa i)
                    new-item ((:op cur) item)
                    new-i (if ((:test cur) new-item)
                            (:true cur)
                            (:false cur))]
                (-> aaa
                    (update-in [i :business]
                               inc)
                    (update-in [i :items]
                               subvec 1)
                    (update-in [new-i :items]
                               conj new-item))))
            aa
            (:items (nth aa i))))
         a
         (range (count a))))
      monkeys
      (range 20))
     (sort-by :business)
     (take-last 2)
     (map :business)
     (apply *))
