package net.deltapvp.luckpermsdiscord.events;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.log.LogBroadcastEvent;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import java.awt.*;

public class NodeListeners {
    private WebhookClient client;
    private LuckPerms luckPerms;

    public NodeListeners(WebhookClient client, LuckPerms luckPerms) {
        this.client = client;
        this.luckPerms = luckPerms;
    }

    public void register() {
        this.luckPerms.getEventBus().subscribe(NodeAddEvent.class, this::onAdd);
        this.luckPerms.getEventBus().subscribe(NodeRemoveEvent.class, this::onRemove);
    }

    public void onAdd(NodeAddEvent event) {
        WebhookEmbedBuilder d = new WebhookEmbedBuilder();
        d.setColor(Color.GREEN.hashCode());
        d.setTitle(new WebhookEmbed.EmbedTitle("A Permission has been added.", null));
        d.addField(new WebhookEmbed.EmbedField(false, "Executor", "N/A"));
        d.addField(new WebhookEmbed.EmbedField(false, "Type", event.isUser() ? "User" : "Group"));
        d.addField(new WebhookEmbed.EmbedField(false, "Target", event.getTarget().getFriendlyName()));
        d.addField(new WebhookEmbed.EmbedField(false, "Node", event.getNode().getKey()));
        client.send(d.build());
        d.reset();
    }

    public void onRemove(NodeRemoveEvent event) {
        WebhookEmbedBuilder d = new WebhookEmbedBuilder();
        d.setColor(Color.RED.hashCode());
        d.setTitle(new WebhookEmbed.EmbedTitle("A Permission has been removed", null));
        d.addField(new WebhookEmbed.EmbedField(false, "Executor", "N/A"));
        d.addField(new WebhookEmbed.EmbedField(false, "Type", event.isUser() ? "User" : "Group"));
        d.addField(new WebhookEmbed.EmbedField(false, "Target", event.getTarget().getFriendlyName()));
        d.addField(new WebhookEmbed.EmbedField(false, "Node", event.getNode().getKey()));
        client.send(d.build());
        d.reset();
    }
}
