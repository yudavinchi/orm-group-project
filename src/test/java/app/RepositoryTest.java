package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLSyntaxErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RepositoryTest {

    Repository<String> repository= null;

    @BeforeEach
    void beforeEach(){
        repository = new Repository<>(String.class);
    }

//    @Test
//    void readById_getNegativeId_throwsSQLSyntaxErrorException(){
//        assertThrows(java.sql.SQLSyntaxErrorException.class , () -> {repository.readById(-1);});
//    }

    @Test
    void readById_getNegativeId_equalsNull(){
        assertEquals(null, repository.readById(-1));
    }
}
