/*
 * Copyright (C) 2014-2021 D3X Systems - All Rights Reserved
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
package com.d3x.morpheus.vector;

import com.d3x.morpheus.util.DoubleComparator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * An immutable element in a DataVectorView.
 */
@AllArgsConstructor(staticName = "of")
public final class DataVectorElement<K> {
    /**
     * The key of this vector element.
     */
    @Getter @NonNull private final K key;

    /**
     * The value in this vector element.
     */
    @Getter @NonNull private final double value;

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        return (obj instanceof DataVectorElement) && equalsElement((DataVectorElement<K>) obj);
    }

    private boolean equalsElement(DataVectorElement<K> that) {
        return this.key.equals(that.key) && DoubleComparator.DEFAULT.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return key + " => " + Double.toString(value);
    }
}
