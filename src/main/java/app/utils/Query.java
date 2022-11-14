package app.utils;

import java.util.List;

public class Query {
    private String query;

    public static class Builder {

        String query;

        public Builder() {
            query = "";
        }

        public Builder insertInto(String table) {
            query = "INSERT INTO " + table;
            return this;
        }

        public Builder withFields(List<String> fields) {
            query = String.format(query + " (%s)", String.join(",", fields));
            return this;
        }

        public Builder andValues(List<String> values) {
            query = String.format(query + " VALUES (%s)", String.join(",", values));
            return this;
        }

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
