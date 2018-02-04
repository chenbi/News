package com.massmutual.news.data;

import android.net.Uri;
import android.util.Log;


public final class NewsContract {


    public static final String AUTHORITY = "massmutual";


    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);


    public static final Uri[] TABLE_URIS = new Uri[]{Clip.URI};


    private NewsContract() {
    }


    protected interface BaseColumns {

        public static final String _ID = "_id";
        public static final String MIMETYPE = "mimetype";

    }


    protected interface ArticleColumns extends DataColumns {
        public static final String CLIP_ID = DATA1;
        public static final String DESC = DATA3;
        public static final String CLIP_TYPE = DATA4;

        public static final String FEATURED_ID = DATA12;
        public static final String FEATURED_TITLE = DATA13;
        public static final String FEATURED_DESC = DATA14;
        public static final String PUBLISHED_AT = DATA15;
        public static final String IMAGE_URL = DATA16;
        public static final String ORDER = DATA17;

        public static final String THUMBNAIL = DATA18;


        public static final String KEY_FEATURED_ID = "id";
        public static final String KEY_FEATURED_TITLE = "title";

        public static final String KEY_ORDER = "order";

    }






    public static final class Clip implements BaseColumns, DataColumns, ArticleColumns {


        public static final String TABLE_NAME = "Clip";
        public static final Uri CLIP_URI = Uri.withAppendedPath(AUTHORITY_URI, TABLE_NAME);
        public static final String[] PROJ = new String[]{_ID, MIMETYPE, DATA1, DATA2, DATA3, DATA4, DATA5, DATA6,DATA7, DATA8,
                DATA9, DATA10, DATA11, DATA12, DATA13, DATA14, DATA15, DATA16, DATA17, DATA18, DATA19, DATA20, DATA21, DATA22,
                DATA23, DATA24, DATA25, DATA26, DATA27,DATA28};
        public static final String[] TYPE = new String[]{"INTEGER PRIMARY KEY", "TEXT", "TEXT UNIQUE", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT",
                "TEXT", "TEXT", "TEXT", "INTEGER", "TEXT", "TEXT", "TEXT", "TEXT", "INTEGER", "TEXT", "INTEGER", "TEXT", "TEXT", "TEXT",
                "TEXT", "TEXT", "TEXT", "INTEGER DEFAULT 0", "INTEGER", "TEXT", "INTEGER DEFAULT 0","INTEGER",
                "TEXT", "TEXT", "TEXT"};
        public static final Uri URI = CLIP_URI;
        public static final String FOR_CLIP_ID = Clip.CLIP_ID + " =?";
        private Clip() {
        }

        public static Uri buildClipUri(String clipId) {
            Log.v("buildClipUri", "********************" + clipId);
            return CLIP_URI.buildUpon().appendPath(clipId).build();
        }

        public static String getClipId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String[] buildForClipIdSelectionArgs(String clipId) {
            return new String[]{clipId};
        }




    }






    protected interface RelatedLinkColumns extends DataColumns {

        public static final String LINK_ID = DATA4;
        public static final String LINK_NAME = DATA5;
        public static final String LINK_IMAGE = DATA6;

        //public static final String LINK_TYPE = DATA8;
        //public static final String LINK_URL = DATA7;

        // Json keys for link object
        public static final String KEY_LINK_ID = "id";
        public static final String KEY_LINK_NAME = "name";
        public static final String KEY_LINK_IMAGE = "image";

        public static final String KEY_RELATED_LINKS = "categories";

        public static final String RELATED_CLIPS = "RelatedClips";
        //public static final String KEY_LINK_CLIP_TYPE = "type";
        //public static final String KEY_LINK_URL = "url";
    }





    protected interface DataColumns {

        /**
         * *
         */
        public static final String DATA1 = "data1";
        /**
         * *
         */
        public static final String DATA2 = "data2";
        /**
         * *
         */
        public static final String DATA3 = "data3";
        /**
         * *
         */
        public static final String DATA4 = "data4";
        /**
         * *
         */
        public static final String DATA5 = "data5";
        /**
         * *
         */
        public static final String DATA6 = "data6";
        /**
         * *
         */
        public static final String DATA7 = "data7";
        /**
         * *
         */
        public static final String DATA8 = "data8";
        /**
         * *
         */
        public static final String DATA9 = "data9";
        /**
         * *
         */
        public static final String DATA10 = "data10";
        /**
         * *
         */
        public static final String DATA11 = "data11";
        /**
         * *
         */
        public static final String DATA12 = "data12";
        /**
         * *
         */
        public static final String DATA13 = "data13";
        /**
         * *
         */
        public static final String DATA14 = "data14";
        /**
         * *
         */
        public static final String DATA15 = "data15";
        /**
         * *
         */
        public static final String DATA16 = "data16";
        /**
         * *
         */
        public static final String DATA17 = "data17";
        /**
         * *
         */
        public static final String DATA18 = "data18";
        /**
         * *
         */
        public static final String DATA19 = "data19";
        /**
         * *
         */
        public static final String DATA20 = "data20";
        /**
         * *
         */
        public static final String DATA21 = "data21";
        /**
         * *
         */
        public static final String DATA22 = "data22";
        /**
         * *
         */
        public static final String DATA23 = "data23";
        /**
         * *
         */
        public static final String DATA24 = "data24";
        /**
         * *
         */
        public static final String DATA25 = "data25";
        /**
         * *
         */
        public static final String DATA26 = "data26";
        /**
         * *
         */
        public static final String DATA27 = "data27";
        /**
         * *
         */
        public static final String DATA28 = "data28";
    }
}