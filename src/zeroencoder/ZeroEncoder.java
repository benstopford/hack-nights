package zeroencoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Creates a variable length encoding based on fixed 7 byte chunks from the
 * inbound array.
 * <p>
 * The input array is separated into sections of 7 bytes. Any zeros present in these 7 bytes
 * are encoded in a single byte bitmap header. This is followed by between 0 and 7 non-zero bytes.
 * The encoding repeats.
 * <p>
 * The bitmap value of 0 is escaped to comply with the question specification
 * <p>
 * Note that the inbound data is treated as fixed width 7 byte chunks but the encoded value
 * is variable length.
 */
public class ZeroEncoder {


    public static final int notSet = Integer.MAX_VALUE;

    public byte[] revert(byte[] noZeros) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int zerosBitmap = notSet;
        int wordPosition = 0;

        for (byte b : noZeros) {

            //this byte will either be the zeros bitmap or a value
            if (zerosBitmap == notSet) {
                zerosBitmap = b;
                if (zerosBitmap == -1) zerosBitmap = 0;
            } else {
                output.write(b);
                wordPosition++;
            }

            //check to see if we need to add zeros from the bitmap
            while ((zerosBitmap & (1 << wordPosition)) > 0 && wordPosition < 8) {
                output.write((byte) 0);
                wordPosition++;
            }

            //reset if we reach the end of the 7 byte chunk
            if (wordPosition == 7) {
                zerosBitmap = notSet;
                wordPosition = 0;
            }
        }

        return output.toByteArray();
    }


    public byte[] removeZeros(byte[] input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int zerosBitmap = 0;
        int buffered = 0;
        int written = 0;

        for (byte b : input) {
            //Either write zeros to the bitmap of just append the value.
            if (b == 0)
                zerosBitmap = zerosBitmap | 0x01 << buffered;
            else
                buf.write(b);
            buffered++;

            //Once we've consumed 7 bytes of input flush to the output buffer
            if (buffered == 7 || (written + buffered) == input.length) {

                //escape 0x00 in the zeros bitmap
                if (zerosBitmap == 0) zerosBitmap = -1;

                //write this chunk
                output.write((byte) zerosBitmap);
                output.write(buf.toByteArray());

                //cleanup
                buf.reset();
                zerosBitmap = 0;
                buffered = 0;
                written += 7;
            }
        }

        return output.toByteArray();
    }
}
