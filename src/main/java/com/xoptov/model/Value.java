package com.xoptov.model;

import java.util.regex.Pattern;

public class Value<T> {

    private int id;

    private T value;

    private int parentId;

    private Value parent;

    private PropertyValue propertyValue;

    public Value(T value)
    {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }

    public Value getParent() {
        return parent;
    }

    public void setParent(Value parent) {
        this.parent = parent;
    }

    public void setPropertyValue(PropertyValue propertyValue) {
        this.propertyValue = propertyValue;
    }

    public PropertyValue getPropertyValue() {
        return propertyValue;
    }
}
