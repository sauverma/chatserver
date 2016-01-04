from ubuntu:trusty

#RUN sudo apt-get install -y wget git && wget https://storage.googleapis.com/golang/go1.5.2.linux-amd64.tar.gz \
#	&& tar -C /usr/local -xzf go*.tar.gz \
#	&& export GOROOT=/usr/local/go \
#	&& export PATH=$PATH:$GOROOT/bin 

ADD src/main modules/