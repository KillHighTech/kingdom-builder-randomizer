(ns kingdom-builder
  (:require [reagent.core :as reagent :refer [atom]]
            [rand-cljc.core :as rng]))

(def ^:export base-set
  {:goals ["Fishermen"
           "Lords"
           "Miners"
           "Hermits"
           "Farmers"
           "Merchants"
           "Knights"
           "Discoverers"
           "Citizens"
           "Workers"]
   :maps [{:name "Oracle"}
          {:name "Tower"}
          {:name "Farm"}
          {:name "Harbor"}
          {:name "Tavern"}
          {:name "Paddock"}
          {:name "Oasis"}
          {:name "Barn"}]})

(def ^:export nomads
  {:goals ["Families"
           "Shepherds"
           "Ambassadors"]
   :maps [{:name "Quarry" :nomad-tiles 3}
          {:name "Caravan" :nomad-tiles 1}
          {:name "Village" :nomad-tiles 1}
          {:name "Garden" :nomad-tiles 1}]})

(def nomad-tiles
   ["Treasure"
   "Treasure"
   "Resettlement"
   "Resettlement"
   "Sword"
   "Sword"
   "Outpost"
   "Outpost"
   "Donation - Water"
   "Donation - Mountain"
   "Donation - Grass"
   "Donation - Canyon"
   "Donation - Desert"
   "Donation - Flower field"
   "Donation - Forest"])

(def ^:export crossroads
  {:goals []
   :maps (map #(assoc % :crossroads 1)
              [{:name "Barracks/Crossroads"}
               {:name "Lighthouse/Forester's Lodge"}
               {:name "City Hall/Fort"}
               {:name "Monestary/Wagon"}])})

(def crossroad-tasks
  ["Home country"
   "Fortress"
   "Road"
   "Place of refuge"
   "Advance"
   "Compass point"])

(defn ^:export setup
  [expansions seed]
  (let [flipped-states ["↶" ""]
        rng (rng/rng seed)
        sets (conj expansions base-set)
        maps (take 4 (rng/shuffle rng (mapcat :maps sets)))
        goals (take 3 (rng/shuffle rng (mapcat :goals sets)))
        crossroads-card-count (reduce + (map :crossroads maps))
        tasks (take crossroads-card-count (rng/shuffle rng crossroad-tasks))
        nomad-tile-count (reduce + (map :nomad-tiles maps))
        nomad-tiles (take nomad-tile-count (rng/shuffle rng nomad-tiles))]
    {:maps (map #(str (first (rng/shuffle rng flipped-states)) (:name %)) maps)
     :goals goals
     :tasks tasks
     :nomad-tiles nomad-tiles}))

(enable-console-print!)

(def app-state (atom #{:nomads :crossroads}))
(def seed (atom 0))

(def expansions {:nomads nomads :crossroads crossroads})

(defn new-seed
  []
  (let [new-seed (rand-int 19239492)]
    (set! js/window.location.hash new-seed)
    (swap! app-state disj nil)
    (reset! seed new-seed)))

(defn expansion-state [expansion]
  [:button {:on-click (fn [e] (if (expansion @app-state) (swap! app-state disj expansion) (swap! app-state conj expansion)))} (str expansion (if (expansion @app-state) "(enabled)" "(disabled)"))])

(defn kingdom-builder-widget []
  [:div
   [:div
    [:div (map expansion-state (keys expansions)) [:button {:on-click new-seed} "again"]]
    [:div
     [:code (prn-str (setup (map expansions @app-state) @seed))]]]])

(reagent/render-component [kingdom-builder-widget]
  (. js/document (getElementById "app")))

(if js/window.location.hash
  (reset! seed (int (subs js/window.location.hash 1)))
  (new-seed))
