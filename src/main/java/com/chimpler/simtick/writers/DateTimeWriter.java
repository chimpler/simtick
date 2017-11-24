package com.chimpler.simtick.writers;

import com.chimpler.simtick.codec.BitCodec;
import com.chimpler.simtick.codec.CodecFactory;
import com.chimpler.simtick.codec.LongCodec;
import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

// TODO: Support millis and seconds
public class DateTimeWriter extends Writer<DateTime> {
    public static final byte TYPE_ID = 5;

    private final LongCodec codec;
    private Long oldValue = Long.MAX_VALUE;

    private final DateTime minDate;
    private DateTime maxDate;
    private int minDeltaValues;
    private int maxDeltaValues;
    private int millisUnit;

    public DateTimeWriter(int millisUnit, DateTime minDate, DateTime maxDate) {
        this(millisUnit, minDate, maxDate, true, 0, 0);
    }

    public DateTimeWriter(int millisUnit, DateTime minDate, DateTime maxDate, int minDeltaValues, int maxDeltaValues) {
        this(millisUnit, minDate, maxDate, false, minDeltaValues, maxDeltaValues);
    }

    private DateTimeWriter(int millisUnit, DateTime minDate, DateTime maxDate, boolean fixed, int minDeltaValues, int maxDeltaValues) {
        super(fixed);
        this.millisUnit = millisUnit;
        this.minDate = minDate;
        this.maxDate = maxDate;
        this.minDeltaValues = minDeltaValues;
        this.maxDeltaValues = maxDeltaValues;

        this.codec = new CodecFactory().buildDeltaLongCodec(
                minDate.getMillis() / millisUnit,
                maxDate.getMillis() / millisUnit,
                minDeltaValues,
                maxDeltaValues,
                true
        );
    }

    @Override
    public boolean isDelta(DateTime value) {
        return codec.isInDeltaRange(oldValue, value.getMillis() / millisUnit);
    }

    @Override
    public int writeRaw(byte[] buffer, DateTime value, int offset) {
        this.oldValue = value.getMillis() / millisUnit;
        return codec.writeRawValue(buffer, oldValue, offset);
    }

    @Override
    public int writeDelta(byte[] buffer, DateTime value, int offset) {
        long delta = value.getMillis() / millisUnit - this.oldValue;
        this.oldValue += delta;
        return codec.writeDeltaValue(buffer, delta, offset);
    }

    @Override
    public int writerHeader(byte[] buffer, int srcOffset) {
        int offset = srcOffset;
        offset += BitCodec.write(buffer, millisUnit, offset, 32);
        offset += BitCodec.writeDateTime(buffer, minDate, millisUnit, offset);
        offset += BitCodec.writeDateTime(buffer, maxDate, millisUnit, offset);
        offset += BitCodec.writeBoolean(buffer, fixed, offset);
        offset += BitCodec.write(buffer, minDeltaValues, offset, 32);
        offset += BitCodec.write(buffer, maxDeltaValues, offset, 32);
        return offset - srcOffset;
    }

    @Override
    public int getMaxSize() {
        return this.codec.rawBits;
    }

    @Override
    public int getTypeId() {
        return TYPE_ID;
    }

}
