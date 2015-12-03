# corenlpd

[![Build Status](https://travis-ci.org/lucian303/corenlpd.svg?branch=master)](https://travis-ci.org/lucian303/corenlpd)

An HTTP REST server frontend for [Stanford Core NLP](http://nlp.stanford.edu/software/corenlp.shtml) written in Clojure using the [Luminus](http://www.luminusweb.net/) framework.

## Prerequisites

You will need [Leiningen](http://leiningen.org/) 2.0 or above installed.

## Installation

You can install this on a normal machine or run it on AWS Elastic Beanstalk (see below).

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

Then copy ```corenlpd.conf``` to ```/etc/init``` to install the [Upstart](http://upstart.ubuntu.com/) job and run:

	service corenlpd start

## AWS Elastic Beanstalk Deployment

See the documentation for environment variables and the command line needed to set them. These only need to be set once.

You'll need to create a Tomcat 8 / Java 8 AWS environment (otherwise it'll default to Java 7 and fail). After than, when you have the repo, switch to the `master` branch and:

    eb init

Choose `us-east-1` for the region and `corenlpd` for the app (change as needed). The `init` and `create` steps only have to be done once.

    eb create corenlpd-prod
    lein beanstalk deploy corenlpd-prod

Please make sure you're in the `master` branch when deploying to avoid deploying the wrong branch! After the first deploy, from the `master` branch just do:

    lein beanstalk deploy corenlpd-prod

Note that lein must be used for deployment. `eb deploy` does not properly set things up to run this Clojure app as it assumes a Java app.

## Running

To run as a server:

    lein ring server

Or:

	lein run

By default, the server runs on port ```5900``` for testing. In production, set the `PORT` environment variable.

## Testing

	lein test

## API

### Endpoints

#### `GET /parse`

It takes one argument, ```text``` and returns the parsed XML:

	GET /parse?text=This+is+a+sentence.

#### `GET /parse-with-pos`

It takes one argument, ```text``` and returns the parsed XML. The argument must have POS tags seperated by a slash:

	GET /parse-with-pos?text=Document/VB+software/NN+./.


## License

MIT (See LICENSE)

Copyright © 2015 Lucian Hontau
