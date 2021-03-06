package ru.ozon.config;

import org.aeonbits.owner.Config;

import java.net.URL;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:web.properties"
})
public interface WebConfig extends Config {

    @Key("web.isRemote")
    @DefaultValue("false")
    boolean isRemote();

    @Key("web.remote.url")
    URL remoteBrowserUrl();

    @Key("web.enableVideo")
    @DefaultValue("false")
    boolean isVideoOn();
}
