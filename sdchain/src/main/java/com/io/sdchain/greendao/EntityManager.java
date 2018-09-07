package com.io.sdchain.greendao;

import com.io.sdchain.bean.MsgBean;

import java.util.List;

/**
 * @author xiey
 * @date created at 2018/3/16 14:28
 * @package com.io.sdchain.database
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 * @参考 http://blog.csdn.net/u014752325/article/details/53996232
 * @参考 http://blog.csdn.net/u012702547/article/details/52226163
 * @参考 https://github.com/greenrobot/greenDAO/
 */

public class EntityManager {
    private static EntityManager entityManager;
    public MsgBeanDao msgBeanDao;

    /**
     * create info table
     */
    public MsgBeanDao getMsgBeanDao() {
        msgBeanDao = DaoManager.getInstance().getSession().getMsgBeanDao();
        return msgBeanDao;
    }

    /**
     * create instance
     */
    public static EntityManager getInstance() {
        if (entityManager == null) {
            entityManager = new EntityManager();
        }
        return entityManager;
    }

    /**
     * judgment have same record ?
     */
    public static boolean hasSame(String userId, String hash) {
        List<MsgBean> msgBeanList = getInstance().getMsgBeanDao().queryBuilder()
                .where(MsgBeanDao.Properties.UserId.eq(userId))
                .where(MsgBeanDao.Properties.Hash.eq(hash))
                .build().list();
        if (msgBeanList != null && msgBeanList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * insert a record
     */
    public static void insert(MsgBean msgBean) {
        if (!hasSame(msgBean.getUserId(), msgBean.getHash())) {
            msgBean.setReadStatus(false);
            getInstance().getMsgBeanDao().insert(msgBean);
        }
    }

    /**
     * query all
     */
    public static List<MsgBean> queryAll() {
        List<MsgBean> msgBeenList = getInstance().getMsgBeanDao().queryBuilder()
                .build()
                .list();
        return msgBeenList;
    }

    /**
     * query current wallet all read status-unread
     */
    public static List<MsgBean> queryAllReadState(String userId,String account) {
        List<MsgBean> msgBeenList = getInstance().getMsgBeanDao().queryBuilder()
                .where(MsgBeanDao.Properties.UserId.eq(userId))
                .where(MsgBeanDao.Properties.Account.eq(account))
                .where(MsgBeanDao.Properties.ReadStatus.eq(false))
                .build()
                .list();
        return msgBeenList;
    }

    /**
     * query one record
     */
    public static MsgBean query(String userId, String hash) {
        MsgBean msgBean = getInstance().getMsgBeanDao().queryBuilder()
                .where(MsgBeanDao.Properties.UserId.eq(userId))
                .where(MsgBeanDao.Properties.Hash.eq(hash))
                .build()
                .unique();
        return msgBean;
    }

    /**
     * query more records
     */
    public static List<MsgBean> queryByPage(String userId,String account, int page, int count) {
        List<MsgBean> msgBeenList = getInstance().getMsgBeanDao().queryBuilder()
                .where(MsgBeanDao.Properties.UserId.eq(userId))
                .where(MsgBeanDao.Properties.Account.eq(account))
                .offset((page - 1) * count)
                .limit(count)
                .orderDesc(MsgBeanDao.Properties.Timestamp)
                .build()
                .list();
        return msgBeenList;
    }

    /**
     * change one record of read status
     */
    public static void changeReadStatus(String userId, String hash) {
        MsgBean msgBean = query(userId, hash);
        msgBean.setReadStatus(true);
        getInstance().getMsgBeanDao().update(msgBean);

    }

    /**
     * change all record of read status
     */
    public static void changeReadStatusAll() {
        List<MsgBean> msgBeanList = queryAll();
        for (MsgBean msgBean : msgBeanList) {
            msgBean.setReadStatus(true);
            getInstance().getMsgBeanDao().update(msgBean);
        }
    }

    /**
     * change all read status
     */
    public static void changeReadStatusAllUn() {
        List<MsgBean> msgBeanList = queryAll();
        for (MsgBean msgBean : msgBeanList) {
            msgBean.setReadStatus(false);
            getInstance().getMsgBeanDao().update(msgBean);
        }
    }

    /**
     * change all read status by page
     */
    public static void changeReadStatusByPage(int page, int count) {
        List<MsgBean> msgBeenList = getInstance().getMsgBeanDao().queryBuilder()
                .limit(page * count)
                .build()
                .list();
        for (MsgBean msgBean : msgBeenList) {
            msgBean.setReadStatus(true);
            getInstance().getMsgBeanDao().update(msgBean);
        }
    }
}
