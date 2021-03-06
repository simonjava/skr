package com.module.feeds.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.module.feeds.watch.manager.FeedCollectDB;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "FEED_COLLECT_DB".
*/
public class FeedCollectDBDao extends AbstractDao<FeedCollectDB, Long> {

    public static final String TABLENAME = "FEED_COLLECT_DB";

    /**
     * Properties of entity FeedCollectDB.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property FeedID = new Property(0, Long.class, "feedID", true, "_id");
        public final static Property FeedType = new Property(1, Long.class, "feedType", false, "FEED_TYPE");
        public final static Property TimeMs = new Property(2, Long.class, "timeMs", false, "TIME_MS");
        public final static Property FeedSong = new Property(3, String.class, "feedSong", false, "FEED_SONG");
        public final static Property User = new Property(4, String.class, "user", false, "USER");
    }


    public FeedCollectDBDao(DaoConfig config) {
        super(config);
    }
    
    public FeedCollectDBDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"FEED_COLLECT_DB\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: feedID
                "\"FEED_TYPE\" INTEGER," + // 1: feedType
                "\"TIME_MS\" INTEGER," + // 2: timeMs
                "\"FEED_SONG\" TEXT," + // 3: feedSong
                "\"USER\" TEXT);"); // 4: user
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FEED_COLLECT_DB\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, FeedCollectDB entity) {
        stmt.clearBindings();
 
        Long feedID = entity.getFeedID();
        if (feedID != null) {
            stmt.bindLong(1, feedID);
        }
 
        Long feedType = entity.getFeedType();
        if (feedType != null) {
            stmt.bindLong(2, feedType);
        }
 
        Long timeMs = entity.getTimeMs();
        if (timeMs != null) {
            stmt.bindLong(3, timeMs);
        }
 
        String feedSong = entity.getFeedSong();
        if (feedSong != null) {
            stmt.bindString(4, feedSong);
        }
 
        String user = entity.getUser();
        if (user != null) {
            stmt.bindString(5, user);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, FeedCollectDB entity) {
        stmt.clearBindings();
 
        Long feedID = entity.getFeedID();
        if (feedID != null) {
            stmt.bindLong(1, feedID);
        }
 
        Long feedType = entity.getFeedType();
        if (feedType != null) {
            stmt.bindLong(2, feedType);
        }
 
        Long timeMs = entity.getTimeMs();
        if (timeMs != null) {
            stmt.bindLong(3, timeMs);
        }
 
        String feedSong = entity.getFeedSong();
        if (feedSong != null) {
            stmt.bindString(4, feedSong);
        }
 
        String user = entity.getUser();
        if (user != null) {
            stmt.bindString(5, user);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public FeedCollectDB readEntity(Cursor cursor, int offset) {
        FeedCollectDB entity = new FeedCollectDB( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // feedID
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // feedType
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // timeMs
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // feedSong
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // user
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, FeedCollectDB entity, int offset) {
        entity.setFeedID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFeedType(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setTimeMs(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setFeedSong(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUser(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(FeedCollectDB entity, long rowId) {
        entity.setFeedID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(FeedCollectDB entity) {
        if(entity != null) {
            return entity.getFeedID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(FeedCollectDB entity) {
        return entity.getFeedID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
