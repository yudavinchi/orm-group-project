package app.orm;

import app.orm.annotations.AutoIncrementedId;
import app.orm.annotations.UniqueValue;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Id;

import app.orm.utils.TypeConverter;
import com.google.gson.Gson;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class ExecuteQuery {

    private static final TypeConverter typeConverter = TypeConverter.getInstance();
    private static Logger logger = LogManager.getLogger(ExecuteQuery.class.getName());


    //========================== CREATE ==========================
    public static <T> void createTable(Class<T> clz) {
        try {
            Statement statement = ConnectionController.connect().createStatement();
            String createTableQuery = createTableQueryBuilderWithAnnotations(clz);
            statement.execute(createTableQuery);
            ConnectionController.disconnect();
        } catch (SQLException exception) {
            logger.error(exception.toString());
            System.out.println(exception);
        }
    }


    //=========================== READ ===========================
    public static <T> List<T> readAll(Class<T> clz) {
        List<T> results = null;

        try {
            Statement statement = ConnectionController.connect().createStatement();
            Query query = new Query.Builder().select("*").from(getTableName(clz)).build();
            ResultSet rs = statement.executeQuery(query.getQuery());
            results = getResults(rs, clz);
            ConnectionController.disconnect();
        } catch (SQLException exception) {
            logger.error(exception.toString());
            System.out.println(exception);
        }
        logger.info("Successfully read All from class" + results.toString());
        return results;
    }

    public static <T> T readById(int id, Class<T> clz) {
        T result = null;

        try {
            Statement statement = ConnectionController.connect().createStatement();
            Query query = new Query.Builder().select("*").from(getTableName(clz)).where("id", String.valueOf(id)).build();
            ResultSet rs = statement.executeQuery(query.getQuery());
            result = getResult(rs, clz);
            ConnectionController.disconnect();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 SQLException exception) {
            logger.error(exception.toString());
            System.out.println(exception);
        }
        logger.info("Successfully read by id(int) from class" + result.toString());
        return result;
    }

    public static <T> List<T> readByProperty(String property, String value, Class<T> clz) {

        List<T> results = null;

        try {
            Statement statement = ConnectionController.connect().createStatement();
            String table = clz.getSimpleName().toLowerCase();
            Query query = new Query.Builder().select("*").from(table).where(property, value).build();
            ResultSet rs = statement.executeQuery(query.getQuery());
            results = getResults(rs, clz);
        } catch (SQLException exception) {
            logger.error(exception.toString());
            System.out.println(exception);
        }
        logger.info("Successfully read by id(int) from class" + results.toString());
        return results;
    }

    //=========================== ADD ============================

    public static <T> void addItem(T item) {
        try {
            Statement statement = ConnectionController.connect().createStatement();
            String addItemQuery = addItemQueryBuilderWithAnnotations(item);
            statement.execute(addItemQuery);
            ConnectionController.disconnect();
        } catch (SQLException exception) {
            logger.error(exception.toString());
            System.out.println(exception);
        }
    }

    public static <T> void addItems(ArrayList<T> items, Class<T> clz) {
        for (T item : items) {
            addItem(item);
        }
    }

    //========================= UPDATE ===========================
    public static <T> void updatePropertyById(int id, String property, String value, Class<T> clz) {
        try {
            Statement statement = ConnectionController.connect().createStatement();
            Query query = new Query.Builder().update(getTableName(clz)).set(property, value).where("id", String.valueOf(id)).build();
            statement.executeUpdate(query.getQuery());
            ConnectionController.disconnect();
        } catch (SQLException exception) {
            logger.error(exception.toString());
            System.out.println(exception);
        }
    }

    public static <T> void updateEntireItemById(int id, T item, Class<T> clz) {
        try {
            Statement statement = ConnectionController.connect().createStatement();

            List<String> properties = new ArrayList<>();
            List<String> values = new ArrayList<>();
            Field[] declaredFields = clz.getDeclaredFields();

            for (Field field : declaredFields) {

                properties.add(field.getName());
                field.setAccessible(true);
                values.add("'" + field.get(item) + "'");
            }

            String table = clz.getSimpleName().toLowerCase();
            String updatedProperties = "";

            for (int i = 0; i < properties.size(); i++) {
                if (i != properties.size() - 1) {
                    updatedProperties += properties.get(i) + " = " + values.get(i) + ", ";
                } else {
                    updatedProperties += properties.get(i) + " = " + values.get(i);
                }
            }

            System.out.println(String.format("UPDATE %s SET %s WHERE id = '%s';", table, updatedProperties, id));
            String query = String.format("UPDATE %s SET %s WHERE id = '%s';", table, updatedProperties, id);

            statement.executeUpdate(query);

            ConnectionController.disconnect();

        } catch (IllegalAccessException | SQLException exception) {
            logger.error(exception.toString());
            System.out.println(exception);
        }
    }

    //========================= DELETE ===========================

    public static <T> void deleteItemByProperty(String property, String value, Class<T> clz) {
        try {
            Statement statement = ConnectionController.connect().createStatement();
            String deleteByPropertyQuery = deleteItemByPropertyQueryBuilder(clz, property, value);
            statement.execute(deleteByPropertyQuery);
            ConnectionController.disconnect();
        } catch (IllegalAccessException | SQLException | NoSuchFieldException exception) {
            logger.error(exception.toString());
            System.out.println(exception);
        }
    }

    public static <T> void deleteTable(Class<T> clz) {
        try {
            Statement statement = ConnectionController.connect().createStatement();
            statement.execute("DROP TABLE " + clz.getSimpleName().toLowerCase() + ";");
            ConnectionController.disconnect();
        } catch (SQLException exception) {
            logger.error(exception.toString());
            System.out.println(exception);
        }
    }

    private static <T> String deleteItemByPropertyQueryBuilder(Class<T> clz, String property, String value) throws
            NoSuchFieldException, IllegalAccessException {
        String strQuery = "DELETE FROM " + clz.getSimpleName().toLowerCase() + " WHERE ";
        Field[] declaredFields = clz.getDeclaredFields();
        Boolean foundProperty = false;
        for (Field field : declaredFields) {
            if (field.getName().equals(property)) {
                foundProperty = true;
                strQuery += " " + field.getName();
            }
        }
        strQuery += " = " + "'" + value + "';";
        if (!foundProperty) {
            logger.error("Successfully read by id(int) from class");
            throw new NoSuchFieldException("The input property doesn't exists in the table");
        }
        logger.info("Successfully deleted item by property" + strQuery);
        return strQuery;
    }

    //----------------------------------------------------------- HELP FUNCTIONS

    private static <T> Boolean needToConvertToJson(String stringType) {
        if (stringType.equals("TEXT(500)")) {
            return true;
        }
        return false;
    }

    private static <T> String objectToJsonString(Object o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    private static <T> T getResult(ResultSet rs, Class<T> clz)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        List<T> results = getResults(rs, clz);
        return (results.size() == 0) ? null : results.get(0);
    }

    private static <T> List<T> getResults(ResultSet rs, Class<T> clz){
        try {
            List<T> results = new ArrayList<>();

            while (rs.next()) {
                Constructor<T> constructor = clz.getConstructor();
                T item = (T) constructor.newInstance();
                Field[] declaredFields = clz.getDeclaredFields();

                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    field.set(item, rs.getObject(field.getName()));
                }
                results.add(item);
            }
            logger.info("Successfully gets results : " + results);
            return results;
        }
        catch (IllegalAccessException | SQLException | InvocationTargetException | RuntimeException |
               InstantiationException | NoSuchMethodException exception){
            logger.error(exception.toString());
            System.out.println(exception);
        }
       return null;
    }

    private static <T> String getTableName(Class<T> clz) {
        return clz.getSimpleName().toLowerCase();
    }

    //----------------------------------------------------------- COMPLICATED BUILDERS

    public static <T> String createTableQueryBuilderWithAnnotations(Class<T> clz){
        try {
            String strQuery = "";
            Field[] declaredFields = clz.getDeclaredFields();
            int primaryKeys = 0;
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(AutoIncrementedId.class)) {
                    primaryKeys++;
                    strQuery += "id" + " " + TypeConverter.getType(field.getType().toString()) + " primary key AUTO_INCREMENT";
                } else if (field.isAnnotationPresent(Id.class)) {
                    primaryKeys++;
                    strQuery += "id" + " " + TypeConverter.getType(field.getType().toString()) + " primary key UNIQUE NOT NULL";
                } else {
                    strQuery += field.getName() + " " + TypeConverter.getType(field.getType().toString());
                }

                if (field.isAnnotationPresent(UniqueValue.class)) {// UNIQUE
                    strQuery += " UNIQUE";
                }
                if (field.isAnnotationPresent(NotNull.class) && !field.isAnnotationPresent(Id.class)) {// NOT NULL annotation for not id fields
                    strQuery += " NOT NULL";
                }
                strQuery += ", ";

                if (primaryKeys > 1) {
                    logger.error("The class contains more then 2 @Ids");
                    throw new InvalidAttributeValueException("The class contains more then 2 @Ids");
                }
            }
            strQuery = strQuery.substring(0, strQuery.length() - 2);
            return String.format("CREATE TABLE %s (%s);", clz.getSimpleName().toLowerCase(), strQuery);
        }catch (InvalidAttributeValueException exception){
            logger.error(exception.toString());
            System.out.println(exception);
        }
        return null;
    }


    private static <T> String addItemQueryBuilderWithAnnotations(T item){
        try {
            List<String> columns = new ArrayList<>();
            List<String> values = new ArrayList<>();
            boolean neeToUpdateObjectId = false;
            Field[] declaredFields = item.getClass().getDeclaredFields();
            for (Field field : declaredFields
            ) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(AutoIncrementedId.class)) {
                    columns.add("id");
                    neeToUpdateObjectId = true;
                } else {
                    columns.add(field.getName());
                }
                if (needToConvertToJson(TypeConverter.getType(field.getType().toString()))) {
                    values.add("'" + objectToJsonString(field.get(item)) + "'");
                } else {
                    values.add("'" + field.get(item) + "'");
                }
            }
            String query = String.format("INSERT INTO %s (%s) VALUES (%s);", item.getClass().getSimpleName().toLowerCase(), String.join(",", columns), String.join(",", values));
            if (neeToUpdateObjectId) {
                insertIdToItem(item, query);
            }
            return String.format("INSERT INTO %s (%s) VALUES (%s);", item.getClass().getSimpleName().toLowerCase(), String.join(",", columns), String.join(",", values));
        }catch (IllegalAccessException | SQLException exception){
            logger.error(exception.toString());
            System.out.println(exception);
        }
        return null;
    }

    private static <T> void insertIdToItem(T item, String query) throws SQLException, IllegalAccessException {
        try {
            Statement statement = ConnectionController.connect().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.executeQuery("SELECT MAX(id) FROM " + item.getClass().getSimpleName().toLowerCase() + ";");
            if (rs.next()) {
                long id = rs.getLong(1);
                Field[] declaredFields = item.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(AutoIncrementedId.class)) {
                        field.setAccessible(true);
                        field.set(item, id + 1);
                    }
                }
            }
            ConnectionController.disconnect();
        } catch (IllegalAccessException | SQLException exception) {
            logger.error(exception);
            System.out.println(exception);
        }
    }
}

