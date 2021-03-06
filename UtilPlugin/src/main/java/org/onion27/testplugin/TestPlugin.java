package org.onion27.testplugin;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@FunctionalInterface
interface InputHandler {
    void onInput(String message);
}

public final class TestPlugin extends JavaPlugin implements Listener {
    private final String[] OP_LIST = {"27Onion", "_AddMeInTopOfMc_", "SP_Sudierth"};
    private final String[] BUILDERS_LIST = {};
    private Map<String, InputHandler> inputBuffer = new HashMap<>();
    private final String[] ARG0_COMMANDS = {
            "version", "home", "gamemode", "gmo", "echo", "init", "tp", "tpo", "fly", "down",
            "setHealth", "me", "wolfys", "wallet", "say", "input"
    };
    private final String[] HOME_COMMANDS = {
            "set", "visit", "go"
    };
    private  final String[] WALLET_COMMANDS = {
            "check", "send"
    };
    private final File homesSaveFile = new File(getDataFolder(),"./.player_home_info");
    private final File walletSaveFile = new File(getDataFolder(), "./.player_wallet_info");
    private final YamlConfiguration homesConfig = new YamlConfiguration();
    private final YamlConfiguration walletConfig = new YamlConfiguration();
    public final String pluginVersion = "SNAPSHOT-21d169a";

    @Override
    public void onEnable() {
        System.out.println("Enabled Plugin Util!");
        System.out.println("\n================================================");
        getLogger().info("by 27Onion");
        getLogger().info("Version: " + pluginVersion);
        Bukkit.getPluginCommand("util").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
        File configFile = homesSaveFile;
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
            getLogger().info("Make Directory Done");
            getLogger().info("Wooh");
        }
        getLogger().info("Data folder at: " + getDataFolder().getAbsolutePath());
        if(!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                getLogger().info("Can't create the save file.");
            }
        } else {
            try {
                homesConfig.load(homesSaveFile);
                getLogger().info("Loading save: " + homesSaveFile.getAbsolutePath());
                getLogger().info("...");
            } catch (IOException e) {
                getLogger().info("Fuck you");
            } catch (InvalidConfigurationException e) {
                getLogger().info("And fuck you, too at line 72");
            }
            readConfig();
        }
        if(!walletSaveFile.exists()) {
            try {
                walletSaveFile.createNewFile();
            } catch(IOException e) {
                getLogger().info("Can't create the wallet.");
            }
        } else {
            try {
                walletConfig.load(walletSaveFile);
            } catch (IOException e) {
                getLogger().info("Fuck Sudierth");
            } catch (InvalidConfigurationException e) {
                getLogger().info("Fuck 27Onion");
            }
        }
        getLogger().info("Util: Config file at" + configFile.getAbsolutePath());
        getLogger().info("Util: Config folder at" + getDataFolder().getAbsolutePath());
        getLogger().info("\n=====================END======================");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        writeConfig();
    }

    private void writeConfig() {
        try {
            homesConfig.save(homesSaveFile);
            walletConfig.save(walletSaveFile);
        } catch (IOException e) {
            getLogger().info("Never gonna give you up");
        }
    }
    private void readConfig() {
        // Nothing to do, I guess
    }

    private boolean isInArray(Object[] o, Object target) {
        for(int i = 0; i < o.length; i++) {
            if(o[i].equals(target)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e){
        e.getPlayer().sendRawMessage("Welcome to aVisaSC, " + e.getPlayer().getDisplayName() + " !");
        String tip = "You are in the (";
        tip += e.getPlayer().getLocation().getX();
        tip += ", ";
        tip += e.getPlayer().getLocation().getY();
        tip += ", ";
        tip += e.getPlayer().getLocation().getZ();
        tip += ") now.";
        e.getPlayer().sendRawMessage(tip);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(command.getName().equalsIgnoreCase("util")) {
                try {
                    switch (args[0]) {
                        case "version":
                            player.sendRawMessage("Current version is: " + pluginVersion);
                            break;
                        case "init":
                            if(isInArray(OP_LIST, player.getDisplayName())) {
                                player.chat("DEBUG:27Onion Logged In");
                            } else {
                                player.sendRawMessage("FUCK! YOU CANNOT EXECUTE THIS!");
                            }
                            break;
                        case "echo":
                            if(isInArray( OP_LIST, player.getDisplayName())) {
                                player.sendRawMessage("DEBUG:" + args[1]);
                            } else {
                                player.sendRawMessage("FUCK! WHAT DO YOU WANT TO DO!");
                            }
                            break;
                        case "tp":
                            if(isInArray(OP_LIST, player.getDisplayName())) {
                                Location location = new Location(player.getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                                player.sendRawMessage("DEBUG:Teleporting...");
                                player.teleport(location);
                            } else {
                                player.sendRawMessage("SHIT! You aren't in the OP_LIST.");
                            }
                            break;
                        case "setHealth":
                            if(isInArray(OP_LIST, player.getDisplayName())) {
//                                player.setAllowFlight(true);
                                player.setHealth(Double.parseDouble(args[1]));
                            } else {
                                player.sendRawMessage("FUCK YOU!");
                            }
                            break;
                        case "fly":
                            if(isInArray(OP_LIST, player.getDisplayName()) || isInArray(BUILDERS_LIST, player.getDisplayName())) {
                                player.sendRawMessage("Fly on.");
                                player.setAllowFlight(true);
                            } else {
                                player.sendRawMessage("FUCK. DO YOU THINK YOU ARE A BAT!?");
                            }
                            break;
                        case "down":
                            if(isInArray(OP_LIST, player.getDisplayName()) || isInArray(BUILDERS_LIST, player.getDisplayName())) {
                                player.sendRawMessage("Fly off.");
                                player.setAllowFlight(false);
                            } else {
                                player.sendRawMessage("NMSL");
                            }
                            break;
                        case "me":
                            player.sendRawMessage("You are " + player.getDisplayName());
                            break;
                        case "gamemode":
                            if(isInArray(OP_LIST, player.getDisplayName())) {
                                switch (args[1]) {
                                    case "1":
                                        player.setGameMode(GameMode.CREATIVE);
                                        break;
                                    case "2":
                                        player.setGameMode(GameMode.ADVENTURE);
                                        break;
                                    case "3":
                                        player.setGameMode(GameMode.SPECTATOR);
                                        break;
                                    case "0":
                                        player.setGameMode(GameMode.SURVIVAL);
                                        break;
                                }
                            } else {
                                player.sendRawMessage("FUCK yOU>");
                            }
                            break;
                        case "say":
                            if(isInArray(OP_LIST, player.getDisplayName())) {
                                String msg = "";
                                for(int i = 1; i < args.length; i++) {
                                    msg += args[i] + " ";
                                }
                                getServer().broadcastMessage(msg);
                            } else {
                                player.sendRawMessage("FUCK FUCK FUCK");
                            }
                            break;
                        case "tpo":
                            if(isInArray(OP_LIST, player.getDisplayName())) {
                                Player another = Bukkit.getPlayer(args[1]);
                                another.sendRawMessage(player.getDisplayName() + " is teleporting you!");
                                // Ignore the null pointer exception because anyway we'll catch it.
                                Location loc = new Location(player.getWorld(), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));
                                another.teleport(loc);
                            } else {
                                player.sendRawMessage("FUCK YOU OH SHIT DO YOU THINK YOU CAN CONTROL ANOTHER PLAYER!?");
                            }
                            break;
                        case "wolfys":
                            // For wolfys
                            getServer().broadcastMessage("Wolfy Nebell");
                            getServer().broadcastMessage("12.27.2007 - 03.05.2021");
                            getServer().broadcastMessage("27Onion's Best Friend");
                            getServer().broadcastMessage("Slept forever");
                            break;
                        case "gmo":
                            Player another = Bukkit.getPlayer(args[1]);
                            switch(args[2]) {
                                case "0":
                                    another.setGameMode(GameMode.SURVIVAL);
                                    break;
                                case "1":
                                    another.setGameMode(GameMode.CREATIVE);
                                    break;
                                case "2":
                                    another.setGameMode(GameMode.ADVENTURE);
                                    break;
                                case "3":
                                    another.setGameMode(GameMode.SPECTATOR);
                                    break;
                            }
                            break;
                        case "home":
                            switch (args[1]) {
                                case "set":
                                    homesConfig.set(player.getDisplayName(), player.getLocation());
                                    writeConfig();
                                    player.sendRawMessage("bingo");
                                    break;
                                case "go":
                                    Location loc = (Location) homesConfig.get(player.getDisplayName());
                                    if(loc != null) {
                                        player.setGameMode(GameMode.SURVIVAL);
                                        player.teleport(loc);
                                        player.sendRawMessage("Wooh");
                                    } else {
                                        player.sendRawMessage("Never gonna give you up~");
                                    }
                                    break;
                                case "visit":
                                    if(homesConfig.get(player.getDisplayName()) == null) {
                                        player.sendRawMessage("Fuck! You don't have a home yet! Fuckyou FUckyou FUCKYOU!");
                                        break;
                                    }
                                    Location location = (Location) homesConfig.get(args[2]);
                                    if(location != null) {
                                        player.setGameMode(GameMode.SPECTATOR);
                                        player.teleport(location);
                                        player.sendRawMessage("[ " + ChatColor.GRAY + "???????????????????????????" + ChatColor.WHITE + " ] <27Onion> Teleported!!!");
                                    } else {
                                        player.sendRawMessage("Fuck! We can't find " + args[2] + " !");
                                    }
                                    break;
                                default:
                                    player.sendRawMessage("FUCK! DO YOU KNOW WHAT ARE YOU DOING NOW!!?");
                                    break;
                            }
                            break;
                        case "input":
                            String msg = "";
                            for(int i = 1; i < args.length; i++) {
                                msg += args[i] + " ";
                            }
                            String name = player.getDisplayName();
                            if(inputBuffer.containsKey(name)) {
                                inputBuffer.get(name).onInput(msg);
                            }
                            break;
                        case "wallet":
                            switch (args[1]) {
                                case "check":
                                    if(walletConfig.contains(player.getDisplayName())) {
                                        player.sendRawMessage(ChatColor.GREEN + player.getDisplayName() + "'s Wallet Info");
                                        player.sendRawMessage(ChatColor.RESET + "You have " + ChatColor.RED + walletConfig.get(player.getDisplayName()) + ChatColor.RESET + " G.");
                                    } else {
                                        player.sendRawMessage("You don't have a wallet yet. We will help you to create one and put 20G in it.");
                                        walletConfig.set(player.getDisplayName(), 20);
                                        walletConfig.save(walletSaveFile);
                                    }
                                    break;
                                case "send":
                                    Player playerToSend = Bukkit.getPlayer(args[1]);
                                    if(playerToSend != null && walletConfig.contains(player.getDisplayName()) && walletConfig.contains(playerToSend.getDisplayName())) {
                                        Integer toSend = Integer.parseInt(args[2]);
                                        Integer yourMoney = (Integer)walletConfig.get(player.getDisplayName());
                                        Integer theirMoney = (Integer)walletConfig.get(playerToSend.getDisplayName());
                                        if(yourMoney - toSend >= 0) {
                                            yourMoney -= toSend;
                                            theirMoney += toSend;
                                            walletConfig.set(player.getDisplayName(), yourMoney);
                                            walletConfig.set(playerToSend.getDisplayName(), theirMoney);
                                            playerToSend.sendRawMessage(ChatColor.GREEN + "Hey guy! " + player.getDisplayName() + " gave you " + toSend + "G. What a kind person!");
                                        } else {
                                            player.sendRawMessage(ChatColor.RED + "You don't have enough money yet, poor man." + ChatColor.RESET);
                                        }
                                    } else {
                                        player.sendRawMessage(ChatColor.RED + "An error occurred.");
                                        player.sendRawMessage(ChatColor.RED + "Please check if you input a number, or the player to send don't have a wallet yet, or you don't have a wallet.");
                                        player.sendRawMessage(ChatColor.RED + "Please run " + ChatColor.YELLOW + "/util " + ChatColor.BLUE + " wallet check" + ChatColor.RED + "  to check." + ChatColor.RESET);
                                    }
                            }
                            break;
                        default:
                            player.sendRawMessage("FUCK! WHAT ARE YOU TYPING! WE CAN'T FIND " + args[0] + " !");
                            break;
                    }
                } catch(Exception e) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 0) return Arrays.asList(ARG0_COMMANDS);
        if(args[0].equals("home")) {
            if(args.length == 1) {
                return Arrays.asList(HOME_COMMANDS);
            } else if(args.length == 2) {
                return Arrays.stream(HOME_COMMANDS)
                        .filter(s -> s.startsWith(args[1]))
                        .collect(Collectors.toList());
            }
        }
        if(args[0].equals("wallet")) {
            if(args.length == 1) {
                return Arrays.asList(WALLET_COMMANDS);
            } else if(args.length == 2) {
                return Arrays.stream(HOME_COMMANDS)
                        .filter(s -> s.startsWith(args[1]))
                        .collect(Collectors.toList());
            }
        }
        if(args.length == 1)  return Arrays.stream(ARG0_COMMANDS)
                .filter(s -> s.startsWith(args[0]))
                .collect(Collectors.toList());
        return super.onTabComplete(sender, command, alias, args);
    }
}
