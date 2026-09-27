package com.ibm.grocery.api.startup;

import com.ibm.grocery.core.service.DataSeedService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationStartupListener implements ServletContextListener {

    @Inject
    DataSeedService dataSeedService;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        dataSeedService.seed();
    }
}
