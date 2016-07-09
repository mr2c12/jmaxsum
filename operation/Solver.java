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

package operation;

import exception.PostServiceNotSetException;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public interface Solver {

    /**
     * Please report all the information and put them in file
     * @param file report path
     */
    void pleaseReport(String file);

    /**
     * How many steps to do?
     * @param n number of steps.
     */
    void setIterationsNumber(int n);

    void setStepbystep(boolean stepbystep);

    /**
     * Set if the update is only after the algorithm is finished or after each step.
     * @param updateOnlyAtEnd yes if to update only when finished.
     */
    void setUpdateOnlyAtEnd(boolean updateOnlyAtEnd);

    void solve() throws PostServiceNotSetException;


}
