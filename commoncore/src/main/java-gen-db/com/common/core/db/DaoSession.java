package com.common.core.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.common.core.userinfo.remark.RemarkDB;
import com.common.core.userinfo.UserInfoDB;
import com.common.core.account.UserAccount;

import com.common.core.db.RemarkDBDao;
import com.common.core.db.UserInfoDBDao;
import com.common.core.db.UserAccountDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig remarkDBDaoConfig;
    private final DaoConfig userInfoDBDaoConfig;
    private final DaoConfig userAccountDaoConfig;

    private final RemarkDBDao remarkDBDao;
    private final UserInfoDBDao userInfoDBDao;
    private final UserAccountDao userAccountDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        remarkDBDaoConfig = daoConfigMap.get(RemarkDBDao.class).clone();
        remarkDBDaoConfig.initIdentityScope(type);

        userInfoDBDaoConfig = daoConfigMap.get(UserInfoDBDao.class).clone();
        userInfoDBDaoConfig.initIdentityScope(type);

        userAccountDaoConfig = daoConfigMap.get(UserAccountDao.class).clone();
        userAccountDaoConfig.initIdentityScope(type);

        remarkDBDao = new RemarkDBDao(remarkDBDaoConfig, this);
        userInfoDBDao = new UserInfoDBDao(userInfoDBDaoConfig, this);
        userAccountDao = new UserAccountDao(userAccountDaoConfig, this);

        registerDao(RemarkDB.class, remarkDBDao);
        registerDao(UserInfoDB.class, userInfoDBDao);
        registerDao(UserAccount.class, userAccountDao);
    }
    
    public void clear() {
        remarkDBDaoConfig.clearIdentityScope();
        userInfoDBDaoConfig.clearIdentityScope();
        userAccountDaoConfig.clearIdentityScope();
    }

    public RemarkDBDao getRemarkDBDao() {
        return remarkDBDao;
    }

    public UserInfoDBDao getUserInfoDBDao() {
        return userInfoDBDao;
    }

    public UserAccountDao getUserAccountDao() {
        return userAccountDao;
    }

}
