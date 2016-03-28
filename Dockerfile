FROM ubuntu:14.04
MAINTAINER Lucian Hontau <lhontau@unifiedcompliance.com>

# Init
ENV DEBIAN_FRONTEND=noninteractive

# System Deps
RUN apt-get install -y \
    software-properties-common
RUN add-apt-repository ppa:webupd8team/java
RUN apt-get update
# RUN apt-get upgrade -y
# Accept Java license to avoid prompt
RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections
RUN apt-get install -y \
    oracle-java8-installer \
    wget

# Copy files
RUN mkdir -p /usr/local/bin/corenlpd
COPY . /usr/local/bin/corenlpd
WORKDIR /usr/local/bin/corenlpd

# Leiningen
RUN wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein
RUN chmod 755 lein
ENV LEIN_ROOT 1

# Build jar
RUN ./lein uberjar

# Expose ports
ENV port 5900
EXPOSE 5900

CMD java -jar /usr/local/bin/corenlpd/target/corenlpd.jar