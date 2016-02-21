package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.CodecFactory;
import com.chimpler.simtick.codec.LongCodec;
import org.joda.time.DateTime;

public class DateTimeReader extends Reader<DateTime> {
    private final LongCodec codec;
    private Long oldValue = Long.MAX_VALUE;
    private int divFactor;

    public DateTimeReader(DateTime minDate, DateTime maxDate, int minDeltaValues, int maxDeltaValues, boolean isMillis) {
        this.divFactor = isMillis ? 1 : 1000;
        this.codec = new CodecFactory().buildLongCodec(
                minDate.getMillis() / divFactor,
                maxDate.getMillis() / divFactor,
                minDeltaValues,
                maxDeltaValues
        );
    }

    @Override
    public ValueAndLength<DateTime> readRaw(byte[] buffer, int offset) {
        this.oldValue = codec.readRawValue(buffer, offset);
        return valueAndLength.withValueAndLength(
                new DateTime(this.oldValue * this.divFactor),
                codec.rawBits
        );
    }

    @Override
    public ValueAndLength<DateTime> readDelta(byte[] buffer, int offset) {
        long delta = codec.readDeltaValue(buffer, offset);
        this.oldValue += delta;
        return valueAndLength.withValueAndLength(
                new DateTime(this.oldValue * this.divFactor),
                codec.deltaBits
        );
    }
}
