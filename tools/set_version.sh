#!/usr/bin/env bash

# using XMLStarlet Toolkit

xml ed --ps --inplace -N x='http://maven.apache.org/POM/4.0.0' -u "//x:project/x:version" -v $1 pom.xml

xml ed --ps --inplace -u "//data/version" -v $1 dist/moduledata

xml ed --ps --inplace -u "//VASSAL.build.GameModule/@version" -v $1 dist/buildFile.xml