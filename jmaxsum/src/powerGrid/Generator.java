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
 * A generator of the power grid.
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class Generator {

    protected int id;
    protected double power;
    protected static HashMap<Integer, Generator> generatorsMap = new HashMap<Integer, Generator>();
    HashSet<Load> loads;

    private Generator(int id, double power) {
        this.id = id;
        this.power = power;
        this.loads = new HashSet<Load>();
    }

    public static Generator getGenerator(int id, double power) {
        if (!Generator.generatorsMap.containsKey(id)) {
            Generator.generatorsMap.put(id, new Generator(id, power));
        }
        return Generator.generatorsMap.get(id);
    }

    public static Generator getNextGenerator(double power) {
        int id = 0;
        while (Generator.generatorsMap.containsKey(id)) {
            id++;
        }
        return Generator.getGenerator(id, power);
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public int getId() {
        return id;
    }

    public boolean addLoad(Load l) {
        return this.loads.add(l);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Generator other = (Generator) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return ("generator_" + this.getId()).hashCode();
    }

    @Override
    public String toString() {
        return "Generator{" + "id=" + id + " power=" + power + '}';
    }

    public String getLoadsList(){
        StringBuilder sb = new StringBuilder();
        for (Load ld : this.loads){
            sb.append("-- "+ld+"\n");
        }
        return sb.toString();
    }

    public HashSet<Load> getLoads() {
        return loads;
    }
    

    public int howManyLoads(){
        return this.loads.size();
    }

    public boolean hasLoad(Load l){
        return this.loads.contains(l);
    }
}
