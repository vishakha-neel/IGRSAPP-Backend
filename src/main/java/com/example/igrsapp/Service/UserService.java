package com.example.igrsapp.Service;

import java.util.Iterator;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.igrsapp.Repository.dataEntry.AppUserRepository;
import com.example.igrsapp.beans.UserBean;
import com.example.igrsapp.modals.dataEntry.AppUser;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // AppUser user = userRepository.findByUsername(username);
        // if (user == null || !user.isEnable()) {
        //     throw new UsernameNotFoundException("User not found or disabled");
        // }

        // return User.builder()
        //         .username(user.getUsername())
        //         .password(user.getPassword())
        //         .roles(user.getRole())
        //         .build();
        throw new UsernameNotFoundException("User not found or disabled");
    }

    public AppUser registerUser(String username, String rawPassword, String role) {
        AppUser user = new AppUser();
        user.setUsername(username);
        // user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setEnable(true);
        // return userRepository.save(user);
        return null;
    }

    // @Autowired
	// private SessionFactory sessionFactory;
    
    // public String authUser(String userId, String password) {
	// 	String flage = "Null";
	// 	for (Iterator it = sessionFactory.getCurrentSession()
	// 			.createSQLQuery(
	// 					"Select disable from Users where userId='" + userId + "' and password='" + password + "'")
	// 			.list().iterator(); it.hasNext();) {
	// 		if ((Boolean) it.next())
	// 			flage = "Disabled";
	// 		else
	// 			flage = "Success";
	// 	}
	// 	return flage;
	// }

    // public UserBean getUserBean(String userId) {
	// 	UserBean uBean = new UserBean();
	// 	for (Iterator it = sessionFactory.getCurrentSession().createQuery("From Users user where user.userId='" + userId + "'").list().iterator(); it.hasNext();) {
	// 		// Users user = (Users) it.next();
    //         AppUser user = (AppUser) it.next();
	// 		uBean.setUserId(user.getId());
			
	// 	}
	// 	return uBean;
	// }
}
