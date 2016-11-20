package a2ndrade.cafeteam.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import a2ndrade.cafeteam.R;
import a2ndrade.cafeteam.model.Member;
import a2ndrade.cafeteam.ui.util.CircleTransform;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MemberDetails extends AppCompatActivity {
    public static final String EXTRA_MEMBER = "EXTRA_MEMBER";

    @BindView(R.id.name) TextView name;
    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.bio) TextView bio;
    @BindView(R.id.title) TextView title;

    private Member member;
    private Unbinder unbinder;
    private CircleTransform circleTransform;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);
        unbinder = ButterKnife.bind(MemberDetails.this);
        circleTransform = new CircleTransform(this);
        if (getIntent().hasExtra(EXTRA_MEMBER)) {
            member = getIntent().getParcelableExtra(EXTRA_MEMBER);
            bindMember();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void bindMember() {
        if (member == null) {
            return;
        }
        Glide.with(MemberDetails.this)
                .load(member.avatar)
                .placeholder(R.drawable.avatar_placeholder)
                .transform(circleTransform)
                .into(avatar);
        name.setText(member.getPrettyName());
        bio.setText(member.bio);
        title.setText(member.title);
    }
}
