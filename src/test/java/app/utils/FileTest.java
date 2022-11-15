package app.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileTest {

    @Test
    void readJson_getNull_ThrowException(){
        assertThrows(NullPointerException.class, () -> {File.readJson(null);});
    }
}
