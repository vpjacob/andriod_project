package com.doormaster.topkeeper.db;

import android.provider.BaseColumns;

/**
 * Room list metadata
 * Created by Liukebing on 2017/3/8.
 */

public final class RoomMetaData {
    private RoomMetaData(){}
    public static abstract class Room implements BaseColumns {
        public static final String ROOM_TABLE_NAME = "room_list";
        public static final String COMMUNITY_CODE = "community_code";
        public static final String COMMUNITY = "community";
        public static final String BUILDING = "building";
        public static final String ROOM_CODE = "room_code";
        public static final String ROOM_NAME = "room_name";
        public static final String ROOM_ID = "room_id";
        public static final String START_DATETIME = "start_datetime";
        public static final String END_DATETIME = "end_datetime";
    }
}
