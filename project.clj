(defproject kingdom-builder-randomizer "0.1.0-SNAPSHOT"
  :description "A very simple Kingdom Builder Randomizer"
  :url "http://dan.bravender.net"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2197"]
                 [reagent "0.4.2"]]

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
