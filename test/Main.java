package test;

import boundedMaxSum.BoundedMaxSum;
import boundedMaxSum.InstanceCloner;
import hacks.ScrewerUp;
import java.util.Random;
import maxsum.MS_COP_Instance;
import misc.Utils;
import system.COP_Instance;
import olimpo.Cerberus;
import olimpo.Athena;
import olimpo.Eris;
import operation.Solver;
import powerGrid.PowerGrid;

/**
 * Class used for test executions.
 * @author Michele Roncalli
 */
public class Main {

	static final int debug = test.DebugVerbosity.debugMain;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();
		System.out.println("Started");

		try {
			boolean powerGridV = true;
			boolean oldformatV = false;
			boolean screwupV = false;
			boolean boundedV = false;
			String solverV = "athena";
			String oplusV = "min";
			String otimesV = "sum";

			int iterationsV = 300;

			boolean stepbystepV = false;
			boolean updateelV = false;

			String reportV = null;
			COP_Instance original_cop;
			PowerGrid pg = new PowerGrid(200, 3, 2, 0.29, 0.2);
			original_cop = pg.getCopMnotInfNoCo2();
			InstanceCloner ic = null;
			COP_Instance cop;

			if (screwupV || boundedV) {
				ic = new InstanceCloner(original_cop);
				cop = ic.getClonedInstance();
			} else {
				cop = original_cop;
			}

			ScrewerUp screwerup = null;

			if (screwupV) {
				screwerup = new ScrewerUp(cop);
				cop = screwerup.screwItUp();
				System.out.println("Screwed istance:\n"+cop.toStringFile());
			}

			BoundedMaxSum BMax = null;

			if (boundedV) {
				// time for BoundedMaxSum to do his best!
				BMax = new BoundedMaxSum(cop.getFactorgraph());
				//BMax.weightTheGraph();
				BMax.letsBound();
			}

			Solver core = null;

			if (solverV.equalsIgnoreCase("athena")) {
				core = new Athena(cop, oplusV, otimesV);
			} else if (solverV.equalsIgnoreCase("eris")) {
				core = new Eris(oplusV, cop);
			}

			core.setIterationsNumber(iterationsV);
			core.setStepbystep(stepbystepV);
			core.setUpdateOnlyAtEnd(!stepbystepV);
			core.setUpdateOnlyAtEnd(!updateelV);

			if (reportV != null) {
				core.pleaseReport(reportV);
			}

			core.solve();

			if (screwupV || boundedV) {
				// set the variables value to the original instance
				ic.setOriginalVariablesValues();
			}

			System.out.println("original value(cop)= " + cop.actualValue());

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Finish in " + ((System.currentTimeMillis() - startTime) / (double) 1000) + " s");
	}
}
