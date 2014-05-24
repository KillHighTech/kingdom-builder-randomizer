(ns kingdom-builder
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

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
  [expansions]
  (let [flipped-states ["↶" ""]
        sets (conj expansions base-set)
        maps (take 4 (shuffle (mapcat :maps sets)))
        goals (take 3 (shuffle (mapcat :goals sets)))
        crossroads-card-count (reduce + (map :crossroads maps))
        tasks (take crossroads-card-count (shuffle crossroad-tasks))
        nomad-tile-count (reduce + (map :nomad-tiles maps))
        nomad-tiles (take nomad-tile-count (shuffle nomad-tiles))]
    {:maps (map #(str (first (shuffle flipped-states)) (:name %)) maps)
     :goals goals
     :tasks tasks
     :nomad-tiles nomad-tiles}))

(enable-console-print!)

(def app-state (atom #{:nomads :crossroads}))

(def expansions {:nomads nomads :crossroads crossroads})

(defn expansion-state [expansion]
  (dom/button #js {:onClick (fn [e] (if (expansion @app-state) (swap! app-state disj expansion) (swap! app-state conj expansion)))} (str expansion (if (expansion @app-state) "(enabled)" "(disabled)"))))

(defn kingdom-builder-widget [state owner]
  (reify
    om/IRender
      (render [this]
        (dom/div nil
          (dom/div nil
            (apply dom/div nil (map expansion-state (keys expansions)))
            (dom/button #js {:onClick (fn [e] (swap! app-state disj nil))} "again"))
          (dom/code nil (prn-str (setup (map expansions @app-state))))))))

(om/root kingdom-builder-widget app-state
  {:target (. js/document (getElementById "app"))})

