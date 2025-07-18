package TFG.Terranaturale.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class Config {

    public Config() {
        // Empty constructor - no security dependencies
    }

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
