JMaxSum
===================
JMaxSum is a Java implementation of the Max-Sum algorithm that solves the power distribution model proposed by [Zdeborová et al. (2009)](http://journals.aps.org/pre/abstract/10.1103/PhysRevE.80.046112). This package also includes an implementation of simulated annealing [(Kirkpatrick et al., 1983)](http://science.sciencemag.org/content/220/4598/671) and distributed stochastic search [(Zhang et al., 2004)](http://ac.els-cdn.com/S0004370204001481/1-s2.0-S0004370204001481-main.pdf?_tid=c1dd4508-48ec-11e6-9e88-00000aacb362&acdnat=1468409418_719aadebd6eab7e49029ed25e222739e) for comparison purposes.

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
    --probability P	Probability between 0 and 1 used in DSA (default: 0.5)
    --oplus OP		The oplus operator, either "max" or "min" (default: "min")
    --otimes OT		The otimes operator, only "sum" available ATM (default: "sum")
    --seed S		Seed used to generate the random instance (default: random)
    --iterations I	The number of iterations of the algorithm (default: 300)
    --printfactor	Print both original factor graph and after the bounded phase
    --screw			Use the preferences-on-values hack
    --bounded		Use the Bounded Max Sum phase
    --time			Print total time usage
    --report FILE	Write the report of the execution on FILE
