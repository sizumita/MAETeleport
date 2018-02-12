package mae.teleport;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;



import java.util.ArrayList;
import java.util.UUID;
public final class Teleport extends JavaPlugin implements Listener {
    CustomConfig config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        config = new CustomConfig(this);
        config.saveDefaultConfig();
        config.saveConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(cmd.getName().equalsIgnoreCase("maeteleport")){
            openGUI((Player) sender);
            return true;
        } else {
            return true;
        }
    }

    private  void  teleportInWorld(Player player,int x,int y,int z){
        player.teleport(new Location(player.getWorld(), x, y, z));
    }

    private void openGUI ( Player player){
        Inventory i = Bukkit.createInventory(null, 9, ChatColor.DARK_BLUE + "teleport_menu");

        ItemStack spawn = new ItemStack(Material.BED);
        ItemMeta spawnMeta = spawn.getItemMeta();

        ItemStack temple = new ItemStack(Material.ENDER_PORTAL_FRAME);
        ItemMeta templeMeta = temple.getItemMeta();

        ItemStack shop = new ItemStack(Material.GOLD_INGOT);
        ItemMeta shopMeta = shop.getItemMeta();

        spawnMeta.setDisplayName(ChatColor.AQUA + "spawn");
        spawn.setItemMeta(spawnMeta);

        templeMeta.setDisplayName(ChatColor.AQUA + "temple");
        temple.setItemMeta(templeMeta);

        shopMeta.setDisplayName(ChatColor.AQUA + "bank");
        shop.setItemMeta(shopMeta);

        i.setItem(2, spawn);
        i.setItem(5, temple);
        i.setItem(7, shop);

        player.openInventory(i);



    }


    @EventHandler
    public void  onInventoryClickEvent(InventoryClickEvent e) {
        if(!ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase("teleport_menu"))
        return ;
        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);

        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || !e.getCurrentItem().hasItemMeta()){
            player.closeInventory();
            return;
        }
        FileConfiguration conf = config.getConfig();
        switch (e.getCurrentItem().getType()){
            case BED:
                teleportInWorld(player,conf.getInt("spawn_x"),conf.getInt("spawn_y"),conf.getInt("spawn_z"));
                player.closeInventory();player.sendMessage(String.format("%s You have been teleported to %spawn%s!", ChatColor.GREEN, ChatColor.AQUA, ChatColor.GREEN));
                break;
            case ENDER_PORTAL_FRAME:
                teleportInWorld(player,conf.getInt("temple_x"),conf.getInt("temple_y"),conf.getInt("temple_z"));
                player.closeInventory();player.sendMessage(String.format("%s You have been teleported to %temple%s!", ChatColor.GREEN, ChatColor.AQUA, ChatColor.GREEN));
                break;
            case GOLD_INGOT:
                teleportInWorld(player,conf.getInt("bank_x"),conf.getInt("bank_y"),conf.getInt("bank_z"));
                player.closeInventory();player.sendMessage(String.format("%s You have been teleported to %bank%s!", ChatColor.GREEN, ChatColor.AQUA, ChatColor.GREEN));
                break;
            default:
                player.closeInventory();
                break;

        }

    }





}
