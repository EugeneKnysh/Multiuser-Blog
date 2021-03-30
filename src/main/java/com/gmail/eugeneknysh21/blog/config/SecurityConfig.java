package com.gmail.eugeneknysh21.blog.config;

import com.gmail.eugeneknysh21.blog.services.impl.CustomOAuth2UserService;
import com.gmail.eugeneknysh21.blog.services.impl.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfig jwtConfig;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> customRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;
//    private final UserDetailsService userDetailsService;
//    private final DataSource dataSource;

//    @Value("${remember.secret}")
//    private String secretKey;

//    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, DataSource dataSource) {
//        this.userDetailsService = userDetailsService;
//        this.dataSource = dataSource;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .antMatchers("/", "/login").permitAll()
//                .and()
//                    .formLogin()
//                    .loginPage("/login")
//                    .defaultSuccessUrl("/", false)
//                    .failureUrl("/login?error=true")
//                .and()
//                    .rememberMe().key(secretKey)
//                    .userDetailsService(userDetailsService)
//                    .tokenRepository(persistentTokenRepository())
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("access_token")
                    .logoutSuccessUrl("/")
                .and()
                    .apply(jwtConfig)
                .and()
                    .oauth2Login()
                    .loginPage("/login")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .authorizationEndpoint()
                        .authorizationRequestRepository(customRepository)
                .and()
                    .userInfoEndpoint()
                        .oidcUserService(customOidcUserService)
                        .userService(customOAuth2UserService);
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) {
//        auth.authenticationProvider(daoAuthenticationProvider());
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

//    @Bean
//    protected DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//        return daoAuthenticationProvider;
//    }
//
//    @Bean
//    protected PersistentTokenRepository persistentTokenRepository() {
//        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
//        jdbcTokenRepository.setDataSource(dataSource);
//        return jdbcTokenRepository;
//    }
}
