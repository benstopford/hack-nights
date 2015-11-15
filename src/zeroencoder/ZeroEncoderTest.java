package zeroencoder;

import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class ZeroEncoderTest {

    @Test
    public void shouldProcessSingleByte() throws IOException {
        ZeroEncoder encoder = new ZeroEncoder();

        byte[] input = {0x01};
        byte[] noZeros = encoder.removeZeros(input);

        for (byte b : noZeros)
            assertThat(b, not((byte) 0x00));

        byte[] output = encoder.revert(noZeros);

        assertThat(output, is(input));
    }

    @Test
    public void shouldProcessSingleZero() throws IOException {
        ZeroEncoder encoder = new ZeroEncoder();

        byte[] input = {0x00};
        byte[] noZeros = encoder.removeZeros(input);

        for (byte b : noZeros)
            assertThat(b, not((byte) 0x00));

        byte[] output = encoder.revert(noZeros);

        assertThat(output, is(input));
    }

    @Test
    public void shouldProcessSmallByteString() throws IOException {
        ZeroEncoder encoder = new ZeroEncoder();

        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        byte[] noZeros = encoder.removeZeros(input);

        for (byte b : noZeros)
            assertThat(b, not((byte) 0x00));

        byte[] output = encoder.revert(noZeros);

        assertThat(output, is(input));
    }

    @Test
    public void shouldProcessSmallByteStringWithZeros() throws IOException {
        ZeroEncoder encoder = new ZeroEncoder();

        byte[] input = {0x01, 0x02, 0x00, 0x04, 0x00, 0x06, 0x07};
        byte[] noZeros = encoder.removeZeros(input);

        for (byte b : noZeros)
            assertThat(b, not((byte) 0x00));

        byte[] output = encoder.revert(noZeros);

        assertThat(output, is(input));
    }

    @Test
    public void shouldProcessLargeByteString() throws IOException {
        ZeroEncoder encoder = new ZeroEncoder();

        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
        byte[] noZeros = encoder.removeZeros(input);

        for (byte b : noZeros)
            assertThat(b, not((byte) 0x00));

        byte[] output = encoder.revert(noZeros);

        assertThat(output, is(input));
    }


    @Test
    public void shouldProcessLargeByteStringWithZeros() throws IOException {
        ZeroEncoder encoder = new ZeroEncoder();

        byte[] input = {0x00, 0x02, 0x03, 0x04, 0x00, 0x06, 0x07, 0x08, 0x00, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x00};
        byte[] noZeros = encoder.removeZeros(input);

        for (byte b : noZeros)
            assertThat(b, not((byte) 0x00));

        byte[] output = encoder.revert(noZeros);

        assertThat(output, is(input));
    }

    @Test
    public void shouldProcessLargeRandomlyGeneratedByteString() throws IOException {
        ZeroEncoder encoder = new ZeroEncoder();

        byte[] input = new byte[10000];
        new Random().nextBytes(input);

        byte[] noZeros = encoder.removeZeros(input);

        for (byte b : noZeros)
            assertThat(b, not((byte) 0x00));

        byte[] output = encoder.revert(noZeros);

        assertThat(output, is(input));
    }

}