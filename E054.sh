#!/bin/bash

DEST=/tmp/classes
mkdir $DEST 2>/dev/null
javac -cp "lib/*" -d $DEST -sourcepath src $(find src -name "*.java")
