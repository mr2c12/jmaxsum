#!/usr/bin/env bash

# Max-Sum experiments

OUTPUT="msiter.csv"
NR=50

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
	echo -n $C >> $OUTPUT
	for r in `seq 1 $NR`
	do
		TMP=`mktemp`
		java -jar ../jmaxsum.jar --generators $M --loads $D --ancillary $R --center $C --width $W --report $TMP
		ITER=`grep "total number of iteration" $TMP | cut -d = -f 2`
		rm $TMP
		echo -n ",$ITER" >> $OUTPUT
	done
	echo "" >> $OUTPUT
done
