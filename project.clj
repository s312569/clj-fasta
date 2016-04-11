(defproject clj-fasta "0.1.5"
  :description "Parser for sequences in FASTA format."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [fs "1.3.3"]
                 [org.clojure/tools.nrepl "0.2.12"]]
  :repl-options {:init (set! *print-length* 100)}
  :jvm-opts ["-Xmx1000M"])
