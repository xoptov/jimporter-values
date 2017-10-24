package com.xoptov.model;

public class PropertyValue {

    private int id;

    private int categoryId;

    private int propertyId;

    public PropertyValue(int categoryId, int propertyId) {

        this.categoryId = categoryId;
        this.propertyId = propertyId;
    }

    public PropertyValue(int id, int categoryId, int propertyId) {

        this(categoryId, propertyId);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }
}
