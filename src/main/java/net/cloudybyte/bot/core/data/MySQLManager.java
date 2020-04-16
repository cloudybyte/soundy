package net.cloudybyte.bot.core.data;

import java.sql.*;
import java.util.ArrayList;

public class MySQLManager {

    private String DB_HOST;
    private String DB_PORT;
    private String DB_USER;
    private String DB_PASSWORD;
    private String DB_NAME;

    private Connection connection;
    private Statement statement;

    public MySQLManager(String host, String port, String user, String password, String dbName) {
        DB_HOST = host;
        DB_PORT = port;
        DB_USER = user;
        DB_PASSWORD = password;
        DB_NAME = dbName;
    }

    //------------------------------------------------------------------------------------------------------------------

    //                                             >  >  Basic methods  <  <

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Establishes Connection to database
     */
    public void connect() {
        if (connected()) return;
        try {
            String conURI = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?user=" + DB_USER + "&password=" + DB_PASSWORD;
            connection = DriverManager.getConnection(conURI);
            statement = connection.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Queries a SQL query
     *
     * @param sql SQL-Syntax
     * @return ResultSet of SQL query
     */
    public ResultSet query(String sql) {
        try {
            return statement.executeQuery(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Updates a SQL entry
     *
     * @param sql SQL Syntax
     * @return boolean if updated
     */
    public boolean update(String sql) {
        try {
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Closes the connection
     */
    public void disconnect() {
        if (!connected()) return;
        try {
            connection.close();
            connection = null;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean connected() {
        return connection != null;
    }

    //------------------------------------------------------------------------------------------------------------------

    //                                          >  >  SQL Syntax Methods  <  <

    //------------------------------------------------------------------------------------------------------------------

    /**
     * SELECT Method
     *
     * @param keys      SELECT <keys> -> null for all keys
     * @param table     FROM <table> -> required
     * @param condition WHERE <condition> -> null if not needed
     * @param limit     LIMIT <limit>  -> null if not needed
     * @param orderBy   ORDER BY <orderby> -> null if not needed
     * @return ResultSet for selection / can be parsed with the parser methods
     */
    public ResultSet select(String[] keys, String table, String condition, Integer limit, String orderBy) throws MySQL_NotConnectedQueryException {
        if (!connected())
            throw new MySQL_NotConnectedQueryException("MySQLManager is currently not connected to any database!" +
                    " Therefore can't call SELECT");
        String sql = "SELECT ";
        if (keys != null) {
            boolean first = true;
            for (String key : keys) {
                if (!first) {
                    sql += ",";
                }
                first = false;
                sql += "`" + key + "`";
            }
        } else {
            sql += "*";
        }
        sql += " FROM `" + table + "` ";
        if (condition != null) {
            sql += "WHERE " + condition.replace("==", "=")
                    .replace("&&", "AND").replace("&", "AND").replace("~", "LIKE");
        }
        if (limit != null && limit > 0) {
            sql += "LIMIT " + limit + " ";
        }
        if (orderBy != null) {
            sql += "ORDER BY " + orderBy;
        }
        return query(sql);
    }

    /**
     * INSERT Method
     *
     * @param table  INSERT INTO <table> -> required
     * @param keys   Keys to insert -> at least a length of 1
     * @param values VALUES to insert -> same length as keys[]
     * @return if inserted boolean
     */
    public boolean insert(String table, String[] keys, Object[] values) throws MySQL_NotConnectedQueryException {
        if (!connected())
            throw new MySQL_NotConnectedQueryException("MySQLManager is currently not connected to any database!" +
                    " Therefore can't call INSERT");
        String sql = "INSERT INTO `" + table + "`(";
        if (keys != null && values != null) {
            if (keys.length == values.length) {
                boolean first = true;
                for (String key : keys) {
                    if (!first) {
                        sql += ",";
                    }
                    first = false;
                    sql += "`" + key + "`";
                }
                sql += ") VALUES (";
                first = true;
                for (Object value : values) {
                    if (!first) {
                        sql += ",";
                    }
                    first = false;
                    if (value instanceof String || value instanceof Timestamp || value instanceof java.security.Timestamp) {
                        sql += "'" + value + "'";
                    } else {
                        if (value == null) {
                            sql += "NULL";
                        } else {
                            sql += "" + value + "";
                        }
                    }
                }
                sql += ")";
                return update(sql);
            }
        }
        return false;
    }

    /**
     * @param table     UPDATE <table> -> required
     * @param keys      SET <keys[element]> -> at least a length of 1
     * @param values    SET key=<values[element]> -> same length as keys[]
     * @param condition WHERE <condition>
     * @return if updated boolean
     */
    public boolean update(String table, String[] keys, Object[] values, String condition) throws MySQL_NotConnectedQueryException {
        if (!connected())
            throw new MySQL_NotConnectedQueryException("MySQLManager is currently not connected to any database!" +
                    " Therefore can't call UPDATE");
        String sql = "UPDATE `" + table + "` SET ";
        if (keys != null && values != null) {
            if (keys.length == values.length) {
                boolean first = true;
                for (int i = 0; i < keys.length; i++) {
                    if (!first) {
                        sql += ",";
                    }
                    first = false;
                    sql += "`" + keys[i] + "`=";
                    Object value = values[i];
                    if (value instanceof String || value instanceof Timestamp || value instanceof java.security.Timestamp) {
                        sql += "'" + value + "'";
                    } else {
                        if (value == null) {
                            sql += "NULL";
                        } else {
                            sql += "" + value + "";
                        }
                    }
                }
                if (condition != null) {
                    sql += " WHERE " + condition.replace("==", "=")
                            .replace("&&", "AND").replace("&", "AND");

                }
                return update(sql);
            }
        }
        return false;
    }

    public boolean delete(String table, String condition) throws MySQL_NotConnectedQueryException {
        if (!connected())
            throw new MySQL_NotConnectedQueryException("MySQLManager is currently not connected to any database!" +
                    " Therefore can't call DELETE");
        String sql = "DELETE FROM `" + table + "` WHERE ";
        if (condition == null) {
            sql += "1";
        } else {
            sql += condition.replace("==", "=")
                    .replace("&&", "AND").replace("&", "AND");
        }
        return update(sql);
    }

    //------------------------------------------------------------------------------------------------------------------

    //                                           >  >  ResultSet Parser  <  <

    //------------------------------------------------------------------------------------------------------------------

    private String getString(ResultSet result, String column) throws MySQL_NoEntryFoundException, MySQL_WrongDataTypeException {
        try {
            if (result.next()) {
                if (result.getMetaData().getColumnTypeName(result.findColumn(column)).contains("TEXT") ||
                        result.getMetaData().getColumnTypeName(result.findColumn(column)) == "VARCHAR" ||
                        result.getMetaData().getColumnTypeName(result.findColumn(column)) == "CHAR" ||
                        result.getMetaData().getColumnTypeName(result.findColumn(column)) == "TEXT") {
                    return result.getString(column);
                } else {
                    throw new MySQL_WrongDataTypeException(
                            "Data type of column `" + column + "` is " +
                                    result.getMetaData().getColumnTypeName(result.findColumn(column))
                                    + " not TEXT, VARCHAR, CHAR or TEXT! Therefore it can't be cast to java.lang.String"
                    );
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        throw new MySQL_NoEntryFoundException("No MySQL entry found for column `" + column + "`");
    }

    /**
     * ResultSet-Parser Method
     *
     * @param result ResultSet from SELECT
     * @param column column to read from
     * @return String[] -> All Strings of that specific column
     */
    public String[] getStrings(ResultSet result, String column) throws MySQL_NoEntryFoundException, MySQL_WrongDataTypeException {
        ArrayList<String> r = new ArrayList<>();
        resetResultSet(result);
        while (true) {
            String temp = getString(result, column);
            if (temp != null) {
                r.add(temp);
            } else {
                break;
            }
        }
        

        return r.toArray(new String[]{});
    }

    private Integer getInt(ResultSet result, String column) throws MySQL_NoEntryFoundException, MySQL_WrongDataTypeException {
        try {
            if (result.next()) {
                if (result.getMetaData().getColumnTypeName(result.findColumn(column)).contains("INT")) {
                    return result.getInt(column);
                } else {
                    throw new MySQL_WrongDataTypeException(
                            "Data type of column `" + column + "` is " +
                                    result.getMetaData().getColumnTypeName(result.findColumn(column))
                                    + " not INT! Therefore it can't be cast to java.lang.Integer"
                    );
                }
            } else {
                return null;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        throw new MySQL_NoEntryFoundException("No MySQL entry found for column `" + column + "`");
    }

    /**
     * ResultSet-Parser Method
     *
     * @param result ResultSet from SELECT
     * @param column column to read from
     * @return Integer[] -> All Integers of that specific column
     */
    public Integer[] getInts(ResultSet result, String column) throws MySQL_NoEntryFoundException, MySQL_WrongDataTypeException {
        ArrayList<Integer> r = new ArrayList<>();
        resetResultSet(result);
        while (true) {
            Integer temp = getInt(result, column);
            if (temp != null) {
                r.add(temp);
            } else {
                break;
            }
        }
        
        return r.toArray(new Integer[]{});
    }

    private Boolean getBool(ResultSet result, String column) throws MySQL_NoEntryFoundException, MySQL_WrongDataTypeException {
        try {
            if (result.next()) {
                if (result.getMetaData().getColumnTypeName(result.findColumn(column)) == "BOOLEAN") {
                    return result.getBoolean(column);
                } else {
                    throw new MySQL_WrongDataTypeException(
                            "Data type of column `" + column + "` is " +
                                    result.getMetaData().getColumnTypeName(result.findColumn(column))
                                    + " not BOOLEAN! Therefore it can't be cast to java.lang.Boolean"
                    );
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        throw new MySQL_NoEntryFoundException("No MySQL entry found for column `" + column + "`");
    }

    /**
     * ResultSet-Parser Method
     *
     * @param result ResultSet from SELECT
     * @param column column to read from
     * @return Boolean[] -> All Booleans of that specific column
     */
    public Boolean[] getBools(ResultSet result, String column) throws MySQL_NoEntryFoundException, MySQL_WrongDataTypeException {
        ArrayList<Boolean> r = new ArrayList<>();
        resetResultSet(result);
        while (true) {
            Boolean temp = getBool(result, column);
            if (temp != null) {
                r.add(temp);
            } else {
                break;
            }
        }
        
        return r.toArray(new Boolean[]{});
    }

    private Float getFloat(ResultSet result, String column) throws MySQL_NoEntryFoundException, MySQL_WrongDataTypeException {
        try {
            if (result.next()) {
                if (result.getMetaData().getColumnTypeName(result.findColumn(column)) == "FLOAT") {
                    return result.getFloat(column);
                } else {
                    throw new MySQL_WrongDataTypeException(
                            "Data type of column `" + column + "` is " +
                                    result.getMetaData().getColumnTypeName(result.findColumn(column))
                                    + " not FLOAT! Therefore it can't be cast to java.lang.Float"
                    );
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        throw new MySQL_NoEntryFoundException("No MySQL entry found for column `" + column + "`");
    }

    /**
     * ResultSet-Parser Method
     *
     * @param result ResultSet from SELECT
     * @param column column to read from
     * @return Float[] -> All Floats of that specific column
     */
    public Float[] getFloats(ResultSet result, String column) throws MySQL_NoEntryFoundException, MySQL_WrongDataTypeException {
        ArrayList<Float> r = new ArrayList<>();
        resetResultSet(result);
        while (true) {
            Float temp = getFloat(result, column);
            if (temp != null) {
                r.add(temp);
            } else {
                break;
            }
        }
        
        return r.toArray(new Float[]{});
    }

    private Double getDouble(ResultSet result, String column) throws MySQL_NoEntryFoundException, MySQL_WrongDataTypeException {
        try {
            if (result.next()) {
                if (result.getMetaData().getColumnTypeName(result.findColumn(column)) == "DOUBLE") {
                    return result.getDouble(column);
                } else {
                    throw new MySQL_WrongDataTypeException(
                            "Data type of column `" + column + "` is " +
                                    result.getMetaData().getColumnTypeName(result.findColumn(column))
                                    + " not DOUBLE! Therefore it can't be cast to java.lang.Double"
                    );
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        throw new MySQL_NoEntryFoundException("No MySQL entry found for column `" + column + "`");
    }

    /**
     * ResultSet-Parser Method
     *
     * @param result ResultSet from SELECT
     * @param column column to read from
     * @return Double[] -> All Doubles of that specific column
     */
    public Double[] getDoubles(ResultSet result, String column) throws MySQL_NoEntryFoundException, MySQL_WrongDataTypeException {
        ArrayList<Double> r = new ArrayList<>();
        resetResultSet(result);
        while (true) {
            Double temp = getDouble(result, column);
            if (temp != null) {
                r.add(temp);
            } else {
                break;
            }
        }
        
        return r.toArray(new Double[]{});
    }

    private Timestamp getDate(ResultSet result, String column) throws MySQL_NoEntryFoundException, MySQL_WrongDataTypeException {
        try {
            if (result.next()) {
                if (result.getMetaData().getColumnTypeName(result.findColumn(column)) == "TIMESTAMP") {
                    return result.getTimestamp(column);
                } else {
                    throw new MySQL_WrongDataTypeException(
                            "Data type of column `" + column + "` is " +
                                    result.getMetaData().getColumnTypeName(result.findColumn(column))
                                    + " not TIMESTAMP! Therefore it can't be cast to java.sql.Timestamp"
                    );
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        throw new MySQL_NoEntryFoundException("No MySQL entry found for column `" + column + "`");
    }

    /**
     * ResultSet-Parser Method
     *
     * @param result ResultSet from SELECT
     * @param column column to read from
     * @return Timestamp[] -> All Timestamps of that specific column
     */
    public Timestamp[] getDates(ResultSet result, String column) throws MySQL_NoEntryFoundException, MySQL_WrongDataTypeException {
        ArrayList<Timestamp> r = new ArrayList<>();
        resetResultSet(result);
        while (true) {
            Timestamp temp = getDate(result, column);
            if (temp != null) {
                r.add(temp);
            } else {
                break;
            }
        }
        
        return r.toArray(new Timestamp[]{});
    }

    //------------------------------------------------------------------------------------------------------------------

    //                                            >  >  Pointer Methods  <  <

    //------------------------------------------------------------------------------------------------------------------

    public void resetResultSet(ResultSet result) {
        try {
            result.beforeFirst();
        } catch (SQLException ex) {
        }
    }

    //not yet ready
    /*public void reverseResultSet(ResultSet result) {
        try {
            result.afterLast();
        } catch (SQLException ex){}
    }*/


    //------------------------------------------------------------------------------------------------------------------

    //                                            >  >  Custom Exceptions <  <

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Thrown if an error occurred whilst connecting to or disconnecting from a database
     */
    private class MySQL_ConnectException extends Exception {
        public MySQL_ConnectException(String errMsg) {
            super(errMsg);
        }
    }

    /**
     * Thrown if there was a query without being connected to any database
     */
    public class MySQL_NotConnectedQueryException extends Exception {
        public MySQL_NotConnectedQueryException(String errMsg) {
            super(errMsg);
        }
    }

    /**
     * Thrown if the SQL data types are not matched with their corresponding Java data types
     * correctly
     */
    public class MySQL_WrongDataTypeException extends Exception {
        public MySQL_WrongDataTypeException(String errMsg) {
            super(errMsg);
        }
    }

    /**
     * Thrown if no entry was found for specific ResultSet
     */
    public class MySQL_NoEntryFoundException extends Exception {
        public MySQL_NoEntryFoundException(String errMsg) {
            super(errMsg);
        }
    }

}


