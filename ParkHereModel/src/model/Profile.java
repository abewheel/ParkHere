package model;
import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import javax.swing.ImageIcon;

//import android.graphics.Bitmap;
//import android.media.Image;

/**
 * Created by emmalautz on 10/19/16.
 */

public class Profile implements Serializable{

    private ImageIcon profile_pic;
    private int phone_number;
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
    	profile_pic = (ImageIcon) in.readObject();
    	phone_number = in.readInt();
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException{
    	out.writeObject(profile_pic);
    	out.writeInt(phone_number);
    }
    
    public int getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(int phone_number) {
        this.phone_number = phone_number;
    }

    public ImageIcon getProfilePic() {
        return profile_pic;
    }

    public void setProfilePic(ImageIcon profile_pic) {
        this.profile_pic = profile_pic;
    }


}
