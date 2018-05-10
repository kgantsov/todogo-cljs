#!/usr/bin/env bash

NAME="todogo_cljs"
TAG=latest
USER="kgantsov"
DOCKER_ID_USER="kgantsov"

rm -Rf resources/public/js/compiled
lein cljsbuild once min
lein less once

docker build -f Dockerfile-caddy -t $USER/$NAME:$TAG --no-cache .


docker tag $USER/$NAME:$TAG $USER/$NAME:$TAG
docker push $USER/$NAME:$TAG


docker rmi $USER/$NAME:$TAG
