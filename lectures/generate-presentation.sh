#!/usr/bin/env bash

if [ "$#" -ne 1 ]; then
  echo "Usage: ./generate-presentation.sh <presentation-source>"
  exit
fi

lecture_file="$1"
lecture="${lecture_file%.*}"

pandoc -t revealjs \
       -s \
       -o "$lecture".html \
       "$lecture_file" \
       -V revealjs-url=reveal-js \
       -V theme=white \
       --css=theme/theme.css \
       -V transition=fade \
       -V center=false
