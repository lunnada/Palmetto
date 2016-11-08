/**
 * This file is part of Palmetto.
 *
 * Palmetto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Palmetto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Palmetto.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.calculations.direct;

import org.aksw.palmetto.data.SubsetProbabilities;

/**
 * This confirmation measure calculates the difference between the conditional
 * probability of W' given W* abd the marginal probability of W'. result =
 * P(W'|W*)-P(W')
 * 
 * @author Michael Röder
 * 
 */
public class DifferenceBasedConfirmationMeasure implements DirectConfirmationMeasure {

    @Override
    public double[] calculateConfirmationValues(SubsetProbabilities subsetProbabilities) {
        int pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            pos += subsetProbabilities.conditions[i].length;
        }
        double values[] = new double[pos];

        double marginalProbability, conditionalProbability;
        pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            marginalProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]];
            if (marginalProbability > 0) {
                for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                    if (subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]] > 0) {
                        conditionalProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]
                                | subsetProbabilities.conditions[i][j]]
                                / subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                    } else {
                        conditionalProbability = 0;
                    }
                    values[pos] = conditionalProbability - marginalProbability;
                    ++pos;
                }
            } else {
                pos += subsetProbabilities.conditions[i].length;
            }
        }
        return values;
    }

    @Override
    public String getName() {
        return "m_d";
    }
}