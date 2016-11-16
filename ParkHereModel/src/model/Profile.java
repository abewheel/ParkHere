package model;
import java.io.Serializable;

//import javax.swing.ImageIcon;

//import android.graphics.Bitmap;
//import android.media.Image;

/**
 * Created by emmalautz on 10/19/16.
 */

public class Profile implements Serializable{

   // private transient ImageIcon profile_pic;
    private String phone_number;
    
    @Override
    public String toString(){
    	return "phone: "+phone_number + System.lineSeparator();
    }
    
    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

//    public ImageIcon getProfilePic() {
//        return profile_pic;
//    }
//
//    public void setProfilePic(ImageIcon profile_pic) {
//        this.profile_pic = profile_pic;
//    }


}
