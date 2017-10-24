package com.xoptov.manager;

import com.xoptov.model.PropertyValue;
import com.xoptov.model.Value;
import org.jetbrains.annotations.NotNull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class ValueManager {

    private Connection conn;

    private LinkedList<Value> buffer;

    public ValueManager(Connection conn) {
        this.conn = conn;
        buffer = new LinkedList<>();
    }

    public Value<?> create(String value, PropertyValue propertyValue) {
        Value valueObj = create(value);

        if (propertyValue != null) {
            valueObj.setPropertyValue(propertyValue);
        }

        return valueObj;
    }

    public Value<?> getById(int id) {

        for (Value item : buffer) {
            if (item.getId() == id) {
                return item;
            }
        }

        String sql = "SELECT parent_id, as_boolean, as_float, as_string FROM `value` WHERE id = ? LIMIT 1";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                int parentId = rs.getInt("parent_id");
                Boolean asBoolean = rs.getBoolean("as_boolean");
                Float asFloat = rs.getFloat("as_float");
                String asString = rs.getString("as_string");

                Value value = null;

                if (asFloat != 0) {
                    value = new Value<>(asFloat);
                } else if (!asString.isEmpty()) {
                    value = new Value<>(asString);
                } else {
                    value = new Value<>(asBoolean);
                }

                if (parentId > 0) {
                    value.setParentId(parentId);
                }

                buffer.add(value);

                return value;

            } catch (SQLException e) {
                return null;
            }

        } catch (SQLException e) {
            return null;
        }
    }

    public <T> Value<T> getByValue(T value) {
        for (Value<T> item : buffer) {
            if (item.getValue() == value) {

            }
        }
    }

    public <T> boolean addToBuffer(Value<T> value) {

        return !buffer.contains(value) && buffer.add(value);
    }

    @NotNull
    private Value<?> create(String value) {
        if (value.matches("[01]|true|false|yes|no")) {
            return new Value<Boolean>(Boolean.parseBoolean(value));
        } else if (value.matches("\\d+\\.\\d+")) {
            return new Value<Float>(Float.parseFloat(value));
        }

        return new Value<String>(value);
    }
}
