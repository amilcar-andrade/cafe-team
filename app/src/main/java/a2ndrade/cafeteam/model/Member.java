package a2ndrade.cafeteam.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Models a team member
    {
        "avatar": "https://media.licdn.com/media/AARmMw.jpg",
        "bio": "I have zero cycles for this...",
        "firstName": "Stephen",
        "id": "0",
        "lastName": "Brandon",
        "title": "Lead DevOps"
    }
 */

public class Member implements Parcelable{
    public final String avatar;
    public final String bio;
    public final String firstName;
    public final String id;
    public final String lastName;
    public final String title;

    private Member(Parcel in) {
        avatar = in.readString();
        bio = in.readString();
        firstName = in.readString();
        id = in.readString();
        lastName = in.readString();
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(avatar);
        dest.writeString(bio);
        dest.writeString(firstName);
        dest.writeString(id);
        dest.writeString(lastName);
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    @Override
    public String toString() {
        return "avatar=" + avatar + ",bio=" + bio + ",firstName=" + firstName + ",id=" + id
                + ",lastname=" + lastName + ",title=" + title;
    }

    public String getPrettyName() {
        return lastName + " " + firstName;
    }
}
