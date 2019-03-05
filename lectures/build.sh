#!/usr/bin/env bash

shopt -s extglob

for lecture_file in *.@(md|rst); do
  ./generate-presentation.sh "$lecture_file"
done
