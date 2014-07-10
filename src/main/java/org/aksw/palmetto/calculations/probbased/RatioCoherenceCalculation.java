/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International Public License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, a file
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aksw.palmetto.calculations.probbased;

import org.aksw.palmetto.data.SubsetProbabilities;

/**
 * This confirmation measure calculates the ratio between the joint probability
 * of W' and W* and the product of the two marginal probabilities. result =
 * P(W',W*)/(P(W')*P(W*))
 * 
 * @author Michael Röder
 * 
 */
public class RatioCoherenceCalculation extends AbstractUndefinedResultHandlingConfirmationMeasure {

    public RatioCoherenceCalculation() {
        super();
    }

    public RatioCoherenceCalculation(double resultIfCalcUndefined) {
        super(resultIfCalcUndefined);
    }

    @Override
    public double[] calculateCoherenceValues(SubsetProbabilities subsetProbabilities) {
        int pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            pos += subsetProbabilities.conditions[i].length;
        }
        double values[] = new double[pos];

        double segmentProbability, conditionProbability, intersectionProbability;
        pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            segmentProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]];
            if (segmentProbability > 0) {
                for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                    conditionProbability = subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                    intersectionProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]
                            | subsetProbabilities.conditions[i][j]];
                    if (conditionProbability > 0) {
                        values[pos] = intersectionProbability / (segmentProbability * conditionProbability);
                    } else {
                        values[pos] = resultIfCalcUndefined;
                    }
                    ++pos;
                }
            } else {
                for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                    values[pos] = resultIfCalcUndefined;
                    ++pos;
                }
            }
        }
        return values;
    }

    @Override
    protected String getName() {
        return "m_r";
    }
}
