JMaxSum
===================
JMaxSum is a Java implementation of the Max-Sum algorithm that solves the power distribution model proposed by [Zdeborová et al. (2009)](http://journals.aps.org/pre/abstract/10.1103/PhysRevE.80.046112).

Compilation
----------
JMaxSum requires `ant` to compile. The source code can be compiled into a jar with `ant compile jar`.

Execution
----------
JMaxSum can be executed with the following syntax:

    java -jar jmaxsum.jar --generators M --loads D --ancillary R --center C --width W

The following *required* parameters must be specified:

    --generators M	The number of generators
    --loads D		The number of loads connected to each generator
    --ancillary R	The number ancillary lines for each generator
    --center C		The center of the uniform distribution
    --width W		The width of the uniform distribution

In addition, the following *optional* parameters can be specified:

    --algorithm A	Either "maxsum", "annealing" or "dsa" (default: "maxsum")
    --dsa V			DSA version, can be "a" -- "e" (required when using DSA as algorithm)
    --oplus OP		The oplus operator, either "max" or "min" (default: "min")
    --otimes OT		The otimes operator, only "sum" available ATM (default: "sum")
    --seed S		Seed used to generate the random instance (default: random)
    --iterations I	The number of iterations of the algorithm (default: 300)
    --printfactor	Print both original factor graph and after the bounded phase
    --screw			Use the preferences-on-values hack
    --bounded		Use the Bounded Max Sum phase
    --time			Print total time usage
    --report FILE	Write the report of the execution on FILE
