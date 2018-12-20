package com.fanneng.filemanager.common.dao;

import com.fanneng.filemanager.common.bean.Person;
import com.fanneng.filemanager.common.bean.PersonDao;
import com.fanneng.filemanager.common.globalconfig.BaseApplication;

import java.util.List;

/**
 * 作者： liujianguang on 2018/7/4 18:17
 * 邮箱： liujga@enn.cn
 */
public class DataDao {
    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param shop
     */
    public static void insertData(Person shop) {
        BaseApplication.getDaoInstant().getPersonDao().insertOrReplace(shop);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteData(long id) {
        BaseApplication.getDaoInstant().getPersonDao().deleteByKey(id);
    }

    /**
     * 更新数据,修改单个数据
     *
     * @param shop
     */
    public static void updateData(Person shop) {
        BaseApplication.getDaoInstant().getPersonDao().update(shop);
    }

    /**
     * 查询条件为Type=TYPE_LOVE的数据
     *
     * @return
     */
    public static List<Person> queryData() {
        return BaseApplication.getDaoInstant().getPersonDao().queryBuilder().where(PersonDao.Properties.Id.eq(1)).list();
    }

    /**
     * 查询条件为TName的数据
     *
     * @return
     */
    public static List<Person> queryData(String Name) {
        return BaseApplication.getDaoInstant().getPersonDao().queryBuilder().where(PersonDao.Properties.Name.eq(Name)).list();
    }


    /**
     * 查询全部数据
     */
    public static List<Person> queryAll() {
        List<Person> list =BaseApplication.getDaoInstant().getPersonDao().loadAll();
        return list;
        // List< Shop> list = getShopDao().queryBuilder().list();
    }


    /**
     * 增加单个数据(无论有无直接增加)
     */
    public static void insertSigleData(Person shop) {
        BaseApplication.getDaoInstant().getPersonDao().insert(shop);
    }

    /**
     * 增加多个数据(无论有无直接增加)
     */
    public static void insertMultiData(List<Person> shop) {
        BaseApplication.getDaoInstant().getPersonDao().insertInTx(shop);
    }

    /**
     * 增加多个数据(如果有重复则覆盖)
     */
    public static void insertMultiDataOfRepleace(List<Person> shop) {
        BaseApplication.getDaoInstant().getPersonDao().insertOrReplaceInTx(shop);
    }

    /**
     * 查询全部数据
     */
    public static List<Person> queryAllWhere() {
        return BaseApplication.getDaoInstant().getPersonDao().loadAll();
    }

    /**
     * 查询全部数据
     *
     * 查询附加单个条件
         .where()
         .whereOr()
         查询附加多个条件
         .where(, , ,)
         .whereOr(, , ,)
         查询附加排序
         .orderDesc()
         .orderAsc()
         查询限制当页个数
         .limit()
         查询总个数
         .count()
     */
    public static List<Person> queryAllWhereDesc() {
        return BaseApplication.getDaoInstant().getPersonDao().loadAll();
    }

    /**
     * 更新数据,修改多个数据
     *
     * @param shopList
     */
    public static void updateMultiData(List<Person> shopList) {
        BaseApplication.getDaoInstant().getPersonDao().updateInTx(shopList);
    }

    /**
     * 删除数据,通过表名
     *
     * @param shop
     */
    public static void deleteDataByForm(Person shop) {
        BaseApplication.getDaoInstant().getPersonDao().delete(shop);
    }

    /**
     * 删除多个数据,通过表名
     *
     * @param shoplist
     */
    public static void deleteDataByFormList(List<Person> shoplist) {
        BaseApplication.getDaoInstant().getPersonDao().deleteInTx(shoplist);
    }




}