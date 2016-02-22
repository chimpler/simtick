package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.CodecFactory;
import com.chimpler.simtick.codec.LongCodec;
import org.joda.time.DateTime;

public class DateTimeReader extends Reader<DateTime> {
    private final LongCodec codec;
    private Long oldValue = Long.MAX_VALUE;
    private int divFactor;

    public DateTimeReader(DateTime minDate, DateTime maxDate, boolean isMillis) {
        this(minDate, maxDate, true, 0, 0, isMillis);
    }

    public DateTimeReader(DateTime minDate, DateTime maxDate, int minDeltaValues, int maxDeltaValues, boolean isMillis) {
        this(minDate, maxDate, false, minDeltaValues, maxDeltaValues, isMillis);
    }

    private DateTimeReader(DateTime minDate, DateTime maxDate, boolean fixed, int minDeltaValues, int maxDeltaValues, boolean isMillis) {
        super(fixed);
        this.divFactor = isMillis ? 1 : 1000;
        this.codec = new CodecFactory().buildDeltaLongCodec(
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
