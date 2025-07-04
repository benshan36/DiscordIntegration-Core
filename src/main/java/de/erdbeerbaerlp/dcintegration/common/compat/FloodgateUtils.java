package de.erdbeerbaerlp.dcintegration.common.compat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FloodgateUtils {
    /**
     * Checks for a player UUID being an floodgate UUID or not.
     * @param playerUUID
     * @return true if the player joined with floodgate, false otherwise. Also false if floodgate API is not available
     */
    public static boolean isBedrockPlayer(UUID playerUUID){
        try {
            final Class<?> aClass = Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            final Object getInstance = aClass.getDeclaredMethod("getInstance").invoke(null);
            final Method isBedrockPlayer = getInstance.getClass().getDeclaredMethod("isFloodgateId", UUID.class);
            final Object invoke = isBedrockPlayer.invoke(getInstance, playerUUID);
            return (boolean) invoke;
        } catch (RuntimeException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            return false;
        }
    }

    /**
     * Get username of a floodgate player uuid.
     * @param playerUUID
     * @return username of the player or "unknown" if something went wrong
     */

    public static String getUsername(UUID playerUUID) {
        try {
            final Class<?> aClass = Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            final Object getInstance = aClass.getDeclaredMethod("getInstance").invoke(null);
            final Method getPlayerPrefix = getInstance.getClass().getDeclaredMethod("getPlayerPrefix");
            final Method getGamertagFor = getInstance.getClass().getDeclaredMethod("getGamertagFor", long.class);
            return getPlayerPrefix.invoke(getInstance) + ((CompletableFuture<String>) getGamertagFor.invoke(getInstance, playerUUID.getLeastSignificantBits())).get();
        } catch (InterruptedException | ExecutionException | RuntimeException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            return "unknown";
		}

    }
}
