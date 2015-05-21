# corenlpd

An HTTP REST server frontend for [Stanford Core NLP](http://nlp.stanford.edu/software/corenlp.shtml) written in Clojure using the [Luminus](http://www.luminusweb.net/) framework.

## Prerequisites

You will need [Leiningen](http://leiningen.org/) 2.0 or above installed.

## Installation

To install copy source to ```/usr/local/bin/corenlpd``` (or change path in ```corenlpd.conf```) and run:

	lein uberjar

Add a user for the service (as root):

	adduser corenlpd

 Edit sudoers (as root):

	visudo

Add corenlpd to sudoers (as root):

	corenlpd ALL=NOPASSWD: /usr/local/bin/corenlpd/target/corenlpd.jar

(Or remove the ```setuid``` and ```setgid``` entries in ```corenlpd.conf``` and run as the invoking user at your own risk.)

Then copy ```corenlpd.conf``` to ```/etc/init``` to install the [Upstart](http://upstart.ubuntu.com/) job and run:

	service corenlpd start

## Running

To run as a server:

    lein ring server

Or:

	lein run

## Testing

	lein test

## API

Currently, there is one endpoint:

	GET /parse

It takes one argument, ```text``` and returns the parsed XML:

	GET /parse?text=This+is+a+sentence.

## License

See LICENSE.md

Copyright © 2015 Lucian Hontau
