#!/usr/bin/env bash

# Max-Sum experiments

SUBDIR=ms

# Parameters of the model

D=3
R=2
W=0.2

mkdir -p $SUBDIR

for M in 200 1000 2000 10000 20000
do
	if [ -f "$SUBDIR/$M-v.csv" ]
	then
		rm "$SUBDIR/$M-v.csv" # clear previous experiments
	fi

	if [ -f "$SUBDIR/$M-i.csv" ]
	then
		rm "$SUBDIR/$M-i.csv" # clear previous experiments
	fi

	if [ -f "$SUBDIR/$M-t.csv" ]
	then
		rm "$SUBDIR/$M-t.csv" # clear previous experiments
	fi

	for C in 0.290 0.291 0.292 0.293 0.294 0.295 0.296 0.297 0.298 0.299 0.300
	do
		for SEED in `seq 1 100`
		do
			TMP=`mktemp`
			java -jar ../jmaxsum.jar --generators $M --loads $D --ancillary $R --center $C --width $W --seed $SEED --report $TMP --time
			VALUE=`grep "Value=" $TMP | cut -d \; -f 1 | cut -d = -f 2`
			ITER=`grep "total number of iteration" $TMP | cut -d = -f 2`
			TIME=`grep "Time" $TMP | cut -d\  -f 3`
			rm $TMP
			if [ $SEED -ne 1 ]
			then
				echo -n "," >> "$SUBDIR/$M-v.csv"
				echo -n "," >> "$SUBDIR/$M-i.csv"
				echo -n "," >> "$SUBDIR/$M-t.csv"
			fi
			echo -n $VALUE >> "$SUBDIR/$M-v.csv"
			echo -n $ITER >> "$SUBDIR/$M-i.csv"
			echo -n $TIME >> "$SUBDIR/$M-t.csv"
		done
		echo >> "$SUBDIR/$M-v.csv"
		echo >> "$SUBDIR/$M-i.csv"
		echo >> "$SUBDIR/$M-t.csv"
	done
done

