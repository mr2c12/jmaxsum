import sys
import csv

with open(sys.argv[1], "r") as f_input:
    lmin_col = []
    lmin_row = []
    reader = csv.reader(f_input)
    next(reader, None)  # skip the headers

    for row in reader:
        row = map(float, row)
        lmin_row.append(min(row))

        if lmin_col:
            lmin_col = map(min, lmin_col, row)
        else:
            lmin_col = row

    print(lmin_col[::2])
