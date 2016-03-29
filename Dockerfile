FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER Lucian Hontau <lhontau@unifiedcompliance.com>

# System Deps
RUN apk update
RUN apk upgrade
RUN apk add \
    bash \
    ca-certificates \
    wget

# Leiningen
WORKDIR /usr/bin
RUN wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein
RUN chmod 755 lein
ENV LEIN_ROOT 1
RUN ./lein

# Copy files
RUN mkdir -p /usr/local/bin/corenlpd
WORKDIR /usr/local/bin/corenlpd
COPY . /usr/local/bin/corenlpd

# Build jar
RUN lein uberjar

# Expose ports
ENV port 5900
EXPOSE 5900

CMD java -jar /usr/local/bin/corenlpd/target/corenlpd.jar