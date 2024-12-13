#!/usr/bin/env bash

mvn -B package
cp backend/src/main/docker/Dockerfile target/
docker login ghcr.io -u $GITHUB_ACTOR -p $GITHUB_TOKEN
docker build --tag ghcr.io/$GITHUB_REPOSITORY/deploy-pvc:latest ./target
docker push ghcr.io/$GITHUB_REPOSITORY/deploy-pvc:latest