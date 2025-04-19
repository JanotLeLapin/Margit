package org.github.paperspigot;

import com.google.common.base.Throwables;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Level;

import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegionFile;
import org.apache.commons.lang3.StringUtils; // Margit
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class PaperSpigotConfig
{

    private static File CONFIG_FILE;
    private static final String HEADER = "This is the main configuration file for PaperSpigot.\n"
            + "As you can see, there's tons to configure. Some options may impact gameplay, so use\n"
            + "with caution, and make sure you know what each option does before configuring.\n"
            + "\n"
            + "If you need help with the configuration or have any questions related to PaperSpigot,\n"
            + "join us at the IRC.\n"
            + "\n"
            + "IRC: #paperspigot @ irc.spi.gt ( http://irc.spi.gt/iris/?channels=PaperSpigot )\n";
    /*========================================================================*/
    public static YamlConfiguration config;
    static int version;
    static Map<String, Command> commands;
    /*========================================================================*/

    public static void init(File configFile)
    {
        CONFIG_FILE = configFile;
        config = new YamlConfiguration();
        try
        {
            config.load ( CONFIG_FILE );
        } catch ( IOException ex )
        {
        } catch ( InvalidConfigurationException ex )
        {
            Bukkit.getLogger().log( Level.SEVERE, "Could not load paper.yml, please correct your syntax errors", ex );
            throw Throwables.propagate( ex );
        }
        config.options().header( HEADER );
        config.options().copyDefaults( true );

        commands = new HashMap<String, Command>();

        version = getInt( "config-version", 9 );
        set( "config-version", 9 );
        readConfig( PaperSpigotConfig.class, null );
    }

    public static void registerCommands()
    {
        for ( Map.Entry<String, Command> entry : commands.entrySet() )
        {
            MinecraftServer.getServer().server.getCommandMap().register( entry.getKey(), "PaperSpigot", entry.getValue() );
        }
    }

    static void readConfig(Class<?> clazz, Object instance)
    {
        for ( Method method : clazz.getDeclaredMethods() )
        {
            if ( Modifier.isPrivate( method.getModifiers() ) )
            {
                if ( method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE )
                {
                    try
                    {
                        method.setAccessible( true );
                        method.invoke( instance );
                    } catch ( InvocationTargetException ex )
                    {
                        throw Throwables.propagate( ex.getCause() );
                    } catch ( Exception ex )
                    {
                        Bukkit.getLogger().log( Level.SEVERE, "Error invoking " + method, ex );
                    }
                }
            }
        }

        try
        {
            config.save( CONFIG_FILE );
        } catch ( IOException ex )
        {
            Bukkit.getLogger().log( Level.SEVERE, "Could not save " + CONFIG_FILE, ex );
        }
    }

    private static void set(String path, Object val)
    {
        config.set( path, val );
    }

    private static boolean getBoolean(String path, boolean def)
    {
        config.addDefault( path, def );
        return config.getBoolean( path, config.getBoolean( path ) );
    }

    private static double getDouble(String path, double def)
    {
        config.addDefault( path, def );
        return config.getDouble( path, config.getDouble( path ) );
    }

    private static float getFloat(String path, float def)
    {
        // TODO: Figure out why getFloat() always returns the default value.
        return (float) getDouble( path, (double) def );
    }

    private static int getInt(String path, int def)
    {
        config.addDefault( path, def );
        return config.getInt( path, config.getInt( path ) );
    }

    private static <T> List getList(String path, T def)
    {
        config.addDefault( path, def );
        return (List<T>) config.getList( path, config.getList( path ) );
    }

    private static String getString(String path, String def)
    {
        config.addDefault( path, def );
        return config.getString( path, config.getString( path ) );
    }

    public static double babyZombieMovementSpeed;
    private static void babyZombieMovementSpeed()
    {
        babyZombieMovementSpeed = getDouble( "settings.baby-zombie-movement-speed", 0.5D ); // Player moves at 0.1F, for reference
    }

    public static boolean interactLimitEnabled;
    private static void interactLimitEnabled()
    {
        interactLimitEnabled = getBoolean( "settings.limit-player-interactions", true );
        if ( !interactLimitEnabled )
        {
            Bukkit.getLogger().log( Level.INFO, "Disabling player interaction limiter, your server may be more vulnerable to malicious users" );
        }
    }

    public static double strengthEffectModifier;
    public static double weaknessEffectModifier;
    private static void effectModifiers()
    {
        strengthEffectModifier = getDouble( "effect-modifiers.strength", 1.3D );
        weaknessEffectModifier = getDouble( "effect-modifiers.weakness", -0.5D );
    }

    public static Set<Integer> dataValueAllowedItems;
    private static void dataValueAllowedItems()
    {
        dataValueAllowedItems = new HashSet<Integer>( getList( "data-value-allowed-items", Collections.emptyList() ) );
        Bukkit.getLogger().info( "Data value allowed items: " + StringUtils.join(dataValueAllowedItems, ", ") );
    }

    public static boolean stackableLavaBuckets;
    public static boolean stackableWaterBuckets;
    public static boolean stackableMilkBuckets;
    private static void stackableBuckets()
    {
        stackableLavaBuckets = getBoolean( "stackable-buckets.lava", false );
        stackableWaterBuckets = getBoolean( "stackable-buckets.water", false );
        stackableMilkBuckets = getBoolean( "stackable-buckets.milk", false );

        try {
            if (stackableLavaBuckets) {
                // KigPaper - don't update Bukkit enum, broken in Java 17
                //maxStack.set(Material.LAVA_BUCKET, Material.BUCKET.getMaxStackSize());
                Items.LAVA_BUCKET.c(Material.BUCKET.getMaxStackSize());
            }

            if (stackableWaterBuckets) {
                //maxStack.set(Material.WATER_BUCKET, Material.BUCKET.getMaxStackSize());
                Items.WATER_BUCKET.c(Material.BUCKET.getMaxStackSize());
            }

            if (stackableMilkBuckets) {
                //maxStack.set(Material.MILK_BUCKET, Material.BUCKET.getMaxStackSize());
                Items.MILK_BUCKET.c(Material.BUCKET.getMaxStackSize());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean warnForExcessiveVelocity;
    private static void excessiveVelocityWarning()
    {
        warnForExcessiveVelocity = getBoolean("warnWhenSettingExcessiveVelocity", true);
    }
    // KigPaper start
    public static float knockbackHorizontalMultiplier, knockbackVerticalMultiplier, knockbackHorizontalSprinting, knockbackVerticalSprinting, knockbackFriction;
    public static boolean betterVehicleHitboxes, nettyReadTimeout, forceConditionalAutoFlush, savePlayerFiles, enableBookDeserialization,
            accurateMoveTiming, accurateBlockCollisions, bedMessageActions, kickChatMessageLength, entityExplosionConsistency,
            kickTabCompleteSpam;
    public static RegionFile.CompressionAlgorithm regionCompressionAlgorithm;

    private static void knockbackHorizontalMultiplier() {
        knockbackHorizontalMultiplier = (float) getDouble("knockback.horizontal.normal", 1f);
    }
    private static void knockbackVerticalMultiplier() {
        knockbackVerticalMultiplier = (float) getDouble("knockback.vertical.normal", 1f);
    }
    private static void knockbackHorizontalSprinting() {
        knockbackHorizontalSprinting = (float) getDouble("knockback.horizontal.sprinting", 1f);
    }
    private static void knockbackVerticalSprinting() {
        knockbackVerticalSprinting = (float) getDouble("knockback.vertical.sprinting", 1f);
    }
    private static void knockbackFriction() {
        knockbackFriction = (float) getDouble("knockback.friction", 2f);
    }
    private static void betterVehicleHitboxes() { betterVehicleHitboxes = getBoolean("better-vehicle-hitboxes", true); }
    private static void nettyReadTimeout() { nettyReadTimeout = getBoolean("netty-read-timeout", true); }
    private static void conditionalAutoFlush() {
        forceConditionalAutoFlush = getBoolean("net-force-conditional-flush", true);
    }
    private static void savePlayerFiles() {
        savePlayerFiles = getBoolean("save-player-files", true);
    }
    private static void enableBookDeserialization() {
        enableBookDeserialization = getBoolean("enable-book-deserialization", false);
    }
    private static void accurateMoveTiming() {
        accurateMoveTiming = getBoolean("accurate-move-timing", false);
    }
    private static void accurateBlockCollisions() {
        accurateBlockCollisions = getBoolean("accurate-block-collisions", false);
    }
    private static void bedMessageActions() {
        bedMessageActions = getBoolean("bed-message-actions", false);
    }
    private static void regionCompressionAlgorithm() {
        regionCompressionAlgorithm = RegionFile.CompressionAlgorithm.valueOf(getString("region-compression-algo", "ZLIB").toUpperCase(Locale.ROOT));
    }
    private static void kickChatMessageLength() {
        kickChatMessageLength = getBoolean("kick-chat-message-length", false);
    }
    private static void entityExplosionConsistency() {
        entityExplosionConsistency = getBoolean("entity-explosion-consistency", true);
    }
    private static void kickTabCompleteSpam() {
        kickTabCompleteSpam = getBoolean("kick-tab-complete-spam", false);
    }
    // KigPaper end
}
