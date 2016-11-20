package a2ndrade.cafeteam.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import a2ndrade.cafeteam.R;
import a2ndrade.cafeteam.background.MembersLoader;
import a2ndrade.cafeteam.model.Member;
import a2ndrade.cafeteam.recycler.InsetDividerDecoration;
import a2ndrade.cafeteam.ui.util.CircleTransform;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Member>>{
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindString(R.string.app_name) String title;
    @BindDimen(R.dimen.large_avatar_size) int largeAvatarSize;

    private Unbinder unbinder;
    private CircleTransform circleTransform;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        unbinder = ButterKnife.bind(HomeActivity.this);
        circleTransform = new CircleTransform(HomeActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        final Resources res = getResources();
        recyclerView.addItemDecoration(new InsetDividerDecoration(
                MembersViewHolder.class,
                res.getDimensionPixelSize(R.dimen.divider_height),
                res.getDimensionPixelSize(R.dimen.keyline_2),
                ContextCompat.getColor(HomeActivity.this, R.color.divider)));
        getSupportLoaderManager().initLoader(0, null, HomeActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public Loader<List<Member>> onCreateLoader(int id, Bundle args) {
        return new MembersLoader(HomeActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<List<Member>> loader, List<Member> data) {
        recyclerView.setAdapter(new MembersAdapter(data));
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<Member>> loader) {
        // no-op
    }

    class MembersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        final List<Member> items;

        MembersAdapter(List<Member> memberList) {
            this.items = memberList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case R.layout.member_list_header: {
                    return createHeaderHolder(parent, viewType);
                }
                case R.layout.member_list_item: {
                    return createMembersHolder(parent, viewType);
                }
            }
            throw new IllegalArgumentException();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case R.layout.member_list_header: {
                    bindHeader((HeaderViewHolder) holder);
                    break;
                }
                case R.layout.member_list_item: {
                    bindMember((MembersViewHolder) holder, getMember(position));
                    break;
                }
            }
        }

        @Override
        public int getItemCount() {
            // Adjust for static header
            return items.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? R.layout.member_list_header : R.layout.member_list_item;
        }

        private Member getMember(int adapterPosition) {
            return items.get(adapterPosition - 1);
        }

        private HeaderViewHolder createHeaderHolder(ViewGroup parent, int viewType) {
            return new HeaderViewHolder(
                    getLayoutInflater().inflate(viewType, parent, false));
        }

        private MembersViewHolder createMembersHolder(ViewGroup parent, int viewType) {
            final MembersViewHolder holder = new MembersViewHolder(
                    getLayoutInflater().inflate(viewType, parent, false));
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = holder.getAdapterPosition();
                    if (position == RecyclerView.NO_POSITION) return;
                    final Member member = getMember(position);
                    final ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this,
                                    holder.avatar, HomeActivity.this.getString(R.string.transition_member_avatar));

                    Intent i = new Intent(HomeActivity.this, MemberDetails.class);
                    i.putExtra(MemberDetails.EXTRA_MEMBER, member);
                    HomeActivity.this.startActivity(i, options.toBundle());
                }
            };
            holder.itemView.setOnClickListener(clickListener);
            return holder;
        }

        private void bindMember(MembersViewHolder holder, Member member) {
            holder.name.setText(member.getPrettyName());
            holder.bio.setText(member.bio);
            Glide.with(HomeActivity.this)
                    .load(member.avatar)
                    .transform(circleTransform)
                    .override(largeAvatarSize, largeAvatarSize)
                    .placeholder(R.drawable.avatar_placeholder)
                    .into(holder.avatar);
        }

        private void bindHeader(HeaderViewHolder holder) {
            holder.listHeader.setText(R.string.team_header);
        }
    }

    static class MembersViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name) TextView name;
        @BindView(R.id.avatar) ImageView avatar;
        @BindView(R.id.bio) TextView bio;

        MembersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView listHeader;

        HeaderViewHolder(View itemView) {
            super(itemView);
            listHeader = (TextView) itemView;
        }
    }
}