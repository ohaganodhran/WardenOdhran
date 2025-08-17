package com.warden.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User extends GenericEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = -1L;
    
    private String username;
   
    @Column(nullable = false)
    private String email;
   
    private String passwordHash;
    private Date lastLogin;
    private Date lastPasswordReset;
    
    @Transient
    private String password;

    @Transient
    private String passwordConfirm;
    

    public User(String email, String name) {
    	this.email = email;
    	this.username = name;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
        boolean flag = false;

        if (this == obj) {
            flag = true;
        }
        else if (obj instanceof User) {
            User that = (User) obj;
            if (this.email.equals(that.email)) {
                flag = true;
            }
        }
        return flag;
	}
    @Override
	public String toString() {
		return "User [id=" + id + ", Name=" + username + ", email=" + email + ", passwordHash=" + passwordHash + ", lastLogin=" + lastLogin
				+ ", lastPasswordReset=" + lastPasswordReset + "]";
	}
    
}