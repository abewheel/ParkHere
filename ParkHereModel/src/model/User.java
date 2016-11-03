package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by emmalautz on 10/19/16.
 */

public class User implements Serializable{

    private Seeker seeker;
    private long user_id;


    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
    	seeker = (Seeker) in.readObject();
    	user_id = (Long) in.readLong();
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException{
    	out.writeObject(seeker);
    	out.writeLong(user_id);
    }
    
    public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public Seeker getSeeker() {
        return seeker;
    }

    public void setSeeker(Seeker seeker) {
        this.seeker = seeker;
    }

    public Lender getLender() {
        return lender;
    }

    public void setLender(Lender lender) {
        this.lender = lender;
    }

    public Role getCurrent_role() {
        return current_role;
    }

    public void setCurrent_role(Role current_role) {
        this.current_role = current_role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role switchCurrentRole(){
        if (current_role.equals(lender)){
            current_role = seeker;
        }
        else{
            current_role = lender;
        }

        return current_role;
    }

    private Lender lender;
    private  Role current_role;
    private String email;
    private String name;
    private String password;
}
