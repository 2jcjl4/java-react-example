package com.ibm.grocery.micronaut;

import com.ibm.grocery.core.service.DataSeedService;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;

@Singleton
public class StartupListener implements ApplicationEventListener<ServerStartupEvent> {

    private final DataSeedService dataSeedService;

    public StartupListener(DataSeedService dataSeedService) {
        this.dataSeedService = dataSeedService;
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        dataSeedService.seed();
    }
}
