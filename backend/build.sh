#!/usr/bin/env bash

mvn -B package -DskipTests
cp src/main/docker/Dockerfile target/
docker login ghcr.io -u $GITHUB_ACTOR -p $GITHUB_TOKEN
docker build --tag ghcr.io/$GITHUB_REPOSITORY/backend:latest ./target
docker push ghcr.io/$GITHUB_REPOSITORY/backend:latest