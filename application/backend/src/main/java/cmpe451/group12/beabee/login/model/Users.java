package cmpe451.group12.beabee.login.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

import java.util.Collection;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class Users implements UserDetails {
    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;


    @Column(name = "password_reset_token")
    private String password_reset_token;
    @Column(name = "password_reset_token_expiration_date")
    private Date password_reset_token_expiration_date;

    /*
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "AUTHORITIES",
            joinColumns = @JoinColumn(name = "USERNAME"),
            inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID")
    )
    private Set<Authority> authorities;
    */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
        /*
        List<GrantedAuthority> list =new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority("USER"));
        return list;
         */
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
