package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthViewActivity extends AppCompatActivity {
    Intent intent;

    GridView gridView;
    static MonthViewAdapter adapter;
    ArrayList<My_date> dates;

    int year, month, lastdate, day, dow;
//  날짜의 계산과 해당 월의 시작 요일의 계산을 위한 변수 생성
//  day 는 현재 날짜를 1일으로 설정하기 위한 변수
//  dow 는 1일으로 설정된 현재날짜가 무슨 요일인지 판단하기 위한 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar calendar = Calendar.getInstance();

        intent = getIntent();

        if(!TextUtils.isEmpty(intent.getStringExtra("year")))
            year = Integer.parseInt(intent.getStringExtra("year"));
        else
            year = calendar.get(Calendar.YEAR);

        if(!TextUtils.isEmpty(intent.getStringExtra("month")))
            month = Integer.parseInt(intent.getStringExtra("month"));
        else
            month = calendar.get(Calendar.MONTH) + 1;
//          표기되는 월 (1~12) 와 Calendar 상의 월 (0~11) 간에 1의 차이가 있어 표기할 월인 month 에는 1을 더해서 저장한다.

        if(!TextUtils.isEmpty(intent.getStringExtra("lastdate")))
            lastdate = Integer.parseInt(intent.getStringExtra("lastdate"));
        else
            lastdate = calendar.getActualMaximum(Calendar.DATE);
//      인텐트에 저장된 값이 있다면 해당 값으로 날짜정보를 시작하고 처음부터 시작했다면 Calendar 를 이용해 현재 날짜의 정보를 가져온다.

        calendar.set(year, month-1, 1);
//      현재 날짜를 위에서 가져온 year년 month월 1일으로 설정한다. (day,dow 를 구할 때 용이하기 위해 일자를 1일으로 설정한다.)

        day = calendar.get(Calendar.DATE);
        dow = calendar.get(Calendar.DAY_OF_WEEK);
//      Calendar 를 이용해 현재 년도, 월, 해당 월의 최대 날짜(30,31) 를 구해준다
//      day 와 dow 를 이용해 앞서 말한 것 처럼 현재 날짜를 1일으로 바꾸고 요일을 구해 각 변수에 저장한다.(1=일요일,2=월요일...7=토요일)

        TextView tv_Month = (TextView) findViewById(R.id.textview_Month);
        tv_Month.setText(year + "년 " + month + "월");
//      시작했을 때 보여지는 월력의 정보를 해당위치의 텍스트뷰에 setText 해준다.

        dates = new ArrayList<>();

        for(int j = 1; j < dow; j ++) {
            dates.add(new My_date(0));
        }
//      해당 월의 1일의 요일 위치를 저장한 dow 를 이용해 해당 요일의 앞부분은 0을 입력해 dates 에 추가한다.
//      ex) 화요일일 경우 dow = 3; for 문을 j 가 1, 2 일때 두번 반복하여 일요일과 월요일 칸을 비어보이게 작성
        for(int i = 1; i <= lastdate; i ++) {
            dates.add(new My_date(i));
        }
//      현재 월의 최대 일수가 저장된 lastdate 를 이용해 i가 1부터 최대 일수까지 증가하게 하여 해당 일자를 dates에 순서대로 추가한다.
        calendar.set(year,month-1,lastdate);
        for(int k = calendar.get(Calendar.DAY_OF_WEEK); k < 7; k++) {
            dates.add(new My_date(0));
        }
//      구분선을 위한 배경색이 그리드뷰의 빈칸에 나타나는 현상을 막기 위해
//      현재 날짜를 월의 마지막날으로 설정한 후에 for 문을 이용해 현재 요일 변수부터 토요일 변수인 7이 될 때 까지 에 0을 입력해 dates 에 추가한다.

        gridView = findViewById(R.id.gridview_month);
        adapter = new MonthViewAdapter(this, R.layout.date_item , dates);
        gridView.setAdapter(adapter);
//      생성된 현재 월의 아이템 ArrayList<My_date> dates 를 어댑터에 설정해준 후
//      어댑터를 gridView 에 설정해준다.

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //그리드뷰 아이템 클릭 이벤트 (클릭한 날짜 정보 출력)
            @Override
            public void onItemClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {
                if((a_position+1)>=dow && (a_position+2) <= dow + lastdate)
//                  클릭한 위치가 dow 보다 큰 경우 && dow + lastdate보다 작거나 같은 경우에만 출력
//                  ex) 1일 : 금요일 -> dow = 6 / 1일 앞의 목요일 선택시 -> a_position = 4
//                  ex) 3월 : 1일 화요일, 31일 목요일 -> dow = 3, lastdate = 31 / 31일 뒤의 금요일 선택시 -> a_position = 33
//                  a_position 은 그리드뷰의 시작 주소가 0 이지만 dow 는 시작인 일요일이 1이기때문에 비교를 위해 a_position 에 1을 더한 후 비교한다.
//                  lastdate 도 같은 이유로 1을 더해 dow + lastdate 와 비교할 때는 1을 더 더해 a_position 에 2를 더한 후 비교한다.
                    Toast.makeText(MonthViewActivity.this,
                            year +"."+month+"." + (a_position-dow+2), Toast.LENGTH_SHORT).show();
//                  출력 또한 동일한 이유로 a_position - dow 를 했을 때 a_position 은 0부터 시작 dow 는 1부터 시작하여 +2를 해 주어야 해당 위치의 날짜가 알맞게 출력된다.
            }
        });
    }
    public void mClick(View view) { //버튼 클릭 이벤트 (달 바꿈)
        Calendar calendar = Calendar.getInstance();

        switch (view.getId()) {
            case R.id.before_Month: //이전 버튼이 눌렸을 때
                calendar.set(year, month - 2, day); //현재 날짜에서 월만 -1 해준다(month 의 범위가 0~11이기 때문에 -1 하기 위해 month -2 를 입력해준다.)
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                lastdate = calendar.getActualMaximum(Calendar.DATE);
//              인텐트에 넘겨줄 정보인 year,month,lastdate 를 버튼을 눌러 변경된 달의 정보로 업데이트 해 준다.

                finish();
//              현재 액티비티 종료

                Intent intent1 = new Intent(this, MonthViewActivity.class);
                intent1.putExtra("year", String.valueOf(year));
                intent1.putExtra("month", String.valueOf(month));
                intent1.putExtra("lastdate", String.valueOf(lastdate));
                startActivity(intent1);
//              인텐트 생성하여 버튼을 눌러 바뀌게 된 월의 정보를 각 변수의 이름을 key 값으로 설정해서 넘겨주고 액티비티를 다시 시작한다.
                break;

            case R.id.next_Month:
                calendar.set(year, month, day);
//              위와 같은 이유로 month 에 Calendar.MONTH + 1 이 저장되어 있기 때문에 month 를 그대로 입력해준다.
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                lastdate = calendar.getActualMaximum(Calendar.DATE);

                finish();

                Intent intent2 = new Intent(this, MonthViewActivity.class);
                intent2.putExtra("year", String.valueOf(year));
                intent2.putExtra("month", String.valueOf(month));
                intent2.putExtra("lastdate", String.valueOf(lastdate));
                startActivity(intent2);
                break;
//              이전 버튼과 동일하다.
        }
    }
}