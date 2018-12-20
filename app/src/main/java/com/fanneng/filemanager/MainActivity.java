package com.fanneng.filemanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.AndroidTestCase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apkfuns.logutils.LogUtils;
import com.fanneng.filemanager.adapter.DBHelper;
import com.fanneng.filemanager.adapter.PersonAdapter;
import com.fanneng.filemanager.common.bean.Person;
import com.fanneng.filemanager.common.dao.DataDao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private List<Person> list;
    private PersonAdapter adapter;
    RecyclerView recyclerView;

    EditText inputName;
    EditText inputAddress;
    EditText inputAge;

    Button add;
    Button delete;
    Button edit;
    Button query;
    EditText deleteIdTv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();

    }

    private void initView() {
        ButterKnife.bind(this);
        recyclerView =findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        adapter = new PersonAdapter(MainActivity.this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        inputName=findViewById(R.id.input_name);
        inputAddress=findViewById(R.id.input_address);
        inputAge=findViewById(R.id.input_age);

        deleteIdTv=findViewById(R.id.delete_id_tv);
        add=findViewById(R.id.add);
        delete=findViewById(R.id.delete);
        edit=findViewById(R.id.edit);
        query=findViewById(R.id.query);

        add.setOnClickListener(mOnClickListener);
        delete.setOnClickListener(mOnClickListener);
        edit.setOnClickListener(mOnClickListener);
        query.setOnClickListener(mOnClickListener);
    }


    /**
     * 单元测试操作sqlLite的各种sql
     *
     */
    public class TestSqlLite extends AndroidTestCase {

        /**
         * 创建表
         *
         * @throws Exception
         */
        public void createTable() throws Exception {
            DBHelper dbHelper = new DBHelper(this.getContext());
            dbHelper.open();

            String deleteSql = "drop table if exists user ";
            dbHelper.execSQL(deleteSql);

            // id是自动增长的主键，username和 password为字段名， text为字段的类型
            String sql = "CREATE TABLE user (id integer primary key autoincrement, username text, password text)";
            dbHelper.execSQL(sql);
            dbHelper.closeConnection();
        }

        /**
         * 插入数据
         *
         * @throws Exception
         */
        public void insert() throws Exception {
            DBHelper dbHelper = new DBHelper(this.getContext());
            dbHelper.open();

            ContentValues values = new ContentValues(); // 相当于map

            values.put("username", "test");
            values.put("password", "123456");

            dbHelper.insert("user", values);

            dbHelper.closeConnection();
        }

        /**
         * 更新数据
         *
         * @throws Exception
         */
        public void update() throws Exception {
            DBHelper dbHelper = new DBHelper(this.getContext());
            dbHelper.open();
            ContentValues initialValues = new ContentValues();
            initialValues.put("username", "changename"); // 更新的字段和值
            // 第三个参数为条件语句
            dbHelper.update("user", initialValues, "id = ?", new String[] { "1" });

            dbHelper.closeConnection();
        }

        /**
         * 删除数据
         *
         * @throws Exception
         */
        public void delete() throws Exception {
            DBHelper dbHelper = new DBHelper(this.getContext());
            dbHelper.open();

            dbHelper.delete("user", "id =?'", new String[] { "1" });

            dbHelper.closeConnection();
        }

        /**
         * 增加字段
         *
         * @throws Exception
         */
        public void addColumn() throws Exception {
            DBHelper dbHelper = new DBHelper(this.getContext());
            dbHelper.open();

            String updateSql = "alter table user add company text";
            dbHelper.execSQL(updateSql);

            dbHelper.closeConnection();
        }

        /**
         * 查询列表
         *
         * @throws Exception
         */
        public void selectList() throws Exception {
            DBHelper dbHelper = new DBHelper(this.getContext());
            dbHelper.open();

            Cursor returnCursor = dbHelper.findList(false, "user", new String[] { "id", "username", "password" }, "username?", new String[] { "test" }, null, null, "id desc", null);
            while (returnCursor.moveToNext()) {
                String id = returnCursor.getString(returnCursor.getColumnIndexOrThrow("id"));
                String username = returnCursor.getString(returnCursor.getColumnIndexOrThrow("username"));
                String password = returnCursor.getString(returnCursor.getColumnIndexOrThrow("password"));
                System.out.println("id=" + id + ";username=" + username + ";" + password + ";\n");
            }

            dbHelper.closeConnection();
        }

        /**
         * 某一条信息
         *
         * @throws Exception
         */
        public void selectInfo() throws Exception {
            DBHelper dbHelper = new DBHelper(this.getContext());
            dbHelper.open();
            Cursor returnCursor = dbHelper.findOne(false,"user", new String[] { "id", "username", "password" }, "id = '1'", null, null, null, "id desc",null);
            if (returnCursor != null) {
                String id = returnCursor.getString(returnCursor.getColumnIndexOrThrow("id"));
                String username = returnCursor.getString(returnCursor.getColumnIndexOrThrow("username"));
                String password = returnCursor.getString(returnCursor.getColumnIndexOrThrow("password"));
                System.out.println("id=" + id + ";username=" + username + ";" + password + ";\n");
            }
        }
    }



    //增
    private void addPerson() {
        String age = inputAge.getText().toString().trim();
        String name = inputName.getText().toString().trim();
        String address = inputAddress.getText().toString().trim();
        Person mPerson = new Person();
        UUID.randomUUID().toString();
        mPerson.setId(1L);
        mPerson.setOnlySign(UUID.randomUUID().toString());
        mPerson.setAge(age);
        mPerson.setName(name);
        mPerson.setAddress(address);
        DataDao.insertData(mPerson);



        /*Observable.create(new ObservableOnSubscribe<Person>() {
            @Override
            public void subscribe(ObservableEmitter<Person> e) throws Exception {
                //执行一些其他操作
                //.............
                //执行完毕，触发回调，通知观察者
                Log.i("TAG", "---> add 新线程" + Thread.currentThread().getId());

                String age = inputAge.getText().toString().trim();
                String name = inputName.getText().toString().trim();
                String address = inputAddress.getText().toString().trim();
                Person mPerson = new Person();
                mPerson.setAge(age);
                mPerson.setName(name);
                mPerson.setAddress(address);

                Boolean isSuccess = false;
                try {
                    DataDao.insertData(mPerson);
                    isSuccess = true;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                if (isSuccess) {
                    e.onNext(mPerson);
                } else {
                    Observable.error(new NullPointerException("插入数据出错！"));
                }

            }
        }).flatMap(new Function<Person, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Person person) throws Exception {
                return Observable.just(person.toString());
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    //观察者接收到通知,进行相关操作
                    public void onNext(String aLong) {
                        Log.i("TAG", "---> add-onNext线程" + Thread.currentThread().getId());
                        System.out.println("我接收到数据了" + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
*/

    }


    //删
    private void deletePerson(long value) {
        Observable<Boolean> observable = Observable.just(value)
                .map(new Function<Long, Boolean>() {
                    @Override
                    public Boolean apply(Long aLong) throws Exception {

                        boolean isSuccess = false;
                        try {
                            DataDao.deleteData(aLong);
                            isSuccess = true;
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        return isSuccess;
                    }
                });
        observable.subscribeOn(Schedulers.io());
        observable.observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Observer<Boolean>() {
            //用于解除订阅
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    LogUtils.d("---> 删除成功");
                } else {
                    LogUtils.d("---> 删除失败");
                }
            }

            //事件队列异常
            @Override
            public void onError(Throwable e) {
                LogUtils.d("---> 删除出错");
            }

            //事件队列完结时调用该方法
            @Override
            public void onComplete() {

            }
        });

    }

    //查
    private void queryPerson() {
        List<Person> tmpList = DataDao.queryAll();
        Log.i("TAG", "queryPerson: "+tmpList.toString());
        if(tmpList!=null&&tmpList.size()>0){
            for (int i = 0; i < tmpList.size(); i++) {
                Person person = tmpList.get(i);
                Log.i("TAG",person.toString());
            }

        }
        /*Observable.create(new ObservableOnSubscribe<List<Person>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Person>> e) throws Exception {
                Log.i("TAG", "---> query 新线程" + Thread.currentThread().getId());
                List<Person> tmpList = new ArrayList<>();
                try {
                    tmpList = DataDao.queryAll();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    Observable.error(new NullPointerException("查询数据出错！"));
                }

                if (tmpList != null) {
                    e.onNext(tmpList);
                }

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Person>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    //观察者接收到通知,进行相关操作
                    public void onNext(List<Person> aLong) {
                        Log.i("TAG", "---> query-onNext线程" + Thread.currentThread().getId());
                        adapter.updateData(aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });*/


    }

    private View.OnClickListener mOnClickListener =new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.add:
                    Log.i("TAG","addPerson");
                    addPerson();
                    break;
                case R.id.delete:
                    String v = deleteIdTv.getText().toString().trim();
                    if (v.length() > 0) {
                        deletePerson(Long.parseLong(v));
                    } else {
                        LogUtils.e("---> 删除所需的TextView值为空");
                    }
                    break;
                case R.id.edit:
                    String age = inputAge.getText().toString().trim();
                    //editPerson(age);
                    inputAge.setText("");
                    break;
                case R.id.query:
                    Log.i("TAG","queryPerson");
                    list.clear();
                    queryPerson();
                    break;
            }
        }
    };

}
