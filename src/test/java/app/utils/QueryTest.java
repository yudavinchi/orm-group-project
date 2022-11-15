package app.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryTest {

    @Test
    void build_query_equalsExpectedResult(){
        assertEquals("SELECT * FROM user;", new Query.Builder().select("*").from("user").build().getQuery());
    }
}
