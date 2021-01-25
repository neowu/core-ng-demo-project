FROM ubuntu:groovy
RUN apt-get update && apt-get install -y software-properties-common man && rm -rf /var/lib/apt/lists/*
RUN apt-key adv --fetch-keys https://adoptopenjdk.jfrog.io/adoptopenjdk/api/gpg/key/public && add-apt-repository --yes https://adoptopenjdk.jfrog.io/adoptopenjdk/deb/
RUN apt-get update && apt-get install -y \
     adoptopenjdk-15-hotspot \
     wget curl git tmux \
     iproute2 net-tools dnsutils iputils-ping netcat traceroute tcpdump watch htop sysstat lsof \
     vim \
     nghttp2-client wrk jq \
     && rm -rf /var/lib/apt/lists/*
CMD ["sleep", "infinity"]
