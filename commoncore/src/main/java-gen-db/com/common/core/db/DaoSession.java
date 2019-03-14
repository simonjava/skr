package com.common.core.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.common.core.account.UserAccount;
import com.common.core.userinfo.UserInfoDB;

import com.common.core.db.UserAccountDao;
import com.common.core.db.UserInfoDBDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userAccountDaoConfig;
    private final DaoConfig userInfoDBDaoConfig;

    private final UserAccountDao userAccountDao;
    private final UserInfoDBDao userInfoDBDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userAccountDaoConfig = daoConfigMap.get(UserAccountDao.class).clone();
        userAccountDaoConfig.initIdentityScope(type);

        userInfoDBDaoConfig = daoConfigMap.get(UserInfoDBDao.class).clone();
        userInfoDBDaoConfig.initIdentityScope(type);

        userAccountDao = new UserAccountDao(userAccountDaoConfig, this);
        userInfoDBDao = new UserInfoDBDao(userInfoDBDaoConfig, this);

        registerDao(UserAccount.class, userAccountDao);
        registerDao(UserInfoDB.class, userInfoDBDao);
    }
    
    public void clear() {
        userAccountDaoConfig.clearIdentityScope();
        userInfoDBDaoConfig.clearIdentityScope();
    }

    public UserAccountDao getUserAccountDao() {
        return userAccountDao;
    }

    public UserInfoDBDao getUserInfoDBDao() {
        return userInfoDBDao;
    }

}
