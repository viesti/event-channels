(defproject event-channels "0.1.0-SNAPSHOT"
  :description "Event handling with core.async"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.async "0.1.0-SNAPSHOT"]
                 [domina "1.0.1"]]
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :jvm-opts [~(str "-Xbootclasspath/a:" (.. System (getProperties) (get "java.home")) "/lib/jfxrt.jar")]
  :plugins [[lein-cljsbuild "0.3.2"]]
  :cljsbuild {:builds [{:source-paths ["src-cljs"]
                        :notify-command ["growlnotify" "-m"]
                        :compiler {
                                   :output-to "public/js/main.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]})
