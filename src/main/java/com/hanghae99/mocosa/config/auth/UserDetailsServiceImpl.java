package com.hanghae99.mocosa.config.auth;

import com.hanghae99.mocosa.layer.model.User;
import com.hanghae99.mocosa.layer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private  final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByEmail(username);

        if(user == null){
            throw new UsernameNotFoundException(username + "발견되지 않음");
        }

        return new UserDetailsImpl(user);
    }
}