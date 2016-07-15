#!/usr/bin/env bash

# Max-Sum experiments

OUTPUT="maxsum.csv"

# Parameters of the model

M=200
D=3
R=2
W=0.2

if [ -f $OUTPUT ]
then
	rm $OUTPUT # clear previous experiments
fi

for C in 0.290 0.291 0.292 0.293 0.294 0.295 0.296 0.297 0.298 0.299 0.300
do
	for SEED in `seq 1 100`
	do
		TMP=`mktemp`
		java -jar ../jmaxsum.jar --generators $M --loads $D --ancillary $R --center $C --width $W --seed $SEED --report $TMP --time
		VALUE=`grep "Value=" $TMP | cut -d \; -f 1 | cut -d = -f 2`
		TIME=`grep "Time" $TMP | cut -d\  -f 3`
		rm $TMP
		if [ $SEED -ne 1 ]
		then
			echo -n "," >> $OUTPUT
		fi
		echo -n $VALUE,$TIME >> $OUTPUT
	done
	echo >> $OUTPUT
done
