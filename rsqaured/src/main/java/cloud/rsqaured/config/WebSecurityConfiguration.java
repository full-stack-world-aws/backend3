package cloud.rsqaured.config;

import cloud.rsqaured.config.filter.CorsFilter;
import cloud.rsqaured.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

@ComponentScan(basePackageClasses = UserDetailsServiceImpl.class)
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER - 2)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth,
                                DaoAuthenticationProvider authenticationProvider) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class);

        http
            .csrf().disable()
            .antMatcher("/**") // All requests are protected.
            .authorizeRequests()
                .antMatchers("/v2/api-docs/**", "/swagger-resources/**",
                        "/swagger-ui.html", "/login/**", "/webjars/**", "/public/**").permitAll()
                .anyRequest().authenticated()
            .and().formLogin();
    }

}