#!/bin/bash

cp $1 app.db
docker build -t una-tabla-db .
docker run -it una-tabla-db