package fi.hut.soberit.agilefant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;



/* test commit to see if jenkins auto build works
1
2
*/

@Configuration
@ComponentScan(basePackages = "fi.hut.soberit.agilefant.web")
public class WebConfiguration {

    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter bean = new CharacterEncodingFilter();
        bean.setEncoding("UTF-8");
        return bean;
    }

}
