package mae.teleport;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

/**
 * Created by sizumita on 2018/02/11.
 */
public class CustomConfig {
    private FileConfiguration config = null;
    private final File configFile;
    private final String file;
    private final Plugin plugin;

    public CustomConfig(Plugin plugin) {
        this(plugin, "config.yml");
    }

    public CustomConfig(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.file = fileName;
        configFile = new File(plugin.getDataFolder(), file);
    }

    /**
     * デフォルト設定を保存します。
     */
    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            plugin.saveResource(file, false);
        }
    }

    /**
     * 読み込んだFileConfiguretionを提供します。
     *
     * @return 読み込んだ設定
     */
    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    /**
     * 設定を保存します。
     */
    public void saveConfig() {
        if (config == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    /**
     * 設定をリロードします。
     */
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = plugin.getResource(file);
        if (defConfigStream == null) {
            return;
        }

        config.setDefaults(
                YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
    }
}

