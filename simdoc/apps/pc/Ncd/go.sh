#!/bin/sh

rm -f *.csv* *.json*
java -Xmx5G -jar build/libs/Ncd-all.jar $1 $2 $3 $4 $5 $6
