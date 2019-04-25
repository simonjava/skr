package com.module.playways.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.module.playways.room.gift.GiftDB;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GIFT_DB".
*/
public class GiftDBDao extends AbstractDao<GiftDB, Long> {

    public static final String TABLENAME = "GIFT_DB";

    /**
     * Properties of entity GiftDB.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property GiftID = new Property(0, Long.class, "giftID", true, "_id");
        public final static Property CanContinue = new Property(1, Boolean.class, "canContinue", false, "CAN_CONTINUE");
        public final static Property Description = new Property(2, String.class, "description", false, "DESCRIPTION");
        public final static Property GiftName = new Property(3, String.class, "giftName", false, "GIFT_NAME");
        public final static Property GiftType = new Property(4, Integer.class, "giftType", false, "GIFT_TYPE");
        public final static Property GiftURL = new Property(5, String.class, "giftURL", false, "GIFT_URL");
        public final static Property Price = new Property(6, Integer.class, "price", false, "PRICE");
        public final static Property SortID = new Property(7, Integer.class, "sortID", false, "SORT_ID");
        public final static Property SourceURL = new Property(8, String.class, "sourceURL", false, "SOURCE_URL");
        public final static Property RealPrice = new Property(9, Float.class, "realPrice", false, "REAL_PRICE");
    }


    public GiftDBDao(DaoConfig config) {
        super(config);
    }
    
    public GiftDBDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GIFT_DB\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: giftID
                "\"CAN_CONTINUE\" INTEGER," + // 1: canContinue
                "\"DESCRIPTION\" TEXT," + // 2: description
                "\"GIFT_NAME\" TEXT," + // 3: giftName
                "\"GIFT_TYPE\" INTEGER," + // 4: giftType
                "\"GIFT_URL\" TEXT," + // 5: giftURL
                "\"PRICE\" INTEGER," + // 6: price
                "\"SORT_ID\" INTEGER," + // 7: sortID
                "\"SOURCE_URL\" TEXT," + // 8: sourceURL
                "\"REAL_PRICE\" REAL);"); // 9: realPrice
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GIFT_DB\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, GiftDB entity) {
        stmt.clearBindings();
 
        Long giftID = entity.getGiftID();
        if (giftID != null) {
            stmt.bindLong(1, giftID);
        }
 
        Boolean canContinue = entity.getCanContinue();
        if (canContinue != null) {
            stmt.bindLong(2, canContinue ? 1L: 0L);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(3, description);
        }
 
        String giftName = entity.getGiftName();
        if (giftName != null) {
            stmt.bindString(4, giftName);
        }
 
        Integer giftType = entity.getGiftType();
        if (giftType != null) {
            stmt.bindLong(5, giftType);
        }
 
        String giftURL = entity.getGiftURL();
        if (giftURL != null) {
            stmt.bindString(6, giftURL);
        }
 
        Integer price = entity.getPrice();
        if (price != null) {
            stmt.bindLong(7, price);
        }
 
        Integer sortID = entity.getSortID();
        if (sortID != null) {
            stmt.bindLong(8, sortID);
        }
 
        String sourceURL = entity.getSourceURL();
        if (sourceURL != null) {
            stmt.bindString(9, sourceURL);
        }
 
        Float realPrice = entity.getRealPrice();
        if (realPrice != null) {
            stmt.bindDouble(10, realPrice);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, GiftDB entity) {
        stmt.clearBindings();
 
        Long giftID = entity.getGiftID();
        if (giftID != null) {
            stmt.bindLong(1, giftID);
        }
 
        Boolean canContinue = entity.getCanContinue();
        if (canContinue != null) {
            stmt.bindLong(2, canContinue ? 1L: 0L);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(3, description);
        }
 
        String giftName = entity.getGiftName();
        if (giftName != null) {
            stmt.bindString(4, giftName);
        }
 
        Integer giftType = entity.getGiftType();
        if (giftType != null) {
            stmt.bindLong(5, giftType);
        }
 
        String giftURL = entity.getGiftURL();
        if (giftURL != null) {
            stmt.bindString(6, giftURL);
        }
 
        Integer price = entity.getPrice();
        if (price != null) {
            stmt.bindLong(7, price);
        }
 
        Integer sortID = entity.getSortID();
        if (sortID != null) {
            stmt.bindLong(8, sortID);
        }
 
        String sourceURL = entity.getSourceURL();
        if (sourceURL != null) {
            stmt.bindString(9, sourceURL);
        }
 
        Float realPrice = entity.getRealPrice();
        if (realPrice != null) {
            stmt.bindDouble(10, realPrice);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public GiftDB readEntity(Cursor cursor, int offset) {
        GiftDB entity = new GiftDB( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // giftID
            cursor.isNull(offset + 1) ? null : cursor.getShort(offset + 1) != 0, // canContinue
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // description
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // giftName
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // giftType
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // giftURL
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // price
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // sortID
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // sourceURL
            cursor.isNull(offset + 9) ? null : cursor.getFloat(offset + 9) // realPrice
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, GiftDB entity, int offset) {
        entity.setGiftID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCanContinue(cursor.isNull(offset + 1) ? null : cursor.getShort(offset + 1) != 0);
        entity.setDescription(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setGiftName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setGiftType(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setGiftURL(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPrice(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setSortID(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setSourceURL(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setRealPrice(cursor.isNull(offset + 9) ? null : cursor.getFloat(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(GiftDB entity, long rowId) {
        entity.setGiftID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(GiftDB entity) {
        if(entity != null) {
            return entity.getGiftID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(GiftDB entity) {
        return entity.getGiftID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
