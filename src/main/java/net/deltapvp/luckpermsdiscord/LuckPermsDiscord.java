package net.deltapvp.luckpermsdiscord;

import club.minnced.discord.webhook.WebhookClient;
import me.lucko.luckperms.common.api.LuckPermsApiProvider;
import me.lucko.luckperms.common.config.LuckPermsConfiguration;
import me.lucko.luckperms.common.config.generic.KeyedConfiguration;
import me.lucko.luckperms.common.config.generic.adapter.ConfigurationAdapter;
import me.lucko.luckperms.common.plugin.LuckPermsPlugin;
import net.deltapvp.luckpermsdiscord.events.LogListener;
import net.deltapvp.luckpermsdiscord.events.NodeListeners;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.extension.Extension;

import java.lang.reflect.Field;
import java.util.logging.Logger;

public class LuckPermsDiscord implements Extension {
    private final LuckPerms luckPerms;
    private WebhookClient client;

    public LuckPermsDiscord(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    @Override
    public void load() {
        // first time startup message
        if (System.getProperty("luckpermsdiscordnotfirsttime") == null) {
            Logger.getLogger("LuckPermsDiscord").info("It looks like this is your first time using this extension.");
            Logger.getLogger("LuckPermsDiscord").info("Go into the luckperms config and add `discord-url` somewhere with the webhook link.");
            Logger.getLogger("LuckPermsDiscord").info("Example: discord-url: \"https://discord.com/webhook/test\"");
            Logger.getLogger("LuckPermsDiscord").info("");
            System.setProperty("luckpermsdiscordnotfirsttime", "true");
        }
        try {
            discordStuff();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new NodeListeners(client, luckPerms).register();
        new LogListener(client, luckPerms).register();
    }

    @Override
    public void unload() {
        Logger.getLogger("LuckPermsDiscord").info("Disabling...");
    }

    public void discordStuff() throws Exception {
        // check if the url is in the config, if it's not set it
        String URL = getConfigurationAdapter((LuckPermsApiProvider) luckPerms).getString("discord-url", null);
        if (URL == null) {
            Logger.getLogger("LuckPermsDiscord").info("`discord-url` is not set in the config. set it to a webhook link.");
            this.unload();
        }
        client = WebhookClient.withUrl(URL);
    }

    private ConfigurationAdapter getConfigurationAdapter(LuckPermsApiProvider luckPerms) throws Exception {
        Field apiProviderPluginField = LuckPermsApiProvider.class.getDeclaredField("plugin");
        apiProviderPluginField.setAccessible(true);
        LuckPermsPlugin plugin = (LuckPermsPlugin) apiProviderPluginField.get(luckPerms);

        LuckPermsConfiguration configuration = plugin.getConfiguration();

        Field configurationAdapterField = KeyedConfiguration.class.getDeclaredField("adapter");
        configurationAdapterField.setAccessible(true);
        return (ConfigurationAdapter) configurationAdapterField.get(configuration);
    }
}
