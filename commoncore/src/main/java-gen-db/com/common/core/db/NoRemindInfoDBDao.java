package com.common.core.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.common.core.userinfo.noremind.NoRemindInfoDB;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NO_REMIND_INFO_DB".
*/
public class NoRemindInfoDBDao extends AbstractDao<NoRemindInfoDB, Long> {

    public static final String TABLENAME = "NO_REMIND_INFO_DB";

    /**
     * Properties of entity NoRemindInfoDB.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property UserId = new Property(0, Long.class, "userId", true, "_id");
        public final static Property MsgType = new Property(1, int.class, "msgType", false, "MSG_TYPE");
    }


    public NoRemindInfoDBDao(DaoConfig config) {
        super(config);
    }
    
    public NoRemindInfoDBDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NO_REMIND_INFO_DB\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: userId
                "\"MSG_TYPE\" INTEGER NOT NULL );"); // 1: msgType
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NO_REMIND_INFO_DB\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, NoRemindInfoDB entity) {
        stmt.clearBindings();
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(1, userId);
        }
        stmt.bindLong(2, entity.getMsgType());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, NoRemindInfoDB entity) {
        stmt.clearBindings();
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(1, userId);
        }
        stmt.bindLong(2, entity.getMsgType());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public NoRemindInfoDB readEntity(Cursor cursor, int offset) {
        NoRemindInfoDB entity = new NoRemindInfoDB( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // userId
            cursor.getInt(offset + 1) // msgType
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, NoRemindInfoDB entity, int offset) {
        entity.setUserId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMsgType(cursor.getInt(offset + 1));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(NoRemindInfoDB entity, long rowId) {
        entity.setUserId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(NoRemindInfoDB entity) {
        if(entity != null) {
            return entity.getUserId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(NoRemindInfoDB entity) {
        return entity.getUserId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
