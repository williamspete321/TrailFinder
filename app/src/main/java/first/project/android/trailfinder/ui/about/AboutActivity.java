package first.project.android.trailfinder.ui.about;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.example.android.trailfinder.R;
import com.example.android.trailfinder.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView contactTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_about);

        setSupportActionBar(binding.activityAboutToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.about));

        titleTextView = binding.titleTextview;
        titleTextView.setText(getString(R.string.app_name));

        contactTV = binding.contactTextview;
        contactTV.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
