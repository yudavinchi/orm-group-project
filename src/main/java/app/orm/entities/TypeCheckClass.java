package app.orm.entities;

import app.orm.entities.Dog;

import java.time.LocalDateTime;

public class TypeCheckClass {

    Boolean myBoolean;          // BIT(1)
    Byte myByte;                //BINARY
    Short myShort;              //SMALLINT
    int myInt;                  //INT
    String myString;            //VARCHAR(256)
    long mylong;                //BIGINT
    float myFloat;              //FLOAT
    double myDouble;            //DOUBLE
    LocalDateTime BirthDay;     //DATETIME
    char ch;                    //VARCHAR(1)
    Type type;                  // VARCHAR(50) --- enum
    Dog dog;

// other to Json to LONGTEXT;

    public enum Type {

        BLUE, RED, GREEN;
    }

    public TypeCheckClass(Boolean myBoolean, Byte myByte, Short myShort, int myInt, String myString, long mylong, float myFloat, double myDouble, LocalDateTime birthDay, char ch, Type type, Dog dog) {
        this.myBoolean = myBoolean;
        this.myByte = myByte;
        this.myShort = myShort;
        this.myInt = myInt;
        this.myString = myString;
        this.mylong = mylong;
        this.myFloat = myFloat;
        this.myDouble = myDouble;
        BirthDay = birthDay;
        this.ch = ch;
        this.type = type;
        this.dog = dog;
    }


    @Override
    public String toString() {
        return "TYpeCheckClass{" +
                "myBoolean=" + myBoolean +
                ", myByte=" + myByte +
                ", myShort=" + myShort +
                ", myInt=" + myInt +
                ", myString='" + myString + '\'' +
                ", mylong=" + mylong +
                ", myFloat=" + myFloat +
                ", myDouble=" + myDouble +
                ", BirthDay=" + BirthDay +
                ", ch=" + ch +
                ", type=" + type +
                ", dog=" + dog +
                '}';
    }

    public Boolean getMyBoolean() {
        return myBoolean;
    }

    public void setMyBoolean(Boolean myBoolean) {
        this.myBoolean = myBoolean;
    }

    public Byte getMyByte() {
        return myByte;
    }

    public void setMyByte(Byte myByte) {
        this.myByte = myByte;
    }

    public Short getMyShort() {
        return myShort;
    }

    public void setMyShort(Short myShort) {
        this.myShort = myShort;
    }

    public int getMyInt() {
        return myInt;
    }

    public void setMyInt(int myInt) {
        this.myInt = myInt;
    }

    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }

    public long getMylong() {
        return mylong;
    }

    public void setMylong(long mylong) {
        this.mylong = mylong;
    }

    public float getMyFloat() {
        return myFloat;
    }

    public void setMyFloat(float myFloat) {
        this.myFloat = myFloat;
    }

    public double getMyDouble() {
        return myDouble;
    }

    public void setMyDouble(double myDouble) {
        this.myDouble = myDouble;
    }

    public LocalDateTime getBirthDay() {
        return BirthDay;
    }

    public void setBirthDay(LocalDateTime birthDay) {
        BirthDay = birthDay;
    }

    public char getCh() {
        return ch;
    }

    public void setCh(char ch) {
        this.ch = ch;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }
}
