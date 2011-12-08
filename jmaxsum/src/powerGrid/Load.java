/*
 *  Copyright (C) 2011 Michele Roncalli <roncallim at gmail dot com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package powerGrid;

import java.util.HashMap;
import java.util.HashSet;

/**
 * A load in the power grid.
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class Load {

    protected int id;
    protected double requiredPower;
    HashSet<Generator> generators;
    protected static HashMap<Integer, Load> loadsMap = new HashMap<Integer, Load>();
    // TODO: lastid
    static int lastId = -1;

    private Load(int id, double power) {
        this.id = id;
        this.requiredPower = power;
        this.generators = new HashSet<Generator>();
    }

    public synchronized static Load getLoad(int id, double power) {
        if (!Load.loadsMap.containsKey(id)) {
            Load.loadsMap.put(id, new Load(id, power));
        }
        lastId = id;
        return Load.loadsMap.get(id);
    }

    public static void resetLoads(){
        Load.loadsMap = new HashMap<Integer, Load>();
        lastId=-1;
    }

    public static Load getNextLoad(double power) {
        int id = lastId + 1 ;
        while (Load.loadsMap.containsKey(id)) {
            id++;
        }
        return Load.getLoad(id, power);
    }

    public double getRequiredPower() {
        return requiredPower;
    }

    public void setRequiredPower(double power) {
        this.requiredPower = power;
    }

    public int getId() {
        return id;
    }

    public boolean addGenerator(Generator g) {
        return this.generators.add(g);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Load other = (Load) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return ("load_" + this.getId()).hashCode();
    }

    @Override
    public String toString() {
        return "Load{" + "id=" + id + " requiredPower=" + requiredPower + '}';
    }

    public String getGeneratorsList() {
        StringBuilder sb = new StringBuilder();
        for (Generator gn : this.generators) {
            sb.append("-- " + gn + "\n");
        }
        return sb.toString();
    }

    public int howManyGenerators(){
        return this.generators.size();
    }

    public HashSet<Generator> getGenerators() {
        return generators;
    }



}
