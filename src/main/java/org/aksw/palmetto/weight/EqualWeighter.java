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
package org.aksw.palmetto.weight;

import java.util.Arrays;

import org.aksw.palmetto.data.SubsetProbabilities;

@Deprecated
public class EqualWeighter implements Weighter {

    @Override
    public double[] createWeights(SubsetProbabilities probabilities) {
        int pos = 0;
        for (int i = 0; i < probabilities.segments.length; ++i) {
            pos += probabilities.conditions[i].length;
        }
        double weights[] = new double[pos];
        Arrays.fill(weights, 1.0);
        return weights;
    }

    @Override
    public String getName() {
        return "E_e";
    }
}
