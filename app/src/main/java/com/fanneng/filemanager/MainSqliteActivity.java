package com.fanneng.filemanager;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fanneng.filemanager.bean.FileBean;
import com.fanneng.filemanager.common.globalconfig.BaseApplication;
import com.fanneng.filemanager.common.globalconfig.DBHelper;
import com.fanneng.filemanager.common.globalconfig.SQLiteDatabaseDao;

import java.io.File;
import java.util.List;

public class MainSqliteActivity extends AppCompatActivity {

    private SQLiteDatabaseDao dao ;
    private  SQLiteDatabase mDb;
    //显示结果
    TextView show;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sqlite);

        DBHelper.instance().initDbHelp(BaseApplication.getApplication(),"123");
        dao = DBHelper.instance().getSQLiteDatabaseDao();
        mDb = DBHelper.instance().getDataBase();


        //创建一个数据库
        Button createDatabase = (Button) findViewById(R.id.createdatabase);
        createDatabase.setOnClickListener(createDatabaseClick);
        //获取所有数据库
        Button getDatabasesList = (Button) findViewById(R.id.getdatabaseslist);
        getDatabasesList.setOnClickListener(getDatabaseListClick);
        //重命名数据库
        Button renameDatabase = (Button) findViewById(R.id.renamedatabase);
        renameDatabase.setOnClickListener(renameDatabaseClick);
        //删除一个数据库
        Button removeDatabase = (Button) findViewById(R.id.removedatabase);
        removeDatabase.setOnClickListener(removeDatabaseClick);
        //创建一个表
        Button createTable = (Button) findViewById(R.id.createtable);
        createTable.setOnClickListener(createTableClick);
        //获取所有的表
        Button getTablesList = (Button) findViewById(R.id.gettableslist);
        getTablesList.setOnClickListener(getTablesListClick);
        //重命名一个表
        Button renameTable = (Button) findViewById(R.id.renametable);
        renameTable.setOnClickListener(renameTableClick);
        //删除一个表
        Button dropTable = (Button) findViewById(R.id.droptable);
        dropTable.setOnClickListener(dropTableClick);
        //为表添加一个字段
        Button addTableColumn = (Button) findViewById(R.id.addtablecolumn);
        addTableColumn.setOnClickListener(addTableColumnClick);
        //获取表的所有列
        Button getTableColumnsList = (Button) findViewById(R.id.gettablecolumnslist);
        getTableColumnsList.setOnClickListener(getTableColumnsListClick);
        //插入一条数据
        Button insertTable = (Button) findViewById(R.id.inserttable);
        insertTable.setOnClickListener(insertTableClick);
        //查询一条数据
        Button queryTable = (Button) findViewById(R.id.querytable);
        queryTable.setOnClickListener(queryTableClick);
        //更新一条数据
        Button updateTable = (Button) findViewById(R.id.updatetable);
        updateTable.setOnClickListener(updateTableClick);
        //删除一条数据
        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(deleteClick);


        //创建数据库和表
        Button createDatabaseAndTab = (Button) findViewById(R.id.createDatabaseAndTab);
        createDatabaseAndTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 DBHelper.instance().initDbHelp(BaseApplication.getApplication(),"1234567");
            }
        });
        //删除数据库
        Button deleteDataBase = (Button) findViewById(R.id.deleteDataBase);
        deleteDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper.instance().removeDatabase("fn_1234567.db");
            }
        });
        //根据ID查询一条数据
        Button selectOneData = (Button) findViewById(R.id.selectOneData);
        selectOneData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileBean fileBean= DBHelper.instance().queryDataById(11);

                if(fileBean!=null){
                    show.setText(""+fileBean.toString());
                }else{
                    show.setText("该条记录不存在");
                }


            }
        });
        //查询所有数据
        Button selectAllData = (Button) findViewById(R.id.selectAllData);
        selectAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<FileBean> fileBeanList = DBHelper.instance().queryTableAllData();

                String S = "";
                FileBean fileBean;
                for (int i = 0; i < fileBeanList.size(); i++) {
                    fileBean =fileBeanList.get(i);
                    S =  S+ fileBean.toString()+"\n";
                }

                show.setText(""+S);

            }
        });



        //插入一条数据
        Button insertOneData = (Button) findViewById(R.id.insertOneData);
        insertOneData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileBean file = new FileBean();
                file.setName_collect("20180618_04111");
                file.setTime_duration("2分20秒");
                file.setTime_create("2018-06-18 16:20:00");
                file.setFile_path("data/06/date.db");
                file.setUnique_key("201809000000111");
                file.setOther("other");
                DBHelper.instance().insertTable(file);
            }
        });
        //删除一条数据
        Button deleteOneData = (Button) findViewById(R.id.deleteOneData);
        deleteOneData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper.instance().deleteDataById(6);
            }
        });
        //删除一个表的所有数据
        Button deleteAllData = (Button) findViewById(R.id.deleteAllData);
        deleteAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper.instance().deleteAllData();
            }
        });

        //通过ID修改一个表的一条数据
        Button updateOne = (Button) findViewById(R.id.updateOne);
        updateOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileBean file = new FileBean();
                file.setName_collect("a20180618_04111");
                file.setTime_duration("a2分20秒");
                file.setTime_create("a2018-06-18 16:20:00");
                file.setFile_path("adata/06/date.db");
                file.setUnique_key("a201809000000111");
                file.setOther("aother");
                DBHelper.instance().updateData(16,file);
            }
        });
        //通过ID修改一个表的一条数据的一个字段
        Button updateOneName = (Button) findViewById(R.id.updateOneName);
        updateOneName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBHelper.instance().updateDataById(15,"name");
            }
        });





        //显示结果
        show = (TextView) findViewById(R.id.showresult);


    }




    /************对按钮事件进行操作的事件响应****************/

    //创建一个数据库
    View.OnClickListener createDatabaseClick = new View.OnClickListener() {

        @SuppressLint("WrongConstant")
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //创建一个名为students.db的数据库,主要是生成另外一个数据库以示区别
            //默认创建一个users.db的数据库
            DBHelper.instance().initDbHelp(BaseApplication.getApplication(),"123456");
            show.setText("创建的数据库路径为\n"
                    + getDatabasePath("fn_123456.db"));

        }
    };


    //创建一个应用程序数据库的个数(list)的事件响应
    View.OnClickListener getDatabaseListClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            String[] dblist = dao.getDatabasesList();
            String rs = "";
            for (String s : dblist) {
                rs += s + "\n";
            }
            show.setText("数据库名称为:\n" + rs);

        }
    };


    //重命名一个数据库的事件响应
    View.OnClickListener renameDatabaseClick = new View.OnClickListener() {

        @SuppressLint("WrongConstant")
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //创建一个data.db的数据库,并命名为renamedata.db数据库
            openOrCreateDatabase("fn_test.db",
                    SQLiteDatabase.CREATE_IF_NECESSARY, null);
            File f = getDatabasePath("fn_test.db");
            File renameFile = getDatabasePath("fn_test_rm.db");
            boolean b = f.renameTo(renameFile);
            if (b) {
                show.setText("fn_test.db已经重命名为fn_test_rm.db");
            } else {
                show.setText("无法重命名");
            }
        }
    };


    //删除一个数据库的事件响应
    View.OnClickListener removeDatabaseClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //删除students.db数据库
            dao.dropDatabase("fn_123456.db");
            //重新获取数据库名称
            String[] dblist = dao.getDatabasesList();
            String rs = "";
            for (String s : dblist) {
                rs += s + "\n";
            }
            show.setText("数据库fn_123456.db已经删除\n现在数据库的名称为：\n" + rs);
        }
    };

    //创建一个表的事件响应

    View.OnClickListener createTableClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //在user.db数据库中插入mytable表,并添加相应的字段
            dao.createTable(mDb, "collect_data");
            show.setText("数据库fn_123456.db已经创建collect_data表\n");

        }
    };


    //获取一个数据库的所有表个数(list)的事件响应

    View.OnClickListener getTablesListClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            //显示所有的表的数据
            String tableNames = dao.getTablesList(mDb);
            show.setText(tableNames);

        }
    };

    //重命名一个表的事件响应

    View.OnClickListener renameTableClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //创建一个testtable的表
            dao.createTable(mDb, "testtable");
            //将testtable重命名为newtable
            boolean b = dao.alterTableRenameTable(mDb, "testtable", "newtable");
            if (b) {
                show.setText("testtable已经重命名为\nnewtable表\n");
            } else {
                show.setText("newtable已经存在\n请删除(drop table)后重试");
            }
        }
    };

    //删除一个表的事件响应

    View.OnClickListener dropTableClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //删除	newtable表
            dao.dropTable(mDb, "collect_data");
            //显示所有的表的数据
            String tableNames = dao.getTablesList(mDb);
            show.setText("collect_data已经删除\n现在表名称为:\n" + tableNames);
        }
    };


    //修改一个表(给表添加一个字段)的事件响应

    View.OnClickListener addTableColumnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //默认添加一个password字段,类型为varchar,长度为30
            boolean b = dao.alterTableAddColumn(mDb, "collect_data", "other", " varchar(30)");
            if (b) {show.setText("已经添加other字段\n字符类型为:varchar\n长度为:30");}
            else {show.setText("collect_data表中other字段已经存在");}
        }
    };

    //获取一个表的所有列的名称事件响应

    View.OnClickListener getTableColumnsListClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            String str = dao.getTableColumns(mDb,"collect_data");
            show.setText("collect_data表的列名:\n" + str);
        }
    };


    //对一个表添加一个数据的事件响应

    View.OnClickListener insertTableClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            FileBean file = new FileBean();
            file.setName_collect("20180618_04111");
            file.setTime_duration("2分20秒");
            file.setTime_create("2018-06-18 16:20:00");
            file.setFile_path("data/06/date.db");
            file.setUnique_key("201809000000111");
            file.setOther("other");
            dao.insert(mDb, "collect_data", file);

            Cursor c = dao.getAllData(mDb, "collect_data");


            if (c.moveToLast()) {
                String id = c.getString(0);
                String name_collect = c.getString(1);
                String time_duration = c.getString(2);
                String time_create = c.getString(3);
                String file_path = c.getString(4);
                String unique_key = c.getString(5);
                String other = c.getString(6);

                show.setText("最新添加的一条数据:\n" + "id:" + id + "\nname_collect:" + name_collect + "\ntime_create:" + time_create
                        + "file_path:" + file_path + "\nunique_key:" + unique_key + "\nother:" + other
                );
            }

        }
    };

    //查询一个表的所有数据记录的事件响应

    View.OnClickListener queryTableClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //默认查询mytable所有数据
            Cursor c = dao.getAllData(mDb, "collect_data");
            String s = "";
            int columnsSize = c.getColumnCount();
            String[] columns = c.getColumnNames();
            String columnsName = "";
            //获取表头
            for (String col : columns) {

                columnsName += col + "\u0020 \u0020";
            }
            //获取表的内容
            while (c.moveToNext()) {

                for (int i = 0; i < columnsSize; i++) {
                    s += c.getString(i) + "\u0020 \u0020";
                }
                s += "<br>";
            }
            show.setText(Html.fromHtml("<h5>" + columnsName + "</h5>" + s));
        }
    };

    //更新一个表的数据的事件响应

    View.OnClickListener updateTableClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Cursor c = dao.getAllData(mDb, "collect_data");
            if (c.moveToFirst()) {

                int first = Integer.valueOf(c.getString(0));

                //默认修改第一条记录
                FileBean file = new FileBean();
                file.setName_collect("20180618_04111");
                file.setTime_duration("2分20秒");
                file.setTime_create("2018-06-18 16:20:00");
                file.setFile_path("data/06/date.db");
                file.setUnique_key("201809000000111");
                file.setOther("other");

               // dao.update(mDb, "collect_data", first, file);
                dao.updateName(mDb, "collect_data", first, "name_20180618_04111");
                Cursor u = dao.queryById(mDb, "collect_data", first);
                u.moveToFirst();
                show.setText("id为:" + first + "的记录已经修改:\nid:" + first + "\nname_collect:" + u.getString(1));

            } else {

                show.setText("没有要更新的记录！请添加数据后再作修改");
            }
        }
    };

    //删除一个表的一条数据的事件响应

    View.OnClickListener deleteClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Cursor c = dao.getAllData(mDb, "collect_data");
            if (c.moveToLast()) {
                int last = Integer.valueOf(c.getString(0));

                //默认删除最后一条记录
                boolean b = dao.delete(mDb, "collect_data", last);
                if (b) {
                    show.setText("成功删除id为:\n" + last + "的记录！");
                }
            } else {
                show.setText("没有要删除的记录！");
            }
        }
    };


    //退出时关闭数据库
    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        dao.closeConnection();
    }


}
