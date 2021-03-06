/**
 * Palmetto - Palmetto is a quality measure tool for topics.
 * Copyright © 2014 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.prob;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.corpus.WindowSupportingAdapter;
import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.prob.window.ContextWindowFrequencyDeterminer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

@RunWith(Parameterized.class)
public class ContextWindowFrequencyDeterminerCountingTest implements WindowSupportingAdapter {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        /*
                         * Lets start with a simple test document
                         * A B C B A C C
                         */
                        // We ask for A and B with a window size of +-1
                        { 7, new int[][] { { 0, 4 }, { 1, 3 }, {} }, 1,
                                new int[] { 0, 2, 2, 2, 0, 0, 0, 0 } },
                        // We ask for A and C with a window size of +-1
                        { 7, new int[][] { { 0, 4 }, {}, { 2, 5, 6 } }, 1,
                                new int[] { 0, 2, 0, 0, 3, 1, 0, 0 } },
                        // We ask for B and C with a window size of +-1
                        { 7, new int[][] { {}, { 1, 3 }, { 2, 5, 6 } }, 1,
                                new int[] { 0, 0, 2, 0, 3, 0, 2, 0 } },
                        // We ask for A, B and C with a window size of +-1
                        { 7, new int[][] { { 0, 4 }, { 1, 3 }, { 2, 5, 6 } }, 1,
                                new int[] { 0, 2, 2, 2, 3, 1, 2, 0 } },
                        // We ask for A and B with a window size of +-2
                        { 7, new int[][] { { 0, 4 }, { 1, 3 }, {} }, 2,
                                new int[] { 0, 2, 2, 2, 0, 0, 0, 0 } },
                        // We ask for A and C with a window size of +-2
                        { 7, new int[][] { { 0, 4 }, {}, { 2, 5, 6 } }, 2,
                                new int[] { 0, 2, 0, 0, 3, 4, 0, 0 } },
                        // We ask for B and C with a window size of +-2
                        { 7, new int[][] { {}, { 1, 3 }, { 2, 5, 6 } }, 2,
                                new int[] { 0, 0, 2, 0, 3, 0, 3, 0 } },
                        // We ask for A, B and C with a window size of +-2
                        { 7, new int[][] { { 0, 4 }, { 1, 3 }, { 2, 5, 6 } }, 2,
                                new int[] { 0, 2, 2, 2, 3, 4, 3, 0 } },
                        // We have a new very short document A B C
                        { 3, new int[][] { { 0 }, { 1 }, { 2 } }, 2,
                                new int[] { 0, 1, 1, 1, 1, 1, 1, 0 } }

                });
    }

    private int histogram[][];
    private int docLength;
    private int positions[][];
    private int windowSize;
    private int expectedCounts[];

    public ContextWindowFrequencyDeterminerCountingTest(int docLength, int[][] positions, int windowSize,
            int expectedCounts[]) {
        this.docLength = docLength;
        this.histogram = new int[][] { { docLength, 1 } };
        this.positions = positions;
        this.windowSize = windowSize;
        this.expectedCounts = expectedCounts;
    }

    @Test
    public void test() {
        ContextWindowFrequencyDeterminer determiner = new ContextWindowFrequencyDeterminer(this,
                windowSize);
        IntArrayList lists[] = new IntArrayList[positions.length];
        for (int i = 0; i < lists.length; ++i) {
            if (positions[i] != null) {
                lists[i] = new IntArrayList(positions[i].length);
                lists[i].add(positions[i]);
            }
        }
        int counts[] = determiner.determineCounts(
                new String[1][lists.length]/* new String[][] { { "A", "B", "C" } } */,
                new SegmentationDefinition[] { new SegmentationDefinition(
                        new int[0], new int[0][0], null) })[0].counts;
        Assert.assertArrayEquals(expectedCounts, counts);
    }

    @Override
    public int[][] getDocumentSizeHistogram() {
        return histogram;
    }

    @Override
    public IntObjectOpenHashMap<IntArrayList[]> requestWordPositionsInDocuments(String[] words,
            IntIntOpenHashMap docLengths) {
        IntObjectOpenHashMap<IntArrayList[]> positionsInDocuments = new IntObjectOpenHashMap<IntArrayList[]>();
        IntArrayList[] positionsInDocument = new IntArrayList[positions.length];
        for (int i = 0; i < positionsInDocument.length; ++i) {
            if ((positions[i] != null) && (positions[i].length > 0)) {
                positionsInDocument[i] = new IntArrayList();
                positionsInDocument[i].add(positions[i]);
            }
        }
        positionsInDocuments.put(0, positionsInDocument);
        docLengths.put(0, docLength);
        return positionsInDocuments;
    }

    @Override
    public void close() {
    }
}
