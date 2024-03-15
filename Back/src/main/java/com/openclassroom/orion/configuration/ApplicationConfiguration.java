package com.openclassroom.orion.configuration;

import com.openclassroom.orion.module.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class ApplicationConfiguration {

    private final UserRepository repository;

    public ApplicationConfiguration(UserRepository repository) {
        this.repository = repository;
    }


    /**
     * Returns an instance of UserDetailsService.
     *
     * @return UserDetailsService - the user details service that retrieves user details based on username
     * @throws UsernameNotFoundException - if the user details are not found in the repository
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> (UserDetails) repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Provides an instance of AuthenticationProvider.
     *
     * @return DaoAuthenticationProvider - the authentication provider used by the application
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Retrieves the AuthenticationManager from the provided AuthenticationConfiguration.
     *
     * @param config the AuthenticationConfiguration instance to retrieve the AuthenticationManager from
     * @return the AuthenticationManager retrieved from the AuthenticationConfiguration
     * @throws Exception if an error occurs while retrieving the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Returns a PasswordEncoder instance.
     *
     * @return PasswordEncoder - the password encoder instance used by the application
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
