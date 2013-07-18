(defproject frp "0.1.0-SNAPSHOT"
  :description "Polyline drawing with core.async and JavaFX"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.async "0.1.0-SNAPSHOT"]]
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  ;:jvm-opts
  ;["-Xbootclasspath/a:/Library/Java/JavaVirtualMachines/jdk1.7.0_21.jdk/Contents/Home/jre/lib/jfxrt.jar"]
  :jvm-opts [~(str "-Xbootclasspath/a:" (.. System (getProperties) (get "java.home")) "/lib/jfxrt.jar")])
