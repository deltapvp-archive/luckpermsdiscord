package net.deltapvp.luckpermsdiscord.events;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.log.LogBroadcastEvent;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;

import java.awt.*;

public class LogListener {

    private WebhookClient client;
    private LuckPerms luckPerms;

    public LogListener(WebhookClient client, LuckPerms luckPerms) {
        this.client = client;
        this.luckPerms = luckPerms;
    }

    public void register() {
        this.luckPerms.getEventBus().subscribe(LogBroadcastEvent.class, this::onLog);
    }

    public void onLog(LogBroadcastEvent event) {
        WebhookEmbedBuilder d = new WebhookEmbedBuilder();
        d.setColor(Color.GREEN.hashCode());
        d.setTitle(new WebhookEmbed.EmbedTitle("Ingame Logs.", null));
        d.addField(new WebhookEmbed.EmbedField(false, "Executor", event.getEntry().getSource().getName()));
        d.addField(new WebhookEmbed.EmbedField(false, "Server", event.getOrigin().name()));
        d.addField(new WebhookEmbed.EmbedField(false, "Target", event.getEntry().getTarget().getName()));
        d.addField(new WebhookEmbed.EmbedField(false, "Description", event.getEntry().getDescription()));
        d.addField(new WebhookEmbed.EmbedField(false, "Timestamp", event.getEntry().getTimestamp() + ""));
        client.send(d.build());
        d.reset();
    }
}
