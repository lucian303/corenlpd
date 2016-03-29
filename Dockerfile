FROM ubuntu:14.04
MAINTAINER Lucian Hontau <lhontau@unifiedcompliance.com>

# Init
ENV DEBIAN_FRONTEND=noninteractive
RUN mkdir -p /tmp/corenlpd
WORKDIR /tmp/corenlpd

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

# Leiningen
RUN wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein
RUN chmod 755 lein
ENV LEIN_ROOT 1

# Copy files
COPY . /tmp/corenlpd

# Build jar
RUN ./lein uberjar
RUN cp -f /tmp/corenlpd/target/corenlpd.jar /usr/local/bin/corenlpd.jar

# Cleanup
WORKDIR /var/log
RUN rm -rf /tmp/corenlpd

# Expose ports
ENV port 5900
EXPOSE 5900

CMD java -jar /usr/local/bin/corenlpd.jar