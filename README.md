# clj-fasta

Parser for sequences in FASTA format.

## Usage

Import from clojars:

```clojure
[clj-fasta "0.2.0"]
```

Import into namespace:

```clojure
(:require [clj-fasta.core :as fa])
```

Takes a buffered reader and returns a lazy list of hashmaps containing
the accession, description and sequence of the fasta sequences on the
reader. Regular expressions to parse the accession and description
from the fasta sequence can be specified (default is accession being
text before the first space and the description text after the first
space). Ignores all lines prior to the first line that starts with
'>'.

```clojure
user> (with-open [r (reader "/fasta/file.fa")]
        (->> (fasta-seq r)
             first))
{:accession "TRINITY_DN22_c0_g1_i1", :description 
 "len=206 path=[638:0-65 639:66-66 640:67-205] [-1, 638, 639, 640, -2]",
 :sequence "GCGAGGA..."}
user>
```

A collection of fasta sequences can be sent to file using
fasta->file. This function also takes the :append and :func keyword
for appending to a file and transforming the sequence before
writing. For example:

```clojure
user> (with-open [r (reader "/fasta/file.fa")]
        (fasta->file (fasta-seq r)
                     "outfile"
                     :append true
		     :func #(update-in % [:sequence] clojure.string/lower-case)))
"outfile"		     
user>
```

Library also works in ClojureScript although there is no fasta->file
function or integration with biodb. The fasta-seq function takes a
string as its argument and is not lazy.

## License

Copyright © 2016 Jason Mulvenna

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
