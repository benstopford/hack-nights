package huffman;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HuffmanCoderTest {

    @Test
    public void shouldEncodeAndDecodeSimpleString(){

        HuffmanCoder encoder = new HuffmanCoder();

        String encoded = encoder.encode("this is a string without a huge amount of duplication of letters within it");

        String decoded = encoder.decode(encoded);

        assertThat(decoded, is("this is a string without a huge amount of duplication of letters within it"));
    }
}
