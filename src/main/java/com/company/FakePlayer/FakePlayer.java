package com.company.FakePlayer;


import com.company.util.Util;
import com.company.util.WrapperPlayServerNamedEntitySpawn;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.Spell;
import com.nisovin.magicspells.events.SpellCastEvent;
import com.nisovin.magicspells.util.SpellReagents;
import com.nisovin.magicspells.util.compat.EventUtil;
import com.sun.org.apache.xml.internal.serialize.IndentPrinter;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.compatibility.LibsDisguisesSupport;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.LibsDisguises;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.utilities.DisguiseUtilities;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.slf4j.event.Level;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import sun.util.logging.PlatformLogger;

import javax.xml.crypto.Data;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static com.company.FakePlayer.Visibility.*;
import static io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter.adapt;


public class FakePlayer {

    public MythicMobs mm = (MythicMobs) Bukkit.getServer().getPluginManager().getPlugin("MythicMobs");

    //com.nisovin.magicspells.MagicSpells ms = (com.nisovin.magicspells.MagicSpells) Bukkit.getServer().getPluginManager().getPlugin("MagicSpells");//ADD


            /**
             * Spawns the fake player
             */

            Util util = new Util();

            HashMap<String, String> skindata = new HashMap<>();







            public void generateSkin(Player sender, String mobname, String prefix, String url){

                MineskinClient skinClient;

                Model model = Model.DEFAULT;

                skinClient = new MineskinClient();

                skinClient.generateUrl(url, SkinOptions.create("",
                        model,
                       PRIVATE), skin -> {

                    String value = skin.data.texture.value;
                    String signature = skin.data.texture.signature;

                    Bukkit.getLogger().info(signature);

                    System.out.println(signature);


                    sender.sendMessage("§aSkin data generated.");

                    File BossSkin = new File("plugins/LibsDisguises/disguises.yml");
                    File Disguises = new File("plugins/BossSkin/config.yml");

                    FileWriter yaml = new FileWriter(Disguises,true);
                    BufferedWriter out = new BufferedWriter(yaml);
                    out.write("YAML Test:\n");

                    File configFile = new File("plugins/BossSkin/config.yml");

                    FileConfiguration fc = YamlConfiguration.loadConfiguration(configFile);

                    List<String> list = fc.getStringList("Boss");
                    list.add(mobname);
                    fc.set("Boss", list);
                    fc.save(configFile);

                    String data = "player "+ prefix +" setSkin {\"id\":\"a149f81bf7844f8987c554afdd4db533\",\"name\":\"Naruto\",\"properties\":[{\"signature\":\"" + signature +  "\",\"name\":\"textures\",\"value\":\"" + value + "\"}]}";
                    
                   File configFile1 = new File("plugins/LibsDisguises/disguises.yml");
                    FileConfiguration fc1 = YamlConfiguration.loadConfiguration(configFile1);
                    fc1.set(((char)32 + mobname), data);
                    fc1.save(configFile1);
                    char space = (char)32;







                });
    }







            public void spawnFakePlayer(Player player, int i, int number, String[] args) throws IOException {

                List<Spell> spellsInGroup = (List<Spell>) com.nisovin.magicspells.MagicSpells.spells().stream().filter(s -> s.getSpellGroups().contains(com.nisovin.magicspells.MagicSpells.getSpellbook(player).getSpellBookGroup())).collect(Collectors.toList());


                Spell spell1 = spellsInGroup.get(number);

                player.sendMessage("Spell4\n" + spell1.getName());


                MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
                WorldServer worldServer = ((CraftWorld) player.getWorld()).getHandle();

                GameProfile gameProfile = new GameProfile(UUID.fromString("05849d38-023f-4268-b921-dc705f7f4412"), "Colinus999");
                EntityPlayer entityPlayer = new EntityPlayer(minecraftServer, worldServer, gameProfile, new PlayerInteractManager(worldServer));






                Random ra = new Random();
                entityPlayer.setNoGravity(false);

                //Spawn the EntityPlayer and set the Location of the EntityPlayer

                entityPlayer.playerConnection = new PlayerConnection(minecraftServer, new NetworkManager(EnumProtocolDirection.CLIENTBOUND), entityPlayer);
                int entityID = entityPlayer.getId();
                entityPlayer.setHealth(1f);
                worldServer.addEntity(entityPlayer);
                entityPlayer.setLocation(player.getLocation().getX() + 2, player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(),player.getLocation().getPitch());
                entityPlayer.setPosition(player.getLocation().getX() + 2, player.getLocation().getY(), player.getLocation().getZ());


                DataWatcher watcher = entityPlayer.getDataWatcher();  //ADD

                //watcher.register(new DataWatcherObject<>(254,DataWatcherRegistry.a),(byte)(int)Math.random());
                watcher.register(new DataWatcherObject<>(254, DataWatcherRegistry.a), (byte) 0);
                watcher.register(new DataWatcherObject<>(253, DataWatcherRegistry.b), 300);
                watcher.register(new DataWatcherObject<>(252, DataWatcherRegistry.d), "");
                watcher.register(new DataWatcherObject<>(251, DataWatcherRegistry.h),false);
                watcher.register(new DataWatcherObject<>(250, DataWatcherRegistry.h), false);
                watcher.register(new DataWatcherObject<>(249, DataWatcherRegistry.h), true);



                for(Player player1 : Bukkit.getOnlinePlayers()){
                PlayerConnection connection = ((CraftPlayer) player1).getHandle().playerConnection;
                //Send two Packets so the Server adds the EntityPlayer to the Server
                connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
                connection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
                connection.sendPacket(new PacketPlayOutEntityMetadata(entityPlayer.getId(), watcher, true));}


                SpellCastEvent event = new SpellCastEvent(spell1, entityPlayer.getBukkitEntity(), Spell.SpellCastState.NORMAL, 1.0F, args, 0, new SpellReagents(), 0);

                EventUtil.call(event);

                //spell1.castSpell((entityPlayer.getBukkitEntity()), Spell.SpellCastState.NORMAL, 1.0F, com.nisovin.magicspells.MagicSpells.NULL_ARGS);
                spell1.cast(entityPlayer.getBukkitEntity());

                MagicSpells.getSpellByInternalName("Rabbit_Quirk-Luna_Dexterity").cast(entityPlayer.getBukkitEntity());

                //MagicSpells.scheduleDelayedTask(() -> spell1.cast(entityPlayer.getBukkitEntity()), 0);



                MythicMob m = mm.getMobManager().getMythicMob("Denki");

                ActiveMob mob = m.spawn(adapt(player.getLocation()), 0);

                //GameProfile gameProfile = new GameProfile(UUID.fromString(mob.getUniqueId().toString()), "Denki");
                //gameProfile.getProperties().put("textures", new Property("textures", value, signature));

                mob.getEntity().getBukkitEntity().teleport(player.getLocation());



                //DisguiseAPI.disguiseToAll(adapt(mob.getEntity()), disguise);


                player.sendMessage("Did it " +  DisguiseAPI.isDisguised(adapt(mob.getEntity())));



        }






    public void spawnTake2(Player p, int i){


        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        WrapperPlayServerNamedEntitySpawn spawned = new WrapperPlayServerNamedEntitySpawn();


        Random ra = new Random();
        spawned.setEntityID(ra.nextInt(Integer.MAX_VALUE)+300);
        spawned.setPlayerName(""+i);
        spawned.setPlayerUUID(UUID.randomUUID().toString());

        spawned.setYaw(p.getLocation().getYaw());
        spawned.setPitch(p.getLocation().getPitch());

        // Documentation:
        // [url]http://mc.kev009.com/Entities#Entity_Metadata_Format[/url]
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        watcher.setObject(0, (byte) 0);
        watcher.setObject(1, (short) 300);
        watcher.setObject(8, (byte) 6);
        spawned.setMetadata(watcher);

        try {
            manager.sendServerPacket(p, spawned.getHandle());

            spawned.getEntity(p.getWorld()).teleport(p);



        } catch (InvocationTargetException e) {}
    }




}

