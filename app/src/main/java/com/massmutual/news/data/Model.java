package com.massmutual.news.data;

public abstract class Model {

    public abstract String getTableName();

    public abstract String[] getTableColumns();

    public abstract String[] getTableColumnTypes();
}
