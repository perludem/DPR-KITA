package id.gits.dprkita.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;
import id.gits.dprkita.fragment.AgendaFragment;

public class CalendarActivity extends BaseActivity implements CalendarPickerView.OnDateSelectedListener {

    @InjectView(R.id.calendar_view)
    CalendarPickerView calendar;

    public static void startActivity(Context activity) {
        activity.startActivity(new Intent(activity, CalendarActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getResources().getString(R.string.pilih_tanggal));
        Calendar beforeYear = Calendar.getInstance();
        beforeYear.set(2014, Calendar.JANUARY, 1);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.MONTH, 1);
        Date today = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", new Locale("in", "ID"));
        Date selectedDate = today;
        try {
            selectedDate = dateFormat.parse(AgendaFragment.dateCalendar);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.init(beforeYear.getTime(), nextYear.getTime())
                .withSelectedDate(selectedDate).withHighlightedDate(today);
        calendar.setOnDateSelectedListener(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_calendar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSelected(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Intent intent = new Intent();
        intent.putExtra(AgendaFragment.DATE_AGENDA, dateFormat.format(date));
        setResult(RESULT_OK, intent);

        AgendaFragment.dateCalendar = dateFormat.format(date);
        finish();
    }

    @Override
    public void onDateUnselected(Date date) {

    }
}
