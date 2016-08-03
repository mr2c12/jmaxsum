import sys
import csv
import numpy

for x in range(290, 301):
	xf = "%.3f" % (x/1000.0)
	f_input = open(sys.argv[1] + "/" + xf + "/c-v.csv", "r")
	reader = csv.reader(f_input)
	lmin_col = []

	for row in csv.reader(f_input):
		row = map(float, row)
		if lmin_col:
			lmin_col = map(min, lmin_col, row)
		else:
			lmin_col = row
	
	print "%.3f,%f,%f" % (x/1000.0, numpy.ma.masked_invalid(lmin_col).mean(), numpy.ma.masked_invalid(lmin_col).std())

print

for x in range(290, 301):
	xf = "%.3f" % (x/1000.0)
	f_input = open(sys.argv[1] + "/" + xf + "/e-v.csv", "r")
	reader = csv.reader(f_input)
	lmin_col = []

	for row in csv.reader(f_input):
		row = map(float, row)
		if lmin_col:
			lmin_col = map(min, lmin_col, row)
		else:
			lmin_col = row
	
	print "%.3f,%f,%f" % (x/1000.0, numpy.ma.masked_invalid(lmin_col).mean(), numpy.ma.masked_invalid(lmin_col).std())
