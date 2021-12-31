package com.kz.audit;

import com.kz.audit.server.WebsocketStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AuditApplication extends SpringBootServletInitializer {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(AuditApplication.class, args);
    }

    @Bean
    public ServletContextInitializer initializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                //new QiNiuApi(ConfigTool.getProp("qiniu.access"), ConfigTool.getProp("qiniu.secret"), ConfigTool.getProp("qiniu.bucket"));
                logger.info("启动成功");
                try {
                    WebsocketStarter.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

}
