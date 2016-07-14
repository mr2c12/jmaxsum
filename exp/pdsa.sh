#!/usr/bin/env bash

# Experiments to determine the best p for DSA

SUBDIR=p

# Parameters of the model

M=200
D=3
R=2
C=0.29
W=0.2

mkdir -p $SUBDIR

for DSA in a b c d e
do
	if [ -f $DSA.csv ]
	then
		rm $DSA.csv # clear previous experiments
	fi
	for P in 0 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 1.0
	do
		for SEED in 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20
		do
			TMP=`mktemp`
			java -jar ../jmaxsum.jar --generators $M --loads $D --ancillary $R --center $C --width $W --seed $SEED --algorithm dsa --dsa $DSA --probability $P --report $TMP
			VALUE=`grep "Value=" $TMP | cut -d \; -f 1 | cut -d = -f 2`
			rm $TMP
			if [ $SEED -ne 1 ]
			then
				echo -n "," >> $DSA.csv
			fi
			echo -n $VALUE >> $DSA.csv
		done
		echo >> $DSA.csv
	done
done
