package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthViewActivity extends AppCompatActivity {

    GridView gridView;
    static MonthViewAdapter adapter;
    ArrayList<My_date> dates;

    int year, month, lastdate, day, dow;
    //날짜의 계산과 해당 월의 시작 요일의 계산을 위한 변수 생성
    //day 는 현재 날짜를 1일으로 설정하기 위한 변수
    //dow 는 1일으로 설정된 현재날짜가 무슨 요일인지 판단하기 위한 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        lastdate = calendar.getActualMaximum(Calendar.DATE);

        calendar.set(2022, 3, 1);

        day = calendar.get(Calendar.DATE);

        dow = calendar.get(Calendar.DAY_OF_WEEK);
//      Calendar 를 이용해 현재 년도, 월, 해당 월의 최대 날짜(30,31) 를 구해준다
//      day와 dow를 이용해 앞서 말한 것 처럼 현재 날짜를 1일으로 바꾸고 요일을 구해 각 변수에 저장한다.(1=일요일,2=월요일...7=토요일)

        TextView tv_Month = (TextView) findViewById(R.id.textview_Month);
        tv_Month.setText("          " + year + "년 " + month + "월");
//      시작했을 때 보여지는 월력의 정보를 해당위치의 텍스트뷰에 setText 해준다.

        dates = new ArrayList<>();

        for(int j = 1; j < dow; j ++) {
            dates.add(new My_date(0));
        }
//      해당 월의 1일의 요일 위치를 저장한 dow를 이용해 해당 요일의 앞부분은 0을 입력해 dates에 추가한다.
//      ex) 화요일일 경우 dow = 3; for문을 j 가 1, 2 일때 두번 반복하여 일요일과 월요일 칸을 비어보이게 작성
        for(int i = 1; i <= lastdate; i ++) {
            dates.add(new My_date(i));
        }
//      현재 월의 최대 일수가 저장된 lastdate를 이용해 i가 1부터 최대 일수까지 증가하게 하여 해당 일자를 dates에 순서대로 추가한다.

        gridView = findViewById(R.id.gridview_month);
        adapter = new MonthViewAdapter(this, R.layout.date_item , dates);
        gridView.setAdapter(adapter);
    }
    public void onClick(View view) {

        Calendar calendar = Calendar.getInstance();
        TextView tv_Month = (TextView) findViewById(R.id.textview_Month);

        switch (view.getId()) {
            case R.id.before_Month: //이전 버튼이 눌렸을 때
                calendar.set(year,month-2, day); //현재 날짜에서 월만 -1 해준다(month의 범위가 0~11이기 때문에 -1 하기 위해 month -2 를 입력해준다.

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                lastdate = calendar.getActualMaximum(Calendar.DATE);

                tv_Month.setText("          " + year + "년 " + month + "월");

                dates.clear();

                dow = calendar.get(Calendar.DAY_OF_WEEK);
                for(int j = 1; j < dow; j ++) {
                    dates.add(new My_date(0));
                }
                for(int i = 1; i <= lastdate; i ++) {
                    dates.add(new My_date(i));
                }
//              바뀐 월에 맞추어 dates를 clear() 한 이후 월력을 새로 입력한다.
                adapter = new MonthViewAdapter(this, R.layout.date_item, dates);
                adapter.notifyDataSetChanged();
                gridView.setAdapter(adapter);
//              바뀐 월력을 갱신하기 위해 어댑터를 새로 생성하고 notifyDataSetChanged()를 호출한 이후 gridView에 새로 어댑터를 set한다.
//              참조 : https://docko.tistory.com/273
                break;

            case R.id.next_Month:
                calendar.set(year,month,day);
//              위와 같은 이유로 month 에 Calendar.MONTH + 1 이 저장되어 있기 때문에 month를 그대로 입력해준다.
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                lastdate = calendar.getActualMaximum(Calendar.DATE);

                tv_Month.setText("          " + year + "년 " + month + "월");

                dates.clear();

                dow = calendar.get(Calendar.DAY_OF_WEEK);
                for(int j = 1; j < dow; j ++) {
                    dates.add(new My_date(0));
                }
                for(int i = 1; i <= lastdate; i ++) {
                    dates.add(new My_date(i));
                }
                adapter = new MonthViewAdapter(this, R.layout.date_item, dates);
                adapter.notifyDataSetChanged();
                gridView.setAdapter(adapter);
                break;
//              이전 버튼과 동일하다.
            case R.id.textview_date:
                Toast.makeText(MonthViewActivity.this, String.format("%d.%d.", year, month),
                        Toast.LENGTH_LONG).show();
//              월력에서 특정 일이 클릭되었을때 선택된 일의 정보 년,월,일 을 출력한다.
                break;
        }
    }
}