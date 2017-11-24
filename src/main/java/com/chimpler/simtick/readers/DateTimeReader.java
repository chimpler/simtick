package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.BitCodec;
import com.chimpler.simtick.codec.CodecFactory;
import com.chimpler.simtick.codec.LongCodec;
import org.joda.time.DateTime;

public class DateTimeReader extends Reader<DateTime> {
    private final LongCodec codec;
    private Long oldValue = Long.MAX_VALUE;
    private final int millisUnit;

    public DateTimeReader(int millisUnit, DateTime minDate, DateTime maxDate) {
        this(millisUnit, minDate, maxDate, true, 0, 0);
    }

    public DateTimeReader(int millisUnit, DateTime minDate, DateTime maxDate, int minDeltaValues, int maxDeltaValues) {
        this(millisUnit, minDate, maxDate, false, minDeltaValues, maxDeltaValues);
    }

    private DateTimeReader(int millisUnit, DateTime minDate, DateTime maxDate, boolean fixed, int minDeltaValues, int maxDeltaValues) {
        super(fixed, 225);
        System.out.println("===============================");
        System.out.println("================> " + millisUnit);
        System.out.println("================> " + minDate);
        System.out.println("================> " + maxDate);
        System.out.println("================> " + fixed);
        System.out.println("================> " + minDeltaValues);
        System.out.println("================> " + maxDeltaValues);
        System.out.println("===============================");
        this.millisUnit = millisUnit;
        this.codec = new CodecFactory().buildDeltaLongCodec(
                minDate.getMillis() / millisUnit,
                maxDate.getMillis() / millisUnit,
                minDeltaValues,
                maxDeltaValues,
                true
        );
    }

    public static ValueAndLength<Reader> fromByteArray(byte[] buffer, int srcOffset) {
        int millisUnit = (int) BitCodec.read(buffer, srcOffset, 32);
        return new ValueAndLength<Reader>(
                new DateTimeReader(
                        millisUnit,
                        BitCodec.readDateTime(buffer, millisUnit, srcOffset + 32),
                        BitCodec.readDateTime(buffer, millisUnit, srcOffset + 96),
                        BitCodec.readBoolean(buffer, srcOffset + 160),
                        (int) BitCodec.read(buffer, srcOffset + 161, 32),
                        (int) BitCodec.read(buffer, srcOffset + 193, 32)
                ),
                225
        );
    }

    @Override
    public ValueAndLength<DateTime> readRaw(byte[] buffer, int offset) {
        this.oldValue = codec.readRawValue(buffer, offset);
        return valueAndLength.withValueAndLength(
                new DateTime(this.oldValue * this.millisUnit),
                codec.rawBits
        );
    }

    @Override
    public ValueAndLength<DateTime> readDelta(byte[] buffer, int offset) {
        long delta = codec.readDeltaValue(buffer, offset);
        this.oldValue += delta;
        return valueAndLength.withValueAndLength(
                new DateTime(this.oldValue * this.codec.getMinDelta()),
                codec.deltaBits
        );
    }

    @Override
    public String toString() {
        return "DateTimeReader{" +
                "codec=" + codec +
                ", oldValue=" + oldValue +
                ", millisUnit=" + millisUnit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateTimeReader that = (DateTimeReader) o;

        if (millisUnit != that.millisUnit) return false;
        if (codec != null ? !codec.equals(that.codec) : that.codec != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = codec != null ? codec.hashCode() : 0;
        result = 31 * result + millisUnit;
        return result;
    }
}
