package model;

import java.io.Serializable;

/**
 * Created by emmalautz on 10/19/16.
 */

public class User implements Serializable{

    private Seeker seeker;
    private long user_id;
    private Lender lender;
    private  Role current_role;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String bt_customer_id;

    @Override
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	if (seeker != null ) sb.append("seeker: "+seeker.toString()+System.lineSeparator());
    	if (lender != null) sb.append("lender: "+lender.toString()+System.lineSeparator());
    	sb.append("userId: "+user_id+System.lineSeparator());
    	sb.append("email: "+email+System.lineSeparator());
    	sb.append("name: "+firstName+" "+lastName+System.lineSeparator());
    	sb.append("password: "+password+System.lineSeparator());
    	return sb.toString();
    }
    
    public String getBTCutomerId(){
    	return bt_customer_id;
    }
    
    public void setBTCustomerId(String id){
    	this.bt_customer_id = id;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }
    
    public String getLastName() {
        return firstName;
    }

    public void setLastName(String name) {
        this.lastName = name;
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

}
