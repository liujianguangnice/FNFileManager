package com.fanneng.filemanager.common.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 作者： liujianguang on 2018/7/5 14:36
 * 邮箱： liujga@enn.cn
 */
@Entity
public class Person {
    //不能用int
    @Id(autoincrement = true)
    @NotNull
    private Long id;

    private String OnlySign;

    private String Name;

    @Property(nameInDb = "Age")
    private String Age;

    private String Address;

    private int sell_num;

    private String image_url;

    @Transient//使用该注释的属性不会被存入数据库的字段中
    private int type;

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", OnlySign='" + OnlySign + '\'' +
                ", Name='" + Name + '\'' +
                ", Age='" + Age + '\'' +
                ", Address='" + Address + '\'' +
                ", sell_num=" + sell_num +
                ", image_url='" + image_url + '\'' +
                ", type=" + type +
                '}';
    }

    @Generated(hash = 1112930087)
    public Person(@NotNull Long id, String OnlySign, String Name, String Age,
            String Address, int sell_num, String image_url) {
        this.id = id;
        this.OnlySign = OnlySign;
        this.Name = Name;
        this.Age = Age;
        this.Address = Address;
        this.sell_num = sell_num;
        this.image_url = image_url;
    }

    @Generated(hash = 1024547259)
    public Person() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAge() {
        return this.Age;
    }

    public void setAge(String Age) {
        this.Age = Age;
    }

    public String getAddress() {
        return this.Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public int getSell_num() {
        return this.sell_num;
    }

    public void setSell_num(int sell_num) {
        this.sell_num = sell_num;
    }

    public String getImage_url() {
        return this.image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getOnlySign() {
        return this.OnlySign;
    }

    public void setOnlySign(String OnlySign) {
        this.OnlySign = OnlySign;
    }
}
