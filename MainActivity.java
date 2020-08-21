package com.example.mssqlconn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    EditText etSql;

    private static String ip = "172.30.1.52"; //접속할 서버측의 IP, 현재는 로컬에서 진행하니 이 컴퓨터의 IP주소를 할당하면 된다.
    private static String port = "49670"; //SQL 구성 관리자에서 TCP/IP 구성 중 모든 IP포트를 설정하는 구간에서 동적 포트를 적으면 된다.
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "Real_DiadooDB"; //접속할 데이터베이스 이름
    private static String username = "duser"; //서버의 ID PW
    private static String password = "qwer";
    private static String url ="jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database;

    private Connection connection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //인터넷 퍼미션을 요구하도록 함
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},PackageManager.PERMISSION_GRANTED);


        //레이아웃의 에디트텍스트를 가져옴
        etSql = (EditText)findViewById(R.id.sql_et);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            //연결시도
            Class.forName(Classes);
            connection = DriverManager.getConnection(url,username,password);
            //연결성공 후 SUCCESS문자 표시
            etSql.setText("SUCCESS");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            //실패시 에러 표시
            etSql.setText("ERROR");
        }
    }

    public void sqlButton(View view) {

        if(connection != null){
            Statement statement = null;
            try {
                statement = connection.createStatement();
                //쿼리문을 이용해 가져올 데이터값을 정한다.
                ResultSet resultSet = statement.executeQuery("SELECT groupName FROM groupTbl WHERE groupNum = 8");
                while(resultSet.next()){
                    //가져온 모든 값들을 next()메소드로 모두 순회하여 띄우도록 한다.
                    etSql.setText(resultSet.getString(1));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }else{
            etSql.setText("Connection is null");
        }


    }

}