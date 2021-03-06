package kjm.linkverifier.auth.security;


import kjm.linkverifier.auth.security.jwtToken.AuthEntryPointJwtToken;
import kjm.linkverifier.auth.security.jwtToken.AuthTokenFilter;
import kjm.linkverifier.auth.security.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    final UserDetailsServiceImpl userDetailsService;

    private final AuthEntryPointJwtToken authEntryPointJwtToken;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AuthEntryPointJwtToken authEntryPointJwtToken) {
        this.userDetailsService = userDetailsService;
        this.authEntryPointJwtToken = authEntryPointJwtToken;
    }

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authEntryPointJwtToken).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests().antMatchers("/auth/signup/confirm/**").permitAll()
                .antMatchers("/auth/signup/**").permitAll()
                .antMatchers("/files/**").permitAll()
                .antMatchers("/**").permitAll()
                .antMatchers("/links/**").permitAll()
                .antMatchers("/facebook/signin").permitAll()
                .antMatchers("/users/**").permitAll()
                .antMatchers("/comments/**").permitAll()
                .anyRequest().authenticated();


        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
