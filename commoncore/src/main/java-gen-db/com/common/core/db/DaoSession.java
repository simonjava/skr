package com.common.core.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.common.core.account.UserAccount;
import com.common.core.userinfo.noremind.NoRemindInfoDB;
import com.common.core.userinfo.remark.RemarkDB;
import com.common.core.userinfo.UserInfoDB;

import com.common.core.db.UserAccountDao;
import com.common.core.db.NoRemindInfoDBDao;
import com.common.core.db.RemarkDBDao;
import com.common.core.db.UserInfoDBDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userAccountDaoConfig;
    private final DaoConfig noRemindInfoDBDaoConfig;
    private final DaoConfig remarkDBDaoConfig;
    private final DaoConfig userInfoDBDaoConfig;

    private final UserAccountDao userAccountDao;
    private final NoRemindInfoDBDao noRemindInfoDBDao;
    private final RemarkDBDao remarkDBDao;
    private final UserInfoDBDao userInfoDBDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userAccountDaoConfig = daoConfigMap.get(UserAccountDao.class).clone();
        userAccountDaoConfig.initIdentityScope(type);

        noRemindInfoDBDaoConfig = daoConfigMap.get(NoRemindInfoDBDao.class).clone();
        noRemindInfoDBDaoConfig.initIdentityScope(type);

        remarkDBDaoConfig = daoConfigMap.get(RemarkDBDao.class).clone();
        remarkDBDaoConfig.initIdentityScope(type);

        userInfoDBDaoConfig = daoConfigMap.get(UserInfoDBDao.class).clone();
        userInfoDBDaoConfig.initIdentityScope(type);

        userAccountDao = new UserAccountDao(userAccountDaoConfig, this);
        noRemindInfoDBDao = new NoRemindInfoDBDao(noRemindInfoDBDaoConfig, this);
        remarkDBDao = new RemarkDBDao(remarkDBDaoConfig, this);
        userInfoDBDao = new UserInfoDBDao(userInfoDBDaoConfig, this);

        registerDao(UserAccount.class, userAccountDao);
        registerDao(NoRemindInfoDB.class, noRemindInfoDBDao);
        registerDao(RemarkDB.class, remarkDBDao);
        registerDao(UserInfoDB.class, userInfoDBDao);
    }
    
    public void clear() {
        userAccountDaoConfig.clearIdentityScope();
        noRemindInfoDBDaoConfig.clearIdentityScope();
        remarkDBDaoConfig.clearIdentityScope();
        userInfoDBDaoConfig.clearIdentityScope();
    }

    public UserAccountDao getUserAccountDao() {
        return userAccountDao;
    }

    public NoRemindInfoDBDao getNoRemindInfoDBDao() {
        return noRemindInfoDBDao;
    }

    public RemarkDBDao getRemarkDBDao() {
        return remarkDBDao;
    }

    public UserInfoDBDao getUserInfoDBDao() {
        return userInfoDBDao;
    }

}
