# clj-fasta

Parser for sequences in FASTA format.

## Usage

Import from clojars:

```clojure
[clj-fasta.core "0.1.7"]
```

Import into namespace:

```clojure
(:require [clj-fasta :as fa])
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

## License

Copyright © 2016 Jason Mulvenna

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
