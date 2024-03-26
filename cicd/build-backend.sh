#!/usr/bin/env bash

set -e

pushd backend
    mvn clean package
popd

mkdir -p dist/backend

cp backend/target/*-runner.jar dist/backend/leovote-backend.jar
