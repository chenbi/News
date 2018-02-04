package com.massmutual.news.data;


public final class NewsModel {

    // Array containing all data models, and index values for each model
    public static final Model[] MODELS = new Model[]{new ClipModel()};


    public static final class ClipModel extends Model {

        @Override
        public String getTableName() {
            return NewsContract.Clip.TABLE_NAME;
        }

        @Override
        public String[] getTableColumns() {
            return NewsContract.Clip.PROJ;
        }

        @Override
        public String[] getTableColumnTypes() {
            return NewsContract.Clip.TYPE;
        }

    }





}
