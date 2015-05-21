# corenlpd

An HTTP REST server frontend for Stanford Core NLP written in Clojure (http://nlp.stanford.edu/software/corenlp.shtml)

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To run as a server:

    lein ring server

## Installation

To install copy source to /usr/local/bin/corenlpd (or change path in corenlpd.conf) and run:

	lein uberjar

Then copy corenlpd.conf to /etc/init and run:

	service corenlpd start

## License

See LICENSE.md

Copyright © 2015 Lucian Hontau
