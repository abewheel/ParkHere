package model;
import java.awt.Image;

//import android.graphics.Bitmap;
//import android.media.Image;

/**
 * Created by emmalautz on 10/19/16.
 */

public class Profile {

    private Image profile_pic;
    private int phone_number;
    
    public int getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(int phone_number) {
        this.phone_number = phone_number;
    }

    public Image getProfilePic() {
        return profile_pic;
    }

    public void setProfilePic(Image profile_pic) {
        this.profile_pic = profile_pic;
    }


}
