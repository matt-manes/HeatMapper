package filehandler;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GZDecompressorTest {
    
    @Test
    void read() {
        Path path = TestFile.getGZFile();
        byte[] decompressed = GZDecompressor.read(path);
        assertTrue(decompressed.length > 0);
    }
}