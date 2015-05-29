# corenlpd

[![Build Status](https://travis-ci.org/lucian303/corenlpd.svg?branch=master)](https://travis-ci.org/lucian303/corenlpd)

An HTTP REST server frontend for [Stanford Core NLP](http://nlp.stanford.edu/software/corenlp.shtml) written in Clojure using the [Luminus](http://www.luminusweb.net/) framework.

## Prerequisites

You will need [Leiningen](http://leiningen.org/) 2.0 or above installed.

## Installation

To install as an Upstart service, copy source to ```/usr/local/bin/corenlpd``` (or change path in ```corenlpd.conf```) and run:

	lein uberjar

Add a user for the service (as root):

	sudo adduser --disabled-password --gecos "" corenlpd

Add corenlpd to sudoers.d (as root):

	sudo sh -c "echo \"corenlpd ALL=NOPASSWD: /usr/local/bin/corenlpd/target/corenlpd.jar\" >> /etc/sudoers.d/corenlpd"
	sudo chmod 0440 /etc/sudoers.d/corenlpd

**OR** add corenlpd to suders directly (as root):

	sudo sh -c "echo \"corenlpd ALL=NOPASSWD: /usr/local/bin/corenlpd/target/corenlpd.jar\" >> /etc/sudoers"

(Or remove the ```setuid``` and ```setgid``` entries in ```corenlpd.conf``` and run as the invoking user at your own risk.)

Assign ownership of the app directory to the ```corenlpd``` user.

	sudo chown -R corenlpd:corenlpd /usr/local/bin/corenlpd

Then copy ```corenlpd.conf``` to ```/etc/init``` to install the [Upstart](http://upstart.ubuntu.com/) job and run:

	service corenlpd start

## Running

To run as a server:

    lein ring server

Or:

	lein run

By default, the server runs on port ```5900```.

## Testing

	lein test

## API

### Endpoints

#### GET /parse

It takes one argument, ```text``` and returns the parsed XML:

	GET /parse?text=This+is+a+sentence.

#### GET /parse-with-pos

It takes one argument, ```text``` and returns the parsed XML. The argument must have POS tags seperated by a slash:

	GET /parse-with-pos?text=Document/VB+software/NN+./.


## License

MIT (See LICENSE)

Copyright © 2015 Lucian Hontau
