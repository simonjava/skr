package com.module.feeds.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.module.feeds.make.make.FeedsDraftDB;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "FEEDS_DRAFT_DB".
*/
public class FeedsDraftDBDao extends AbstractDao<FeedsDraftDB, Long> {

    public static final String TABLENAME = "FEEDS_DRAFT_DB";

    /**
     * Properties of entity FeedsDraftDB.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property DraftID = new Property(0, Long.class, "draftID", true, "_id");
        public final static Property UpdateTs = new Property(1, Long.class, "updateTs", false, "UPDATE_TS");
        public final static Property From = new Property(2, Integer.class, "from", false, "FROM");
        public final static Property FeedsMakeModelJson = new Property(3, String.class, "feedsMakeModelJson", false, "FEEDS_MAKE_MODEL_JSON");
    }


    public FeedsDraftDBDao(DaoConfig config) {
        super(config);
    }
    
    public FeedsDraftDBDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"FEEDS_DRAFT_DB\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: draftID
                "\"UPDATE_TS\" INTEGER," + // 1: updateTs
                "\"FROM\" INTEGER," + // 2: from
                "\"FEEDS_MAKE_MODEL_JSON\" TEXT);"); // 3: feedsMakeModelJson
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FEEDS_DRAFT_DB\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, FeedsDraftDB entity) {
        stmt.clearBindings();
 
        Long draftID = entity.getDraftID();
        if (draftID != null) {
            stmt.bindLong(1, draftID);
        }
 
        Long updateTs = entity.getUpdateTs();
        if (updateTs != null) {
            stmt.bindLong(2, updateTs);
        }
 
        Integer from = entity.getFrom();
        if (from != null) {
            stmt.bindLong(3, from);
        }
 
        String feedsMakeModelJson = entity.getFeedsMakeModelJson();
        if (feedsMakeModelJson != null) {
            stmt.bindString(4, feedsMakeModelJson);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, FeedsDraftDB entity) {
        stmt.clearBindings();
 
        Long draftID = entity.getDraftID();
        if (draftID != null) {
            stmt.bindLong(1, draftID);
        }
 
        Long updateTs = entity.getUpdateTs();
        if (updateTs != null) {
            stmt.bindLong(2, updateTs);
        }
 
        Integer from = entity.getFrom();
        if (from != null) {
            stmt.bindLong(3, from);
        }
 
        String feedsMakeModelJson = entity.getFeedsMakeModelJson();
        if (feedsMakeModelJson != null) {
            stmt.bindString(4, feedsMakeModelJson);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public FeedsDraftDB readEntity(Cursor cursor, int offset) {
        FeedsDraftDB entity = new FeedsDraftDB( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // draftID
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // updateTs
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // from
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // feedsMakeModelJson
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, FeedsDraftDB entity, int offset) {
        entity.setDraftID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUpdateTs(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setFrom(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setFeedsMakeModelJson(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(FeedsDraftDB entity, long rowId) {
        entity.setDraftID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(FeedsDraftDB entity) {
        if(entity != null) {
            return entity.getDraftID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(FeedsDraftDB entity) {
        return entity.getDraftID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
