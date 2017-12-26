package com.example.formandocodigo.psicotimes.utils;

/**
 * Created by FormandoCodigo on 10/12/2017.
 */

public final class Continual {

    public static final class Net {
        public static final String BASE_URL = "http://192.168.1.6:8000/api/";
    }

    public static final class Shared {
        public static final String DEFAULT_FILE_NAME = "user_";
    }

    public static final class SQLite {
        public static final String DEFAULT_FILE_NAME = "psicotimes";

        public static final class App {
            public final String TABLE_NAME = "application";
            public final String COLUMN_ID = "id";
            public final String COLUMN_NAME = "name";
            public final String COLUMN_RELEVANCE = "relevance";
            public final String COLUMN_IMAGE = "image";
            public final String COLUMN_DESCRIPTION = "description";
            public final String COLUMN_CREATED_AT = "created_at";
            public final String COLUMN_UPDATED_AT = "updated_at";
        }

        public static final class StateUse {
            public final String TABLE_NAME = "state_uses";
            public final String COLUMN_ID_USERS = "id_users";
            public final String COLUMN_ID_APP = "id_app";
            public final String COLUMN_TIME_USE = "timeUse";
            public final String COLUMN_QUANTITY = "quantity";
            public final String COLUMN_LAST_USE_TIME = "lastUseTime";
            public final String COLUMN_CREATED_AT = "created_at";
            public final String COLUMN_UPDATED_AT = "updated_at";
        }

        public static final class Statistics {
            public final String TABLE_NAME = "statistics";
            public final String COLUMN_QUANTITY = "quantity";
            public final String COLUMN_TIME_USE = "timeUse";
            public final String COLUMN_CREATED_AT = "created_at";
            public final String COLUMN_UPDATED_AT = "updated_at";
        }
    }
}
