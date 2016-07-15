#!/usr/bin/env bash

# DSA experiments

SUBDIR=dsa

# Parameters of the model

M=200
D=3
R=2
W=0.2

for DSA in c e
do
	for C in 0.290 0.291 0.292 0.293 0.294 0.295 0.296 0.297 0.298 0.299 0.300
	do	
		mkdir -p $SUBDIR/$C

		if [ -f $SUBDIR/$C/$DSA.csv ]
		then
			rm $SUBDIR/$C/$DSA.csv # clear previous experiments
		fi

		for P in 0.5 0.6 0.7 0.8 0.9 1.0
		do
			for SEED in `seq 1 100`
			do
				TMP=`mktemp`
				java -jar ../jmaxsum.jar --generators $M --loads $D --ancillary $R --center $C --width $W --seed $SEED --algorithm dsa --dsa $DSA --probability $P --report $TMP --time
				VALUE=`grep "Value=" $TMP | cut -d \; -f 1 | cut -d = -f 2`
				TIME=`grep "Time" $TMP | cut -d\  -f 3`
				rm $TMP
				if [ $SEED -ne 1 ]
				then
					echo -n "," >> $SUBDIR/$C/$DSA.csv
				fi
				echo -n $VALUE,$TIME >> $SUBDIR/$C/$DSA.csv
			done
			echo >> $SUBDIR/$C/$DSA.csv
		done
	done
done
