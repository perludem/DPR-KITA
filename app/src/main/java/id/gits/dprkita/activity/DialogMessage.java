package id.gits.dprkita.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;

public class DialogMessage extends ActionBarActivity {

    public static final String MESSAGE = "mp_message";
    @InjectView(R.id.message)
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_message);
        ButterKnife.inject(this);
        getSupportActionBar().hide();
        String data = getIntent().getStringExtra(MESSAGE);
        message.setText(data);

    }
}
