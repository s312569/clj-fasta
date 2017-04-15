(defproject clj-fasta "0.2.3"
  :description "Parser for sequences in FASTA format."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.521"]
                 [fs "1.3.3"]
                 [org.clojure/tools.nrepl "0.2.13"]
                 [biodb "0.2.4"]]
  :plugins [[lein-cljsbuild "1.1.3"]]
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljc"]
                        :jar true
                        :compiler {:output-to "out/main.js"
                                   :optimizations :advanced}}]}
  :hooks [leiningen.cljsbuild]
  :repl-options {:init (set! *print-length* 100)}
  :jvm-opts ["-Xmx1000M"])
