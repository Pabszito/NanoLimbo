/*
 * Copyright (C) 2020 Nan1t
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ua.nanit.limbo.server.data;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

public class InfoForwarding {

    private Type type;
    private byte[] secretKey;
    private List<String> tokens;
    private String noDataForwardedMessage;
    private String invalidForwardedDataMessage;
    private String velocityRequiredMessage;
    private String keyIntegrityFailedMessage;

    public Type getType() {
        return type;
    }

    public byte[] getSecretKey() {
        return secretKey;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public boolean hasToken(String token) {
        return tokens != null && token != null && tokens.contains(token);
    }

    public boolean isNone() {
        return type == Type.NONE;
    }

    public boolean isLegacy() {
        return type == Type.LEGACY;
    }

    public boolean isModern() {
        return type == Type.MODERN;
    }

    public boolean isBungeeGuard() {
        return type == Type.BUNGEE_GUARD;
    }

    public String getNoDataForwardedMessage() {
        return noDataForwardedMessage;
    }

    public String getInvalidForwardedDataMessage() {
        return invalidForwardedDataMessage;
    }

    public String getVelocityRequiredMessage() {
        return velocityRequiredMessage;
    }

    public String getKeyIntegrityFailedMessage() {
        return keyIntegrityFailedMessage;
    }

    public enum Type {
        NONE,
        LEGACY,
        MODERN,
        BUNGEE_GUARD
    }

    public static class Serializer implements TypeSerializer<InfoForwarding> {

        @Override
        public InfoForwarding deserialize(java.lang.reflect.Type type, ConfigurationNode node) throws SerializationException {
            InfoForwarding forwarding = new InfoForwarding();

            try {
                forwarding.type = Type.valueOf(node.node("type").getString("").toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                throw new SerializationException("Undefined info forwarding type");
            }

            if (forwarding.type == Type.MODERN) {
                forwarding.secretKey = node.node("secret").getString("").getBytes(StandardCharsets.UTF_8);
            }

            if (forwarding.type == Type.BUNGEE_GUARD) {
                forwarding.tokens = node.node("tokens").getList(String.class);
            }

            // Get kick messages
            forwarding.noDataForwardedMessage = node.node("errors", "no-forwarding-data").getString();
            forwarding.invalidForwardedDataMessage = node.node("errors", "invalid-forwarded-data").getString();
            forwarding.keyIntegrityFailedMessage = node.node("errors", "key-integrity-failed").getString();
            forwarding.velocityRequiredMessage = node.node("errors", "velocity-required").getString();

            return forwarding;
        }

        @Override
        public void serialize(java.lang.reflect.Type type, @Nullable InfoForwarding obj, ConfigurationNode node) {

        }
    }
}
