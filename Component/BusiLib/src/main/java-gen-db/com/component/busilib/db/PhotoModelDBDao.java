package com.component.busilib.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.zq.person.photo.PhotoModelDB;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PHOTO_MODEL_DB".
*/
public class PhotoModelDBDao extends AbstractDao<PhotoModelDB, String> {

    public static final String TABLENAME = "PHOTO_MODEL_DB";

    /**
     * Properties of entity PhotoModelDB.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property LocalPath = new Property(0, String.class, "localPath", true, "LOCAL_PATH");
        public final static Property Status = new Property(1, Integer.class, "status", false, "STATUS");
    }


    public PhotoModelDBDao(DaoConfig config) {
        super(config);
    }
    
    public PhotoModelDBDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PHOTO_MODEL_DB\" (" + //
                "\"LOCAL_PATH\" TEXT PRIMARY KEY NOT NULL ," + // 0: localPath
                "\"STATUS\" INTEGER);"); // 1: status
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PHOTO_MODEL_DB\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PhotoModelDB entity) {
        stmt.clearBindings();
 
        String localPath = entity.getLocalPath();
        if (localPath != null) {
            stmt.bindString(1, localPath);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(2, status);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PhotoModelDB entity) {
        stmt.clearBindings();
 
        String localPath = entity.getLocalPath();
        if (localPath != null) {
            stmt.bindString(1, localPath);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(2, status);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public PhotoModelDB readEntity(Cursor cursor, int offset) {
        PhotoModelDB entity = new PhotoModelDB( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // localPath
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1) // status
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PhotoModelDB entity, int offset) {
        entity.setLocalPath(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setStatus(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
     }
    
    @Override
    protected final String updateKeyAfterInsert(PhotoModelDB entity, long rowId) {
        return entity.getLocalPath();
    }
    
    @Override
    public String getKey(PhotoModelDB entity) {
        if(entity != null) {
            return entity.getLocalPath();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PhotoModelDB entity) {
        return entity.getLocalPath() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
