package app.utils;

import app.orm.utils.TypeConverter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypeConverterTest {

    @BeforeAll
    static void beforeAll(){
        TypeConverter.getInstance();
    }

    @Test
    void getType_int_equalsInteger(){
        assertEquals("INTEGER", TypeConverter.getType("int"));
    }
}
