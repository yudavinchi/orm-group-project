package app.utils;

import java.lang.ref.PhantomReference;
import java.util.List;

public class Query {
    private final String query;

    public static class Builder {

        String query;

        public Builder() {
            query = "";
        }

        // INSERT INFO
        public Builder insertInto(String table) {
            query = "INSERT INTO " + table;
            return this;
        }

        // ADD FIELDS
        public Builder withFields(List<String> fields) {
            query = String.format(query + " (%s)", String.join(",", fields));
            return this;
        }

        // ADD VALUES
        public Builder andValues(List<String> values) {
            query = String.format(query + " VALUES (%s)", String.join(",", values));
            return this;
        }

        // SELECT
        public Builder select(String property){
            query = String.format("SELECT %s", property);
            return this;
        }

        // FROM
        public Builder from(String table){
            query = String.format(query + " FROM %s", table);
            return this;
        }

        // WHERE
        public Builder where(String property, String value){
            query = String.format(query + " WHERE %s='%s'", property, value);
            return this;
        }

        // UPDATE
        public Builder update(String property){
            query = String.format("UPDATE %s", property);
            return this;
        }

        // SET
        public Builder set(String property, String value){
            query = String.format(query + " SET %s='%s'", property, value);
            return this;
        }

        // CREATE TABLE
        public Builder createTable(String name) {
            query = "CREATE TABLE " + name;
            return this;
        }

        public Query build() {
            query += ";";
            return new Query(this);
        }
    }

    private Query(Builder builder) {
        this.query = builder.query;
    }

    public String getQuery() {
        return query;
    }
}
