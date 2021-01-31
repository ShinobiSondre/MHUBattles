package com.company.util;

import com.company.MHUBattles;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.mojang.authlib.GameProfile;
import com.nisovin.magicspells.Spell;
import com.nisovin.magicspells.Spellbook;
import com.nisovin.magicspells.events.SpellCastEvent;
import com.nisovin.magicspells.events.SpellCastedEvent;
import com.nisovin.magicspells.spelleffects.EffectPosition;
import com.nisovin.magicspells.spells.TargetedMultiSpell;
import com.nisovin.magicspells.spells.TargetedSpell;
import com.nisovin.magicspells.util.CastItem;
import com.nisovin.magicspells.util.SpellReagents;
import com.nisovin.magicspells.util.compat.EventUtil;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.CitizensAPI;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter.adapt;
import static org.inventivetalent.glow.reflection.minecraft.DataWatcher.V1_9.setValue;


public class MagicSpells {

    public HashMap<Player, String> previousspells = new HashMap<>();
    public HashMap<Player, Player> players = new HashMap<>();
    MHUBattles mhu;
DenizenAPI denizenAPI;




    public HashMap<Integer,String> Quirkgroups(){


        HashMap<Integer,String> quirkgroups = new HashMap<>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("plugins/MagicSpells/spells-command.yml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (line != null) {
            //dataParts = line.split("-" + " "); // since your delimiter is "|"
            if (line.contains("- ") && !line.contains("- %")){
                int i = 0;
                String[] list = line.split("-");
                quirkgroups.put(i,list[1]);
                i++;
            }
            // read next line
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        return quirkgroups;}



        public void forcecancelSpell(Player player){

            player.setFlying(true);
            player.setFlying(false);


    }

    int count = 0;

    HashMap<UUID,Player> npcs = new HashMap<UUID, Player>();





    public void bindQuirk(Player target, String[] args) throws ReflectiveOperationException {

        Spellbook targetbook = com.nisovin.magicspells.MagicSpells.getSpellbook(target);

        targetbook.getSpells();

        List<Spell> spellsInGroup = (List<Spell>) com.nisovin.magicspells.MagicSpells.spells().stream().filter(s -> s.getSpellGroups().contains(targetbook.getSpellBookGroup())).collect(Collectors.toList());

        Spell spell1 = spellsInGroup.get(2);

        target.sendMessage("Spell4\n" + spell1.getName());

        UUID meh = UUID.fromString("fd689196-d6ee-42b0-9500-960618a7bcaa");

if(count==0) {
    MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
    WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
    EntityPlayer npc = new EntityPlayer(server, world, new GameProfile(target.getUniqueId(), "REEE"), new PlayerInteractManager(world));


    npc.setInvulnerable(false);
    Location loc = target.getLocation();

    npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata();

    spell1.castSpell((npc.getBukkitEntity()), Spell.SpellCastState.NORMAL, 1.0F, com.nisovin.magicspells.MagicSpells.NULL_ARGS);


    for(Player target1 : Bukkit.getOnlinePlayers()){
    PlayerConnection connection = ((CraftPlayer) target1).getHandle().playerConnection;
    connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
    connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));}


    npc.getBukkitEntity().setOp(true);
    //Bukkit.dispatchCommand(npc.getBukkitEntity(), "c forcecast " + target.getName() + " Explosion-Quirk-AE-Blast_Rush_Turbo-Leap-Right");
    Bukkit.dispatchCommand(npc.getBukkitEntity(), "cast Explosion-Quirk-AE-Blast_Rush_Turbo-Leap-Right");


    SpellCastEvent event = new SpellCastEvent(spell1, target, Spell.SpellCastState.NORMAL, 1.0F, args, 0, new SpellReagents(), 0);

    EventUtil.call(event);

    npcs.put(target.getUniqueId(), npc.getBukkitEntity());
}
        target.sendMessage(count+"oofa1");


        //count++;
        spell1.castSpell(npcs.get(target.getUniqueId()), Spell.SpellCastState.NORMAL, 1.0F, com.nisovin.magicspells.MagicSpells.NULL_ARGS);
            target.sendMessage("Name: " + npcs.get(target.getUniqueId()).getName());

            npcs.get(target.getUniqueId()).teleport(target.getLocation());






        /*for(Spell spell : spellsInGroup){

            ItemStack item = target.getInventory().getItemInMainHand();

            spell.canUseFakeItem(item);


            Bukkit.dispatchCommand(target,"c bind " + spell.getName());
            Bukkit.dispatchCommand(target,"c bind " + spell.getInternalName());

            //spell.castFromConsole(Bukkit.getConsoleSender(),args);


        }*/

    }


    public String getQuirkGroup(Player target){

        Spellbook targetbook = com.nisovin.magicspells.MagicSpells.getSpellbook(target);

        targetbook.getSpellBookGroup();

        String group = targetbook.getSpellBookGroup();

        return group;}



    void WriteToFile (String quirkgroup, String playername){
        try {


            File file = new File("plugins/PreviousQuirkGroups/" + playername + ".txt");

            FileWriter myWriter = new FileWriter(file,false);
            myWriter.write(quirkgroup);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }



    public String GetPreviousQuirkGroup(Player sender){

        try {
            File myObj = new File("plugins/PreviousQuirkGroups/" + sender.getName() + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                return data;
            }
            myReader.close();
        } catch (
                FileNotFoundException e) {
            System.out.println("QuirkGroup file is not found");
            e.printStackTrace();
        }
    return null;}



    public void WriteSpellGroup(Player sender){

        String group = com.nisovin.magicspells.MagicSpells.getSpellbook(sender).getSpellBookGroup();



        WriteToFile(group,sender.getName());



    }



    public void giveSpells(Player sender, String spellgroup, String text, ChatColor c) {

        Spellbook s1 = com.nisovin.magicspells.MagicSpells.getSpellbook(sender);

        WriteSpellGroup(sender);


        players.put(sender,sender);


        s1.removeAllSpells();
        s1.addGrantedSpells();

        List<Spell> spellsInGroup = (List<Spell>) com.nisovin.magicspells.MagicSpells.spells().stream().filter(s -> s.getSpellGroups().contains(spellgroup)).collect(Collectors.toList());

        if (spellsInGroup.size() != 0) {


            spellsInGroup.forEach(spell -> {


                s1.addSpell(spell);

            });
            s1.setSpellBookGroup(spellgroup);
            s1.save();


            sender.sendMessage(c + text);


        }


    }


    public void returnSpells(Player sender, String message) {


        System.out.print("ReturnSpells activated");



if(players.get(sender)!=null) {

    if (GetPreviousQuirkGroup(sender) != null || GetPreviousQuirkGroup(sender) != "") {
        Spellbook s2 = com.nisovin.magicspells.MagicSpells.getSpellbook((Player) sender);

        players.remove(sender);

        s2.removeAllSpells();
        s2.addGrantedSpells();

        List<Spell> spellsInGroup = (List<Spell>) com.nisovin.magicspells.MagicSpells.spells().stream().filter(s -> s.getSpellGroups().contains(GetPreviousQuirkGroup(sender))).collect(Collectors.toList());

        if (spellsInGroup.size() != 0) {


            spellsInGroup.forEach(spell -> {


                s2.addSpell(spell);

            });
            s2.setSpellBookGroup(GetPreviousQuirkGroup(sender));
            s2.save();

            sender.sendMessage(message);


        }
    }

}
    }

}
