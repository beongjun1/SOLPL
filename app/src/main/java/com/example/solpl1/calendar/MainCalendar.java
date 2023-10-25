package com.example.solpl1.calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.solpl1.R;
import com.example.solpl1.MainActivity;
import com.example.solpl1.chat.Activity.ChatActivity;
import com.example.solpl1.map.MainMap;
import com.example.solpl1.mypage.mypage_main_activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import org.threeten.bp.LocalDate;
import org.threeten.bp.DayOfWeek;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainCalendar extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Button select_btn;
    private final String TAG = this.getClass().getSimpleName();
    private List<CalendarDay> selectedDates;  // 선택된 날짜 리스트 변수

    private MaterialCalendarView calendarView;
    EditText trip_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        calendarView = findViewById(R.id.calendarview);
        bottomNavigationView = findViewById(R.id.bottomNavi);
        select_btn = findViewById(R.id.calendar_select_btn);
        trip_title = findViewById(R.id.calendar_title);
        // 첫 시작 요일이 월요일이 되도록 설정
        calendarView.state()
                .edit()
                .setFirstDayOfWeek(DayOfWeek.of(Calendar.SATURDAY))
                .commit();

        // 월, 요일을 한글로 보이게 설정
        calendarView.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        calendarView.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));

        // 좌우 화살표 사이 연, 월의 폰트 스타일 설정
        calendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader);

        // 요일 선택 시 정의한 drawable 적용 되도록 함
        calendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                selectedDates = dates;  // 선택된 날짜 리스트 저장
                // 아래 로그를 통해 시작일, 종료일이 어떻게 찍히는지 확인하고 본인이 필요한 방식에 따라 바꿔 사용한다
                // UTC 시간을 구하려는 경우 이 라이브러리에서 제공하지 않으니 별도의 로직을 짜서 만들어내 써야 한다
                String startDay = dates.get(0).getDate().toString();
                String endDay = dates.get(dates.size() - 1).getDate().toString();
                Log.e(TAG, "시작일 : " + startDay + ", 종료일 : " + endDay);
            }
        });

        // 일자 선택 시 내가 정의한 드로어블이 적용되도록 한다
        calendarView.addDecorators(new DayDecorator(this));

        // 좌우 화살표 가운데의 연/월이 보이는 방식 커스텀
        calendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                // CalendarDay라는 클래스는 LocalDate 클래스를 기반으로 만들어진 클래스다
                // 때문에 MaterialCalendarView에서 연/월 보여주기를 커스텀하려면 CalendarDay 객체의 getDate()로 연/월을 구한 다음 LocalDate 객체에 넣어서
                // LocalDate로 변환하는 처리가 필요하다
                LocalDate inputText = day.getDate();
                String[] calendarHeaderElements = inputText.toString().split("-");
                StringBuilder calendarHeaderBuilder = new StringBuilder();
                calendarHeaderBuilder.append(calendarHeaderElements[0])
                        .append(" ")
                        .append(calendarHeaderElements[1]);
                return calendarHeaderBuilder.toString();
            }
        });


        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDates != null && !selectedDates.isEmpty()) {
                    if (isEditTextEmpty(trip_title)) {
                        Toast.makeText(MainCalendar.this, "여행의 이름을 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String startDay = selectedDates.get(0).getDate().toString();
                        String endDay = selectedDates.get(selectedDates.size() - 1).getDate().toString();
                        Log.e(TAG, "선택된 시작일 : " + startDay + ", 종료일 : " + endDay);

                        //데이터베이스에 날짜들과 제목 저장
                        save_to_database(startDay,endDay);
                    }
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_main:
                        Intent intent = new Intent(MainCalendar.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_calendar:
                        break;
                    case R.id.nav_chat:
                        Intent intent2 = new Intent(MainCalendar.this, ChatActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_map:
                        Intent intent3 = new Intent(MainCalendar.this, MainMap.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_mypage:
                        Intent intent4 = new Intent(MainCalendar.this, mypage_main_activity.class);
                        startActivity(intent4);
                        finish();
                        break;
                }
                return true;
            }
        });

    }

    private void save_to_database(String startDay, String endDay) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userEmail = user.getEmail();
        String currentUserId = user.getUid(); // 현재 사용자의 UID 가져오기
        DatabaseReference user_account_database;
        String convertedPath = convertToValidPath(userEmail);

        user_account_database = FirebaseDatabase.getInstance().getReference("UserAccount");
        Log.e("캡스톤", user_account_database.toString());
        user_account_database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String idToken = snapshot.child(currentUserId).child("idToken").getValue(String.class);

                if (idToken != null) {
                    // 데이터베이스 경로 설정 및 저장
                    DatabaseReference tripRef = FirebaseDatabase.getInstance().getReference("trip").child(convertedPath);
                    String tripId = tripRef.push().getKey(); // 새로운 여행에 대한 고유 ID 생성
                    String key = convertedPath+tripId;

                    // 여행 정보를 준비
                    String tripTitle = trip_title.getText().toString();
                    Map<String, Object> tripInfo = new HashMap<>();
                    tripInfo.put("startDay", startDay);
                    tripInfo.put("endDay", endDay);
                    tripInfo.put("title", tripTitle);
                    tripInfo.put("user_id_token",idToken);
                    tripInfo.put("key",key);

                    tripRef.child(key).setValue(tripInfo);

                    //첫 날과 마지막 날 보내기
                    Intent intent = new Intent(MainCalendar.this, EditCalendar.class);
                    intent.putExtra("startDay", startDay); // "startDay"라는 키로 시작일 전달
                    intent.putExtra("endDay", endDay); // "endDay"라는 키로 종료일 전달
                    intent.putExtra("key",key);
                    Log.d("key","key : "+key);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), startDay + " ~ "+endDay, Toast.LENGTH_LONG).show();

                }
            }

                @Override
                public void onCancelled (@NonNull DatabaseError error){


            }
     });
    }
    //문자열을 데이터베이스 경로에 사용 가능한 형식으로 변환
    public static String convertToValidPath(String input) {
        // 허용되는 문자: 알파벳 소문자, 숫자, 밑줄(_)
        String validCharacters = "abcdefghijklmnopqrstuvwxyz0123456789_";
        // 입력된 문자열을 소문자로 변환하고 허용되지 않는 문자를 밑줄로 대체
        String converted = input.toLowerCase().replaceAll("[^" + validCharacters + "]", "_");
        return converted;
    }

    /* 선택된 요일의 background를 설정하는 Decorator 클래스 */
    private static class DayDecorator implements DayViewDecorator {
        private final Drawable drawable;

        public DayDecorator(Context context) {
            drawable = ContextCompat.getDrawable(context, R.drawable.calendar_selector);
        }
        // true 리턴 시 모든 요일에 내가 설정한 drawable 적용된다
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return true;
        }

        // 일자 선택 시 내가 정의한 드로어블이 적용되도록 한다
        @Override
        public void decorate(DayViewFacade view) {
            view.setSelectionDrawable(drawable);
            view.addSpan(new StyleSpan(Typeface.BOLD));   // 달력 안의 모든 숫자들이 볼드 처리됨
        }

    }

    private boolean isEditTextEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }
}
