(defproject kingdom-builder-randomizer "0.1.0-SNAPSHOT"
  :description "A very simple Kingdom Builder Randomizer"
  :url "http://dan.bravender.net"

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.28"]
                 [reagent "0.5.0"]
                 [rand-cljc "0.1.0"]]

  :plugins [[lein-cljsbuild "1.0.3"]]

  :source-paths ["src"]

  :cljsbuild {
    :builds [{:id "kingdom-builder-randomizer"
              :source-paths ["src"]
              :compiler {
                :output-to "main.js"
                :output-dir "out"
                :preamble ["reagent/react.js"]
                :optimizations :advanced}}]})
