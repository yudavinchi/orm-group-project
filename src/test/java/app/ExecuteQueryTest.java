package app;

import app.orm.ExecuteQuery;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExecuteQueryTest {

    @Test
    void readAll_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ExecuteQuery.readAll(null));
    }

    @Test
    void needToConvertToJson_String_equalsTrue() {
        try {
            Method method = ExecuteQuery.class.getDeclaredMethod("private static java.lang.Boolean app.orm.ExecuteQuery.needToConvertToJson(java.lang.String)", ExecuteQuery.class);
            method.setAccessible(true);

            assertEquals(true, method.invoke("TEXT(500)"));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            System.out.println(exception);
        }
    }
}
