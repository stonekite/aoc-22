(def fs (js/require "fs"))

(def buffer (.readFileSync fs "./inputs/day6.txt" "utf8"))

(def len 4)

((fn [i]
   (let [substring (subs buffer (- i len) i)]
     (if
      (and
       (= len (count substring))
       (=
        (count (set substring))
        (count substring)))
       i
       (recur (inc i)))))
 len)
