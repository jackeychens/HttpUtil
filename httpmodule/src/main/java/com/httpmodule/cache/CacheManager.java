package com.httpmodule.cache;



import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by apple on 16/7/1.
 */
public enum CacheManager {

    INSTANCE;

    private Lock mLock;
    private CacheDao cacheDao;



    CacheManager() {
        mLock = new ReentrantLock();
        cacheDao = new CacheDao();
    }

    /**
     * 获取缓存
     *
     * @param key 缓存的Key
     * @return 缓存的对象实体
     */
    public CacheEntity get(String key) {
        mLock.lock();
        try {
            return cacheDao.getKey(key);
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 获取所有缓存
     *
     * @return 缓存的对象实体
     */
    public List<CacheEntity> getAll() {
        mLock.lock();
        try {
            return cacheDao.getAllEntity();
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 更新缓存，没有就创建，有就替换
     *
     * @param key    缓存的key
     * @param entity 需要替换的的缓存
     * @return 被替换的缓存
     */
    public CacheEntity replace(String key, CacheEntity entity) {
        mLock.lock();
        try {
            entity.setKey(key);
            cacheDao.replace(key,entity);
            return entity;
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 移除缓存
     *
     * @param key 缓存的key
     * @return 是否移除成功
     */
    public void remove(String key) {
        if (key == null) return;
        mLock.lock();
        try {
            cacheDao.remove(key);
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 清空缓存
     *
     * @return 是否清空成功
     */
    public void clear() {
        mLock.lock();
        try {
            cacheDao.deleteAll();
        } finally {
            mLock.unlock();
        }
    }

}
