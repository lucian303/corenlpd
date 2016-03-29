FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER Lucian Hontau <lhontau@unifiedcompliance.com>

# Init
ENV DEBIAN_FRONTEND=noninteractive
RUN mkdir -p /tmp/corenlpd
WORKDIR /tmp/corenlpd

# System Deps
RUN apk update
RUN apk upgrade
RUN apk add \
    bash \
    ca-certificates \
    wget

# Leiningen
RUN wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein
RUN chmod 755 lein
ENV LEIN_ROOT 1

# Copy files
COPY . /tmp/corenlpd

# Build jar
RUN ./lein uberjar
RUN mv /tmp/corenlpd/target/corenlpd.jar /usr/local/bin/corenlpd.jar

# Cleanup
WORKDIR /var/log
RUN rm -rf /tmp/corenlpd

# Expose ports
ENV port 5900
EXPOSE 5900

CMD java -jar /usr/local/bin/corenlpd.jar