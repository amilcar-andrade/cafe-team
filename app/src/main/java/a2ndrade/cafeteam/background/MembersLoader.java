package a2ndrade.cafeteam.background;

import android.content.Context;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import a2ndrade.cafeteam.R;
import a2ndrade.cafeteam.model.Member;

/**
 * Class to encapsulate where we are loading the team from.
 *
 * Android will not complain if you open a raw resource from the UI thread but it makes more sense
 * to open the resource in the background.
 *
 * NOTE: In a real application you should use OkHttp, Retrofit, Volley or whatever here.
 */

public class MembersLoader extends android.support.v4.content.AsyncTaskLoader<List<Member>> {
    private List<Member> data;
    private final JsonAdapter<List<Member>> jsonAdapter;

    public MembersLoader(Context context) {
        super(context);
        Moshi moshi = new Moshi.Builder().build();
        Type listOfMembersType = Types.newParameterizedType(List.class, Member.class);
        jsonAdapter = moshi.adapter(listOfMembersType);
    }

    @Override
    public List<Member> loadInBackground() {
        StringBuilder sb = new StringBuilder();
        Scanner s = new Scanner(getContext().getResources().openRawResource(R.raw.team));
        while (s.hasNextLine()) {
            sb.append(s.nextLine());
        }
        final String jsonAsString = sb.toString();
        try {
            return jsonAdapter.fromJson(jsonAsString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Return empty list if there is an error
        return Collections.<Member>emptyList();
    }

    @Override
    protected void onStartLoading() {
        if (data != null) {
            deliverResult(data);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<Member> data) {
        this.data = data;
        super.deliverResult(data);
    }
}
