package app.entities;

public class Vehicle {
    String company;
    String model;
    int id;
    int year;
    String comment;

    public Vehicle() {
    }

    public Vehicle(String company, String model, int id, int year, String comment) {
        this.company = company;
        this.model = model;
        this.id = id;
        this.year = year;
        this.comment = comment;
    }

    public void printComapny(){
        System.out.println(company);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "company='" + company + '\'' +
                ", model='" + model + '\'' +
                ", id=" + id +
                ", year=" + year +
                ", comment='" + comment + '\'' +
                '}';
    }
}
