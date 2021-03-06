/*
 * Copyright (C) 2014-2018 D3X Systems - All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.d3x.morpheus.frame;

import com.d3x.morpheus.util.Asserts;

/**
 * A class that provides some useful methods to make various kinds of assertions on DataFrames which are useful for testing.
 *
 * <p>This is open source software released under the <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache 2.0 License</a></p>
 *
 * @author  Xavier Witdouck
 */
public class DataFrameAsserts {

    /**
     * Asserts two vectors are equal in terms of structure and values.
     * @param expected      the expected expected
     * @param actual        the actual frame
     */
    public static void assertEquals(DataFrameVector<?,?,?,?,?> expected, DataFrameVector<?,?,?,?,?> actual) {
        Asserts.assertEquals(actual.size(), expected.size(), "The DataFrameVector sizes match");
        Asserts.assertEquals(actual.key(), expected.key(), "The DataFrameVector keys match");
        for (int i=0; i<expected.size(); ++i) {
            final Object expectedValue = expected.getValueAt(i);
            final Object actualValue = actual.getValueAt(i);
            Asserts.assertEquals(actualValue, expectedValue, "The DataFrame values match at (" + i + "," + i + ")");
        }
    }

    /**
     * Asserts that two frames have the same row and column keys, ignoring the values in the DataFrames
     * @param actual    the actual frame
     * @param expected  the expected frame
     */
    public static void assertEqualStructure(DataFrame<?,?> actual, DataFrame<?,?> expected) {
        Asserts.assertEquals(actual.rowCount(), expected.rowCount(), "The DataFrame row counts match");
        Asserts.assertEquals(actual.colCount(), expected.colCount(), "The DataFrame column count match");
        Asserts.assertEquals(actual.rows().keyClass(), expected.rows().keyClass(), "The DataFrame row key types match");
        Asserts.assertEquals(actual.cols().keyClass(), expected.cols().keyClass(), "The DataFrame column key types match");
        for (int i=0; i<actual.rowCount(); ++i) {
            final Object rowKeyActual = actual.rows().key(i);
            final Object rowKeyExpected = expected.rows().key(i);
            Asserts.assertEquals(rowKeyActual, rowKeyExpected, "DataFrame row keys matched at index " + i);
        }
        for (int i = 0; i<actual.colCount(); ++i) {
            final Object colKeyActual = actual.cols().key(i);
            final Object colKeyExpected = expected.cols().key(i);
            Asserts.assertEquals(colKeyActual, colKeyExpected, "DataFrame column keys matched at index " + i);
        }
    }


    /**
     * Asserts two frames are equal in terms of structure and values based on indexes
     * @param actual        the actual frame
     * @param expected      the expected expected
     */
    @SuppressWarnings("unchecked")
    public static void assertEqualsByIndex(DataFrame<?,?> actual, DataFrame<?,?> expected) {
        DataFrameAsserts.assertEqualStructure(expected, actual);
        DataFrameCursor<?,?> actualCursor = actual.cursor();
        DataFrameCursor<?,?> expectedCursor = expected.cursor();
        for (int j = 0; j<expected.colCount(); ++j) {
            for (int i=0; i<expected.rowCount(); ++i) {
                final Object expectedValue = expectedCursor.atOrdinals(i,j).getValue();
                final Object actualValue = actualCursor.atOrdinals(i,j).getValue();
                if (expectedValue != null && actualValue != null) {
                    final Class type1 = expectedValue.getClass();
                    final Class type2 = actualValue.getClass();
                    Asserts.assertEquals(type2, type1, "The DataFrame value types match at (" + i + "," + j + ")");
                    if (type1.equals(Double.class)) {
                        final double v1 = ((Number)expectedValue).doubleValue();
                        final double v2 = ((Number)actualValue).doubleValue();
                        if (!Double.isNaN(v1) || !Double.isNaN(v2)) {
                            final double delta = Math.max(0.00000001, Math.min(Math.abs(v1), Math.abs(v2)) * 0.00000001);
                            Asserts.assertEquals(v2, v1, delta, "The DataFrame doubles match at (" + i + "," + j + ")");
                        }
                    } else {
                        Asserts.assertEquals(actualValue, expectedValue, "The DataFrame values match at (" + i + "," + j + ")");
                    }
                } else {
                    Asserts.assertEquals(actualValue, expectedValue, "The DataFrame values match at (" + i + "," + j + ")");
                }
            }
        }
    }


    /**
     * Asserts two frames are equal in terms of structure and values based on keys
     * @param actual        the actual frame
     * @param expected      the expected expected
     */
    public static <R,C> void assertEqualsByKey(DataFrame<R,C> expected, DataFrame<R,C> actual) {
        Asserts.assertEquals(actual.rowCount(), expected.rowCount(), "The DataFrame row counts match");
        Asserts.assertEquals(actual.colCount(), expected.colCount(), "The DataFrame column count match");
        for (int i=0; i<expected.rowCount(); ++i) {
            final R expectedRowKey = expected.rows().key(i);
            Asserts.assertTrue(actual.rows().contains(expectedRowKey), "DataFrame contains expected row key " + expectedRowKey);
        }
        for (int i = 0; i<expected.colCount(); ++i) {
            final C expectedColKey = expected.cols().key(i);
            Asserts.assertTrue(actual.cols().contains(expectedColKey), "DataFrame contains expected column " + expectedColKey);
        }
        expected.cols().forEach(column -> {
            DataFrameColumn<R,C> other = actual.col(column.key());
            column.forEachValue(v -> {
                var rowKey = v.rowKey();
                var colKey = v.colKey();
                var expectedValue = v.getValue();
                var actualValue = other.getValue(rowKey);
                if (expectedValue != null && actualValue != null) {
                    final Class type1 = expectedValue.getClass();
                    final Class type2 = actualValue.getClass();
                    Asserts.assertEquals(type2, type1, "The DataFrame value types match at (" + rowKey + "," + colKey + ")");
                    if (type1.equals(Double.class)) {
                        final double v1 = ((Number)expectedValue).doubleValue();
                        final double v2 = ((Number)actualValue).doubleValue();
                        if (!Double.isNaN(v1) || !Double.isNaN(v2)) {
                            final double delta = Math.max(0.00000001, Math.min(Math.abs(v1), Math.abs(v2)) * 0.00000001);
                            Asserts.assertEquals(v2, v1, delta, "The DataFrame doubles match at (" + rowKey + "," + colKey + ")");
                        }
                    } else {
                        Asserts.assertEquals(actualValue, expectedValue, "The DataFrame values match at (" + rowKey + "," + colKey + ")");
                    }
                } else {
                    Asserts.assertEquals(actualValue, expectedValue, "The DataFrame values match at (" + rowKey + "," + colKey + ")");
                }
            });
        });
    }

}
