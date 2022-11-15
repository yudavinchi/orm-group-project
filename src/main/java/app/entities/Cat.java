package app.entities;

import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Cat {

    @Id
    private int id;
    private int collarNumber;

    public Cat(int id ,int collarNumber) {
        this.id = id;
        this.collarNumber = collarNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCollarNumber() {
        return collarNumber;
    }

    public void setCollarNumber(int collarNumber) {
        this.collarNumber = collarNumber;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "id=" + id +
                ", collarNumber=" + collarNumber +
                '}';
    }
}
