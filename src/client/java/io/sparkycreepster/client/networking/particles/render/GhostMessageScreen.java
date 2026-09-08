package io.sparkycreepster.client.networking.particles.render;

import io.sparkycreepster.custom.networking.packets.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class GhostMessageScreen extends Screen {

    private TextFieldWidget textField;

    public GhostMessageScreen() {
        super(Text.literal("Ghost Message"));
    }

    @Override
    protected void init() {
        super.init();

        int width = 300;
        int height = 20;

        int x = (this.width - width) / 2;
        int y = this.height / 2 - 20;

        textField = new TextFieldWidget(
                this.textRenderer,
                x,
                y,
                width,
                height,
                Text.literal("Message")
        );

        textField.setMaxLength(256);

        this.addDrawableChild(textField);

        this.addDrawableChild(
                ButtonWidget.builder(
                        Text.literal("Send"),
                        button -> sendMessage()
                ).dimensions(
                        x + 100,
                        y + 30,
                        100,
                        20
                ).build()
        );

        this.setInitialFocus(textField);
    }

    private void sendMessage() {
        String message = textField.getText();

        if (message.isBlank()) {
            return;
        }

        var buf = PacketByteBufs.create();
        buf.writeString(message, 256);

        ClientPlayNetworking.send(
                Packets.GHOST_MESSAGE,
                buf
        );

        this.close();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if (keyCode == 257) { // Enter
            sendMessage();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(
            DrawContext context,
            int mouseX,
            int mouseY,
            float delta
    ) {
        this.renderBackground(context);

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Ghost Message"),
                this.width / 2,
                this.height / 2 - 50,
                0xFFFFFF
        );

        super.render(context, mouseX, mouseY, delta);
    }
}