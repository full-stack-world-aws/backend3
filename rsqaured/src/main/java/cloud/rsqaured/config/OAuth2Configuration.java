package cloud.rsqaured.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
@Order(6)
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager auth;

    private final DataSource dataSource;

    private final UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;


    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public OAuth2Configuration(AuthenticationManager auth,
                               DataSource dataSource,
                               @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
                               PasswordEncoder passwordEncoder) {
        this.auth = auth;
        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public JdbcTokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        endpoints
                .authorizationCodeServices(authorizationCodeServices())
                .authenticationManager(auth)
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore())
                .approvalStoreDisabled();
    }
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .jdbc(dataSource);

    }
}
