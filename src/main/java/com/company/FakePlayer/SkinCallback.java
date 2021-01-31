package com.company.FakePlayer;

import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public interface SkinCallback {

    void done(Skin skin) throws IOException, InvalidConfigurationException;

    default void waiting(long delay) {
    }

    default void uploading() {
    }

    default void error(String errorMessage) {
    }

    default void exception(Exception exception) {
    }

    default void parseException(Exception exception, String body) {
    }

}
