package com.chimpler.simtick.readers;

import java.util.Objects;

public class ValueAndLength<V> {
    public V value;
    public int length;

    public ValueAndLength() {}

    public ValueAndLength(V value, int length) {
        withValueAndLength(value, length);
    }

    public ValueAndLength withValueAndLength(V value, int length) {
        this.value = value;
        this.length = length;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.hashCode(), length);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ValueAndLength) {
            ValueAndLength valueAndLength = (ValueAndLength)obj;
            return this.value.equals(valueAndLength.value) && this.length == valueAndLength.length;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("ValueAndLength(%s, %d)", value, length);
    }
}
