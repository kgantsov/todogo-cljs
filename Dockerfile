FROM clojure:onbuild

RUN mkdir -p /todogo/
ADD . /todogo/
WORKDIR /todogo/

CMD ["lein figwheel dev"]
