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

package function;

/**
 * Very simple implementation of CO2 emission function.
 * The emission is:
 * (consumption/maxPoxer)*mult
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class CO2Simple implements CO2Function{

    protected double mult=1;
    protected double maxPower;

    public CO2Simple(double maxPower, double mult) {
        this.maxPower = maxPower;
        this.mult = mult;
    }



    public double emission(double consumption) {

        return (consumption/this.maxPower)*this.mult;

    }

    @Override
    public String toString() {
        return "CO2Simple{" + "mult=" + mult + "maxPower=" + maxPower + '}';
    }

    public double getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(double maxPower) {
        this.maxPower = maxPower;
    }

    public double getMult() {
        return mult;
    }

    public void setMult(double mult) {
        this.mult = mult;
    }


    

}
