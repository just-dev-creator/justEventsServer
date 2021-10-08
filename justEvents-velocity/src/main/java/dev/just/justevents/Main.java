package dev.just.justevents;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import dev.just.justevents.utils.Configuration;
import org.slf4j.Logger;

@Plugin(
        id = "justEvents-velocity",
        name = "JustEvents",
        version = "1.0-SNAPSHOT"
)
public class Main {
    public static Configuration configuration;
    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        configuration = new Configuration();
    }
}
