package com.xoptov.manager;

import com.xoptov.model.PropertyValue;
import java.sql.Connection;
import java.util.LinkedList;

public class PropertyValueManager {

    private Connection conn;

    private LinkedList<PropertyValue> buffer;

    public PropertyValueManager(Connection conn) {
        this.conn = conn;
    }

    public PropertyValue create(int categoryId, int propertyId) {
        PropertyValue propertyValue = new PropertyValue(categoryId, propertyId);

        return propertyValue;
    }
}
