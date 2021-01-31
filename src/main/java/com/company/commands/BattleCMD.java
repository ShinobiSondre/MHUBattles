package com.company.commands;

import com.company.FakePlayer.FakePlayer;
import com.company.MHUBattles;
import com.company.util.*;
import com.earth2me.essentials.Essentials;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.util.Vector;

import java.io.*;
import java.util.*;

import static com.nisovin.magicspells.MagicSpells.getVariableManager;
import static io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter.adapt;
import static java.lang.Double.parseDouble;


public class BattleCMD implements CommandExecutor,Listener{


    public Essentials es = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

    private static final MHUBattles mhu;

    static {
        mhu = (MHUBattles) Bukkit.getServer().getPluginManager().getPlugin("MHUBattles");
    }

    public Util util1;

    public Util util2 = new Util();

    MagicSpells ms = new MagicSpells();
    ArrayList<String> arenas = new ArrayList();
    ArrayList<String> mobList = new ArrayList();
    ArrayList<String> friendList = new ArrayList();
    HashMap<String, String> takenArenas = new HashMap<>();

    public ArrayList<UUID> pathclearUUID = new ArrayList();

    public TitleManagerAPI titleManagerAPI = (TitleManagerAPI) Bukkit.getServer().getPluginManager().getPlugin("TitleManager");
    public MythicMobs mm = (MythicMobs) Bukkit.getServer().getPluginManager().getPlugin("MythicMobs");

    @Override
    public boolean onCommand(CommandSender sender1, Command cmd, String label, String[] args) {


        Player sender = Bukkit.getPlayer(sender1.getName());


         if (cmd.getName().equals("mmbattle")) {
             Player player = Bukkit.getPlayer(args[0]);
             Bukkit.dispatchCommand(player,"check");


             try {
                 //New Added Uiid remember to add it in the command in the mob
                 MobChallenge(player,args[1]);
             } catch (InterruptedException | FileNotFoundException e) {
                 e.printStackTrace();
             }


         }


         else if (cmd.getName().equals("mobskin")){



             FakePlayer fp = new FakePlayer();

            try {

                fp.generateSkin(sender, args[0], args[1], args[2]);

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ld reload");

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bs reload");
            } catch (CommandException e) {

                sender.sendMessage("§cUsage: /mobskin mobname mobprefix skinurl");
            }

            sender.sendMessage("§aSkin set!");

         }



         //MHU FAMILIY COMMANDS


         else if (cmd.getName().equals("surname")){



             Player p1 = (Player) sender1;

             String surname = args[0];

             //if(!checkFirstLoginElements(p1)){

             try{

             String name = es.getUser(p1.getPlayer())._getNickname().split(" ")[0];

             String gender = null;




                 gender = es.getUser(p1.getPlayer())._getNickname().split(" ")[2];

                 es.getUser(p1.getPlayer()).setNickname(ChatColor.translateAlternateColorCodes('&',(name + " " + surname + " " + gender)));
                 mhu.surnameCheck.add(p1.getUniqueId());

             p1.sendMessage("§a§lYour surname is now " + args[0]);}


             catch (Exception e){

                 try {

                     String name = es.getUser(p1.getPlayer())._getNickname().split(" ")[0];

                     es.getUser(p1.getPlayer()).setNickname(ChatColor.translateAlternateColorCodes('&', (name + " " + surname)));

                     mhu.surnameCheck.add(p1.getUniqueId());

                     p1.sendMessage("§a§lYour surname is now " + args[0]);

                 }catch (Exception e1){


                     es.getUser(p1.getPlayer()).setNickname(ChatColor.translateAlternateColorCodes('&', (p1.getName() + " " + surname)));

                     mhu.surnameCheck.add(p1.getUniqueId());

                     p1.sendMessage("This one ");

                     p1.sendMessage("§a§lYour surname is now " + args[0]);


                 }

             }

                 /*}

                 else
                     p1.sendMessage("§a§lYou already chose a surname.");*/


         }

         else if (cmd.getName().equals("male")){

             Player p1 = (Player) sender1;

             //TEST


             //END

             String name = es.getUser(p1.getPlayer())._getNickname().split(" ")[0];

             String surname = es.getUser(p1.getPlayer())._getNickname().split(" ")[1];

             es.getUser(p1.getPlayer()).setNickname(ChatColor.translateAlternateColorCodes('&',(name + " " + surname + " §f§l[§b§l♂§f§l]")));

             mhu.genderCheck.add(p1.getUniqueId());

             try {
                 writeFirstLoginElements(p1);
             } catch (IOException e) {
                 e.printStackTrace();
             }

         }

         else if (cmd.getName().equals("female")){

             Player p1 = (Player) sender1;

             String name = es.getUser(p1.getPlayer())._getNickname().split(" ")[0];

             String surname = es.getUser(p1.getPlayer())._getNickname().split(" ")[1];

             es.getUser(p1.getPlayer()).setNickname(ChatColor.translateAlternateColorCodes('&',(name + " " + surname + " §f§l[§d§l♀§f§l]")));

             mhu.genderCheck.add(p1.getUniqueId());

             try {
                 writeFirstLoginElements(p1);
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }

        else if (cmd.getName().equals("marry")) {


            Player p1 = (Player) sender1;

            Player p2 = Bukkit.getPlayer(args[0]);

             if(p1.equals(p2))
                 p1.sendMessage("§a§lYou can't marry yourself §d§lbaka§a§l...");

            else if(p2!=null) {


                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p2.getName() + " set marrige.proposal." + p1.getName().toLowerCase() + " true");

                titleText(p2, 0, "§a§l" + p1.getName(), "1", false);
                titleText(p2, 40, "§a§lWants to!!!", "3", false);
                titleText(p2, 70, "§d§lMARRY", "4", false);
                titleText(p2, 90, "§a§lyou!!", "5", false);

                Text(p2,120,"§a§lWrite §d§lYes§a§l to accept or §c§lNo §a§lto deny.." + "\n","6");

            }

        }

        else if(cmd.getName().equals("divorce")){


            Player p2 = Bukkit.getPlayer(args[0]);

            Player parent;

             String surnameP2 = es.getUser(p2.getPlayer())._getNickname().split(" ")[1];
             String surnameSender = es.getUser(sender)._getNickname().split(" ")[1];

             for(PermissionAttachmentInfo parentperm : p2.getEffectivePermissions()) {

                 if (parentperm.getPermission().contains("married.")) {

                     String parentname = parentperm.getPermission().split("married.")[1];
                     String filteredparentname = parentname.split("[.]")[0];
                     parent = Bukkit.getPlayer(filteredparentname);

                     Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p2.getName() + " unset married." + sender.getName() + "." + surnameSender);
                     Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p2.getName() + " unset married");

                     Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + sender.getName() + " unset married." + p2.getName() + "." + surnameP2);
                     Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + sender.getName() + " unset married");

                 }


             }


             sender.sendMessage("§c§lYou §4§ldivorced§c§l " + p2.getName());


         }


         else if (cmd.getName().equals("adopt")) {


             Player p1 = (Player) sender1;

             Player p2 = Bukkit.getPlayer(args[0]);

             if(p1.equals(p2))
                 p1.sendMessage("§a§lYou can't adopt yourself §d§lbaka§a§l...");

             else if(p2!=null) {

                 titleText(p2, 0, "§a§l" + p1.getName(), "1", false);
                 titleText(p2, 40, "§a§lWants to!!!", "3", false);
                 titleText(p2, 70, "§d§lADOPT", "4", false);
                 titleText(p2, 90, "§a§lyou!!", "5", false);

                 Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p2.getName() + " set adopt.proposal." + p1.getName().toLowerCase() + " true");

                 Text(p2,120,"§a§lWrite §d§lYes§a§l to accept or §c§lNo §a§lto deny.." + "\n","6");

             }

         }



         //BAN ROBBERY COMMAND




         else if(cmd.getName().equals("robstart")){

             for(Player player: Bukkit.getOnlinePlayers()) {

                 if (player.hasPermission("hero.group")) {

                     player.sendMessage("                                       ");
                     player.sendMessage("                                       ");
                     player.sendMessage(ChatColor.BLUE + "------------ " + ChatColor.DARK_BLUE + "Police Report" + ChatColor.BLUE + " ------------");
                     player.sendMessage("        " + ChatColor.DARK_RED + args[0] + ChatColor.WHITE + " is robbing a bank!");
                     player.sendMessage("        " + ChatColor.WHITE + "Do " + ChatColor.BLUE + "/mhufind " + ChatColor.BLUE + args[0]);
                     player.sendMessage("        " + ChatColor.WHITE + "To stop them!");
                     player.sendMessage(ChatColor.BLUE + "--------------- " + ChatColor.DARK_BLUE + "E.N.D" + ChatColor.BLUE + " ---------------");
                     player.sendMessage("                                       ");
                     player.sendMessage("                                       ");


                 }

             }

         }


         //END



         //MHU MS IMMUNE MOBS COMMAND


         else if(cmd.getName().equals("immune")){


             try {
                 ImmuneMythicMobs(args[0]);
             } catch (IOException e) {
                 e.printStackTrace();

                 sender.sendMessage("Failed!");

             }

             sender.sendMessage("§a§l"+ args[0] + " is now immune to any quirk!");


         }

         else if(cmd.getName().equals("pathtest")){

             Player player = sender;

             try {
                 MobWithPath(player);
             } catch (IOException e) {
                 e.printStackTrace();
             }


         }

         else if(cmd.getName().equals("updatemob")){

             Player player = sender;

             updateMob(player);


         }

         else if(cmd.getName().equals("mhu")){

             Player player = (Player) sender1;

             if(args[0].equals("help")) {

                 TextComponent[] plugins = {mhuHelp(player, "MHUBattles", "/help mhubattles"), mhuHelp(player, "MHUGTA", "/help mhugta"),
                         mhuHelp(player, "MHULevels", "/help mhulevels"), mhuHelp(player, "MHUAgencies", "/help mhuagencies"),
                         mhuHelp(player, "MHUAutomaticEvents", "/help mhuautomaticevents"), mhuHelp(player, "MHUParties", "/help mhuparties")};


                 player.sendMessage("§f§l[§4§lMHU§c-§f§lHelp§f§l]" + "\n" + " ");


                 for (TextComponent plugin : plugins) {


                     player.spigot().sendMessage(plugin);


                 }

                 player.sendMessage(" " + "\n" +  "§f§l[§4§lE.N.D§f§l]");


             }




         }

         else if(cmd.getName().equals("check")){

             Player player = (Player) sender1;
             try {
                 NearbyFriends(player);
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             }

         }

         else if(cmd.getName().equals("autobind")){


             /*try {
                 ms.bindQuirk(sender,args);
             } catch (ReflectiveOperationException e) {
                 e.printStackTrace();
             }*/

             mhu.random++;

             FakePlayer fp = new FakePlayer();

             try {
                 fp.spawnFakePlayer(sender, mhu.random, (int) parseDouble(args[0]),args);

             } catch (IOException e) {
                 e.printStackTrace();
             }


         }

         else if(cmd.getName().equals("setarena")){



             Player player = (Player) sender1;
             player.sendMessage("§f§l[§4§lMHU§c-§f§lBattles§f§l] §f§lArena registered");
             try {
                 setArena(args[0]);
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }


         else if(cmd.getName().equals("arenalist")){

                getArenas();

             Player player = (Player) sender1;
             player.sendMessage("§f§l[§4§lMHU§c-§f§lBattles§f§l] §f§lArena List: " + "\n" + " ");
             for(int i = 0; i<arenas.size(); i++){

                 player.sendMessage(arenas.get(i) + "\n");
             }

             player.sendMessage("\n" + "   §4§lE.N.D");
             arenas.clear();
         }

         else if(cmd.getName().equals("randomspawner")){

             CitizenSpawn(sender);
         }

         else if (cmd.getName().equals("addmob")){

             Player player = (Player) sender1;

             try {
                 addMob(args[0]);
             } catch (IOException e) {
                 e.printStackTrace();
             }

             player.sendMessage("§f§l[§4§lMHU§c-§f§lBattles§f§l] Mob Added");

         }

         else if (cmd.getName().equals("moblist")){



            Player player = (Player) sender1;

             mobList.clear();
            mobList();

            player.sendMessage("§f§l[§4§lMHU§c-§f§lBattles§f§l] §f§lMob List: " + "\n" + " ");


             for(int i = 0; i < mobList.size(); i++){

                 player.sendMessage(mobList.get(i) );
             }

            player.sendMessage("\n" + "   §4§lE.N.D");

        }


         else if(cmd.getName().equals("friends")){

                friendList.clear();
             try {
                 listoffriends(sender);

             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             }

             /*if(friendList!=null){
             for(int i = 0; i< friendList.size(); i++){

                 sender.sendMessage("Friend: " + friendList.get(i));
                 sender.sendMessage("UUID: "+ Bukkit.getOfflinePlayer(friendList.get(i)).getUniqueId());


             }

             }else{sender.sendMessage("It is null my friend");}*/


             friendList.clear();
             try {
                 FriendList(sender);
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             }
         }

         else if (cmd.getName().equals("blackmarket")){


             if(args[0].equals("quirkkeys")){


             Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"cc give p QuirkCrate 1 "+sender.getName());
                 Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco take " + sender.getName() + " 1000");

                 mhu.mob.remove();


             }

             if(args[0].equals("hobo")){

                 Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco take " + sender.getName() + " 50");


                 mhu.PlayerRightClicked.sendMessage(" ");
                 mhu.PlayerRightClicked.sendMessage("§2§lThankyou so much!!" + "\n" + "§2§lYou gave me an extra §a§l500$ §2§lthough.." + "\n" + "§2§lSo here you can have it back ;)");
                 mhu.PlayerRightClicked.sendMessage(" ");

                 mhu.hobo.remove();

                 Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco give " + sender.getName() + " 500");


             }

             if(args[0].equals("drugdealer")){

                 Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco take " + sender.getName() + " 1500");

                 Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user " + sender.getName() + " set magicspells.magicitem true");

                 mhu.PlayerRightClicked.sendMessage(" ");
                 mhu.PlayerRightClicked.sendMessage("§f§lHaha thankyou!!" + "\n" + "§f§lNow go out and there and be your true self!!" + "\n" + "§f§lTill we meet again..");
                 mhu.PlayerRightClicked.sendMessage(" ");

                 Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco take " + sender.getName() + " 1500");

                 Bukkit.dispatchCommand(mhu.PlayerRightClicked,"c magicitem Trigger_2");

                 mhu.drugdealer.remove();

                 DelayedCommandServer(Bukkit.getConsoleSender(),10,"50","lp user " + sender.getName() + " set magicspells.magicitem false");


             }


         }

         else if (cmd.getName().equals("blackmarketActivate")){




         }

         else if(cmd.getName().equals("removefriend")){
             Player player = (Player) sender1;
             Player friend = Bukkit.getPlayer(args[0]);

             try {
                 friendList.clear();
                 listoffriends(player);


                 removeFriend(args[0],player);
                 removeFriend(player.getName(),friend);


             } catch (InternalError | NullPointerException | IOException e) {
                 e.printStackTrace();
                 System.out.print("Friend not online");
             }

             player.sendMessage("§c§lFriend §4§lremoved!");

         }


         else if(cmd.getName().equals("gdr")){

             try {
                 giveDonatorRanks(args[0]);
             } catch (IOException e) {
                 e.printStackTrace();
             }


         }

         else if(cmd.getName().equals("accept")){

             Player player = (Player) sender1;

             if(args[0].equals("mobtp")){

                 DelayedTeleport(Bukkit.getPlayer(args[1]),player,5,"friendtp");
                 titleText(player, 0, "§4§lYou join§c§l", "1", false);
                 titleText(player, 40, "§c§l"+Bukkit.getPlayer(args[1]).getName(), "2", false);
                 titleText(player, 80, "§4§lIn battle!", "3", false);

             }

             else{

             //Call listoffriends to fill the friendslist
             try {
                 listoffriends(player);
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             }

             if(friendList.contains(Bukkit.getPlayer(args[1]).getName())){

                 Bukkit.getPlayer(args[1]).sendMessage("§f§lYou are already friends");
                 player.sendMessage("§f§lYou are already friends");

             }

             else {

                 try {
                     //Adds the friends to each others files
                     //addFriend(args[0],player);

                     addFriend(player.getName(), Bukkit.getPlayer(args[1]));
                     addFriend(Bukkit.getPlayer(args[1]).getName(), player);

                 } catch (IOException e) {
                     e.printStackTrace();
                 }

                 Bukkit.getPlayer(args[1]).sendMessage("§f§lYou and " + "§f§l" + player.getName() + " §f§lare now §a§lfriends");
                 player.sendMessage("§f§lYou and " + "§f§l" + Bukkit.getPlayer(args[1]).getName() + " §f§lare now §a§lfriends");

             }}
         }


         else if (cmd.getName().equals("decline")){
             Player player = (Player) sender1;

             if(args[0].equals("mobtp")){


             }

             else{

             player.sendMessage("§c§lFriend request §4§ldenied");}

         }

         else if(cmd.getName().equals("addfriend")){

             Player player = (Player) sender1;

             Bukkit.getPlayer(args[0]).sendMessage("§f§l[§4§lMHU§c-§f§lBattles§f§l] §f§l" + "\n" + " " + "\n" +  "§a§l"+ player.getName() + " §f§lsent you a friend request" + "\n" + " " + "\n");
             ClickableChat(Bukkit.getPlayer(args[0]),"/accept " + args[0] + " " +  player.getName(),"/decline " + args[0]);
             Bukkit.getPlayer(args[0]).sendMessage("\n" + "   §4§lE.N.D");

             player.sendMessage("§f§lYou sent a friend request to " + "§a§l" + args[0]);

         }


        else if (cmd.getName().equals("BattleEnd")) {


            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"warp tutorialStart "+sender.getName());

            QuirkTutorial(sender);


        }

        return true;
    }






    public HashMap<Player, Player> players = new HashMap<>();
    public HashMap<Player, Timer> timer = new HashMap<>();
    public Timer t = new Timer();
    public boolean stop;
    public Map<String, Integer> taskID = new HashMap<String, Integer>();





    //Text


    public void ClickableChat( Player player, String command, String command2){


        TextComponent accept = new TextComponent( "§a§l[Accept]" );

        TextComponent or = new TextComponent( " §f§lor " );

        accept.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, command ));

        accept.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT,TextComponent.fromLegacyText("§a§lClick to accept")));

        TextComponent deny = new TextComponent( "§c§l[Decline]"  );

        deny.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, command2 ));
        deny.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT,TextComponent.fromLegacyText("§c§lClick to decline")));


                player.spigot().sendMessage(accept,or, deny);


    }

    public TextComponent mhuHelp( Player player, String plugin, String command){

        TextComponent plugintxt = new TextComponent( "§7§l--> " + "§c§l" + plugin + " §7§l"  );



        plugintxt.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, command));

       // deny.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, command2 ));
       // deny.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT,TextComponent.fromLegacyText("§c§lClick to decline")));



    return plugintxt;}

    public HashMap<String,Integer>textwait = new HashMap<>();


    public boolean checkFirstLoginElements(Player player) throws FileNotFoundException {

        String data = "";
        boolean check = false;

        File myObj = new File("plugins/MHUBattles/FirstLoginElements/" + "CompletedFirstLoginPlayerList" + ".txt");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {


            data +=  myReader.nextLine() + "\n";

        }

        if(data.contains(player.getUniqueId().toString())){

            check = true;

        }
        return check;}


    public void MobChallenge(Player player, String m) throws InterruptedException, FileNotFoundException {

        if (mm.getMobManager().getMythicMob(m).getFaction().equals("Villain")){
            titleText(player, 0, "§4§lVillain §c§l" + m, "1", false);
            titleText(player, 40, "§c§lChallenges you!", "2", false);}

        else if(mm.getMobManager().getMythicMob(m).getFaction().equals("Hero")) {
            titleText(player, 0, "§6§lHero §e§l" + m, "1", false);
            titleText(player, 40, "§e§lChallenges you!", "2", false);

        }


        teleport(m,player);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user " + player.getName() + " set mhu.fzone false");



    }


    public void MobWithPath(Player player) throws IOException {


        io.lumine.xikage.mythicmobs.mobs.MythicMob m = mm.getMobManager().getMythicMob("Shoto");


        m.getAIGoalSelectors().clear();

        m.getAIGoalSelectors().add(0, "meleeattack");

        m.getAIGoalSelectors().add(1, "leapattarget");

        m.getAIGoalSelectors().add(2,"movetowardstarget");

        m.getAIGoalSelectors().add(3, "patrol 188,4,-235;180,4,-253;");


        m.setBaseHealth(50.0);

        ActiveMob mob = m.spawn(adapt(player.getLocation()),0);



        player.sendMessage("ID: 11   " + mob.getUniqueId());

        PathClearWrite(mob.getUniqueId());





    }


    public void updateMob(Player player){


        io.lumine.xikage.mythicmobs.mobs.MythicMob m = mm.getMobManager().getMythicMob("Shoto");

        pathClear();

        Optional<ActiveMob> a = mm.getMobManager().getActiveMob(pathClear().get(0));

        ActiveMob am = a.get();

        player.sendMessage("Le sigh 22: " + pathclearUUID.get(0));

        m.getAIGoalSelectors().clear();
        m.getMythicEntity().applyOptions(am.getEntity().getBukkitEntity());
        m.applyMobOptions(am,0);
        m.applyMobVolatileOptions(am);
        m.applySpawnModifiers(am);
        am.resetTarget();
        am.setLevel(1);


        //m.spawn(am.getLocation(),0);

        am.setDead();
        am.setDespawned();
        am.unregister();







    }

    public static ArrayList<String> Reader(String filename, String keyword) throws IOException {


        ArrayList <String> data = new ArrayList();

        try {
            File Filepath = new File(filename + ".txt");

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(Filepath));
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

                if (line.contains(keyword)) {

                    if (keyword != "")
                        data.add(line.split(keyword)[1]);
                    else
                        data.add(line);

                }
                // read next line
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return data;
        } finally {

        }


    }



    public void giveDonatorRanks(String group) throws IOException {

        ArrayList<String> doublesCheck = new ArrayList();

        for(String info : Reader( "plugins/Donators",group)) {


            try {

                if (!doublesCheck.contains(info.split(",")[6].replaceAll("\"", ""))) {

                    doublesCheck.add(info.split(",")[6].replaceAll("\"", ""));

                    String ign = info.split(",")[6].replaceAll("\"", "");

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + ign + " addgroup " + group);
                }
            } catch (Exception e) {
            }



        }

    }


    public ArrayList<UUID> pathClear (){

        try {
            File myObj = new File("plugins/MHUBattles/ActiveMob.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

             pathclearUUID.add(UUID.fromString(data));

            }
            myReader.close();
        } catch (
                FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


        return pathclearUUID;}


    public void PathClearWrite(UUID uniqueID) throws IOException {

        File file = new File("plugins/MHUBattles/ActiveMob.txt");

        FileWriter objectName = new FileWriter(file, false);
        PrintWriter pw = new PrintWriter(objectName);

        pw.write(String.valueOf(uniqueID));

        pw.flush();
        pw.close();

        objectName.close();

    }







    //Laggy Option

    /*
    public void FriendList(Player player){

        Inventory meh  = Bukkit.getServer().createInventory(player, 9, "          §a§lFriends");



        //Options

       ArrayList<String> friends = new ArrayList<>();

       friends.add("Naruto");
       friends.add("Eliejr");
       friends.add("Sasuke");




        for (int i = 0; i < friends.size(); i++) {

            ItemStack ref1 = getHead(friends.get(i));

            meh.setItem(i, ref1);

        }

        //player.updateInventory();
        player.openInventory(meh);
        player.updateInventory();

    }
*/



        Inventory friendsINV;
        public void FriendList(Player player) throws FileNotFoundException {

            friendList.clear();
            listoffriends(player);


            this.friendsINV = Bukkit.createInventory(null, 9, "Friends");
            ArrayList<UUID> friends = new ArrayList<UUID>();

           for(int i = 0; i< friendList.size(); i++){

            friends.add(Bukkit.getOfflinePlayer(friendList.get(i)).getUniqueId());}

            populateInterface(player, friends);
        }

        public void populateInterface(Player player, ArrayList<UUID> friends) {


            if(friends.size()>=8){

                player.sendMessage("§a§lYou can only have 9 friends!" + "\n" + "§a§lPlease remove some friends with §c§l/removefriend friendname");

            }else{

                    for(UUID s : friends) {
                        friendsINV.addItem(getHead(s));
                    }
                    player.openInventory(friendsINV);}


        }

        public ItemStack getHead(UUID playeruid) {
            Player player = Bukkit.getPlayer(playeruid);
            ItemStack item = new ItemStack(Material.SKULL_ITEM,1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            if(player != null) {
                meta.setOwner(player.getName());
                meta.setDisplayName(ChatColor.AQUA + player.getName());
                lore.add("Status: " + ChatColor.GREEN + "Online");
                String[] split = ms.getQuirkGroup(player).split("(?=\\p{Upper})");


                String quirkgroup = "";
                for(String splits: split){
                quirkgroup += splits + " ";}

                lore.add("Quirk: " + ChatColor.GREEN + quirkgroup);

            } else {
                OfflinePlayer Op = Bukkit.getOfflinePlayer(playeruid);
                meta.setOwner(Op.getName());
                meta.setDisplayName(ChatColor.AQUA + Op.getName());
                lore.add("Status: " + ChatColor.RED + "Offline");

            /*
                String[] split = ms.getQuirkGroup(player).split("(?=\\p{Upper})");

                String quirkgroup = "";

                for(String splits: split){

                    quirkgroup += splits + " ";}

                lore.add("Quirk: " + ChatColor.RED + quirkgroup);*/
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
        }



        public ItemStack getSkull(Player player){

            SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            meta.setOwner(player.getName());
            skull.setItemMeta(meta);

        return skull;}


int j = 0;


    public static Entity[] getNearbyEntities(Location l, int radius) {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet <Entity> radiusEntities = new HashSet < Entity > ();

        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                for (Entity e: new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
                        radiusEntities.add(e);
                }
            }
        }

        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }


        public void NearbyFriends(Player player) throws FileNotFoundException {

            friendList.clear();


            listoffriends(player);

            Location user = player.getLocation();


            for(int i = 0; i<friendList.size(); i++){

                if(Bukkit.getOfflinePlayer(friendList.get(i)).isOnline()&&Bukkit.getPlayer(friendList.get(i)).getWorld().equals(user.getWorld())){


                    if(Bukkit.getPlayer(friendList.get(i)).getLocation().getWorld().equals(player.getLocation().getWorld())) {

                        Bukkit.getPlayer(friendList.get(i)).sendMessage("§f§l[§4§lMHU§c-§f§lBattles§f§l] §f§l" + "\n" + " " + "\n" + "§c§lDo you want to join" + " §a§l" + player.getName() + "\n" + " " + "\n" + "§c§lin §4§lBattle? " + " " + "\n" + " " + "\n");
                        ClickableChat(Bukkit.getPlayer(friendList.get(i)), "/accept mobtp " + player.getName(), "/decline mobtp" + player.getName());
                        Bukkit.getPlayer(friendList.get(i)).sendMessage("      ");
                    }
            }

            } }


    public void CitizenSpawn(Player player){

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user " + player.getName() + " set mhu.fzone true");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user " + player.getName() + " set mhu.fzone true");


        if(player.hasPermission("mhu.fzone")) {

            j++;

            String[] mobs = {"SuneaterDummy", "DenkiDummy", "DabiDummy", "ShotoDummy"};



            Location loc = player.getLocation();

//Random for which mob to be chosen out of the list
            int random = (int) (Math.random() * mobs.length);

//Random 1-5 blocks infront of the player or beside
            int mobspawnrandom = (int) (Math.random() * 5);

            int timerandom = (int) ((Math.random() * 150) + 50);


            Vector direction = player.getLocation().getDirection();

            Location front = loc.add(direction);

            //Use front as a test though mobloc spawns the mobs randomly around the player / tiny bit harder to find maybe

            Location mobloc = new Location(loc.getWorld(), loc.getX() + mobspawnrandom, loc.getY(), loc.getZ() + mobspawnrandom, loc.getYaw(), loc.getPitch());

            int numberofCitizens = (int) (Math.random() * 24);

            DelayedRandomSpawnCitizen(player, timerandom, "" + j);


        }
    }


    public ArrayList<String> mobList (){

        try {
            File myObj = new File("plugins/MHUBattles/BattleMobs.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                mobList.add(data);

            }
            myReader.close();
        } catch (
                FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


        return mobList;}



    public void addFriend(String playername, Player player) throws IOException {

        File file = new File("plugins/MHUBattles/Friends/"+player.getUniqueId().toString() + ".txt");

        FileWriter objectName = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(objectName);

        pw.write(playername + "\n");

        pw.flush();
        pw.close();

        objectName.close();

    }


    public void removeFriend(String playername, Player player) throws IOException {

        friendList.clear();
        listoffriends(player);

        if(friendList.contains(playername)) {

            try {

                String inFile = "plugins/MHUBattles/Friends/" + player.getUniqueId().toString() + ".txt";

                String lineToRemove = playername;


                StringBuffer newContent = new StringBuffer();

                BufferedReader br = new BufferedReader(new FileReader(inFile));
                String line = br.readLine();
                while ((line = br.readLine()) != null) {
                    if (!line.trim().equals(lineToRemove)) {
                        newContent.append(line);
                        newContent.append("\n"); // new line

                    }
                }
                br.close();

                FileWriter removeLine = new FileWriter(inFile);
                BufferedWriter change = new BufferedWriter(removeLine);
                PrintWriter replace = new PrintWriter(change);
                replace.write(newContent.toString());
                replace.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public ArrayList<String> listoffriends (Player player) throws FileNotFoundException {

        friendList.clear();
        File myObj = new File("plugins/MHUBattles/Friends/"+player.getUniqueId().toString()+".txt");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();

            friendList.add(data);

        }
        myReader.close();


        return friendList;}


    public void addMob (String mobname) throws IOException {

        File file = new File("plugins/MHUBattles/BattleMobs.txt");

        FileWriter objectName = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(objectName);

        pw.write(mobname + "\n");

        pw.flush();
        pw.close();

        objectName.close();

    }


int i = 0;

    void setArena(String arena) throws IOException {



        File file = new File("plugins/MHUBattles/MHUBattleArenas.txt");

        FileWriter objectName = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(objectName);


        pw.write("Arena: " + arena + "\n");




        pw.flush();
        pw.close();

        objectName.close();



    }

    public void getArenas(){

        try {
            File myObj = new File("plugins/MHUBattles/MHUBattleArenas.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.contains("Arena")){

                    String[] arenasplit = data.split("Arena: ");
                    arenas.add(arenasplit[1]);

                }
            }
            myReader.close();
        } catch (
                FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void teleport(String mob, Player player) throws InterruptedException, FileNotFoundException {


        getArenas();

        j++;

        int random = (int)(Math.random() * arenas.size()-1);


        //Add to taken arenas

        ArrayList<String> data = new ArrayList<>();

        data = returnArenaData("plugins/MHUBattles/MHUBattleArenas.txt",arenas.get(random));

        if(takenArenas.containsValue(data.get(0))){


            data = returnArenaData("plugins/MHUBattles/MHUBattleArenas.txt",arenas.get(random));


        }

        takenArenas.put(player.getUniqueId().toString(),data.get(0));





        String[] world = data.get(0).split("Location1: ");

        String[] loc1split = data.get(1).split(",");

        double x = parseDouble(loc1split[0]);
        double y = parseDouble(loc1split[1]);
        double z = parseDouble(loc1split[2]);

        Location mobloc = new Location(Bukkit.getWorld(world[1]),x,y,z);


        String[] loc2split = data.get(2).split(",");

        double x1 = parseDouble(loc2split[0]);
        double y1 = parseDouble(loc2split[1]);
        double z1 = parseDouble(loc2split[2]);

        float yaw = (float) parseDouble(data.get(3));

        float pitch = (float) parseDouble(data.get(4));

        Location playerloc = new Location(Bukkit.getWorld(world[1]),x1,y1,z1,yaw,pitch);


        player.teleport(playerloc);




        DelayedSpawnMob(player,50,"Meh1" + j,mob,mobloc);

       // player.sendMessage("Arena taken" + arenas.get(random));


        //Remove Arena from taken list


        // /arenaClear <mob.uuid>  /arenabusy <target.name> <mob.uuid>



        UUID ui = null;








    }


    public ArrayList<String> returnArenaData(String filename, String arena) {

        ArrayList<String> data = new ArrayList<>();

        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String raw = myReader.nextLine();

                if (raw.contains(arena)) {

                    String world = myReader.nextLine();

                    //World Place 0
                    data.add(world);

                    String loc1 = myReader.nextLine();

                    //Location1 Place 1
                    data.add(loc1);

                    String skip = myReader.nextLine();

                    System.out.print("Skipline");

                    String loc2 = myReader.nextLine();

                    //Location Place 2
                    data.add(loc2);

                  //Yaw Place 3

                    String yawdata = myReader.nextLine();
                    String[] yaw = yawdata.split(",");
                    data.add(yaw[0]);

                    //Pitch place 4

                    data.add(yaw[1]);






                }
            }
            myReader.close();
        } catch (
                FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;}




    //QUIRK TUTORIAL

    public void QuirkTutorial(Player player){


        DelayedCommand(player,1,"M1","lp user " + player.getName() + " set mhu.tutorial.still");

        titleText(player,0,"§f§lWelcome to ","1",false);
        titleText(player,40,"§4§lMy §f§lHero §4§lUnleashed","2",false);

        Text(player,100,"\n" + "   " + "\n" +"§f§lThis tutorial will teach you everything about quirks and don’t bother to try and use a quirk or move " + "\n" + "   " + "\n" +"§f§lcause I have erased both your quirk and uh mobility, I guess." +"\n" + "   " + "\n" + "   ","3");



        Text(player,250,"\n" + "   " + "\n" +"§f§lFirst let’s go through how to obtain a quirk. Here is a netherstar and when you have it in  " + "\n" + "   " + "\n" +"your hand then right click to scroll through abilities and left click to activate them. "+"\n" + "   " + "\n" + "   ","4");
        DelayedCommand(player,250,"T4","give " + player.getName() + " netherstar 1");



        Text(player,450,"\n" + "   " + "\n" +"§f§lEvery quirk has some sort of passive and you have to figure out which key to press to activate it. " + "\n" + "   " + "\n" +"For example you have been given the Explosion Quirk temporarly. It uses all the passive keys for some of its moves. "+"\n" + "   " + "\n" + "   ","5");



        Text(player,650, "\n" +"§f§lTriggeres for quirks are either " +"\n" + "§a§lQ§f§l, §a§lF §f§lor the §a§lSHIFT§f§l keys" + "   " + "\n" + "   ","6");


        Text(player,850,"\n" + "   " + "\n" +"§f§lIf you shift while having the explosion quirk a medium sized explosion will occur that can hurt your opponent." + "\n" + "   " + "\n" + "If you activate the move §a§lBlast Rush Turbo§f§l using the netherstar then press §a§lQ §f§l and §a§lF §f§l to blast forwards." +"\n" + "   " + "\n" + "   ","7");

        Text(player,1060,"\n" + "   " + "\n" +"§f§land if you do that while holding down §a§lShift §f§lyou will fly upwards. " + "\n" + "   " + "\n" + "§f§lYou now have the §6§lExplosion §c§lQuirk §f§land you can move again so give it a go!!" +"\n" + "   " + "\n" + "   ","8");
        DelayedCommand(player,1155,"T8", "qtp ExplosionQuirk " + player.getName());
        DelayedCommand(player,1061,"M2","lp user " + player.getName() + " set mhu.tutorial.still false");

        Text(player,1250,"\n" + "   " + "\n" + "§f§lNow fly above the wall infront of you  " + "\n" + "   " + "\n" + "" +"\n" + "   " + "\n" + "   ","9");

        Text(player,1450,"\n" + "   " + "\n" +"§f§lIf you need a repeat on how to do that then read this book " + "\n" + " ","10");
        DelayedCommand(player,1450,"T10","give " + player.getName() + " writtenbook 1 title:&6&lExplosion&c&lQuirk author:Midoriya lore:ExplosionQuirk_Tutorial book:ExplosionQuirk");








    }


    //END QUIRK TUTORIAL




    public void titleText(final Player sender, long ticks ,String text,String indentifier,boolean check){


        textwait.put(sender.getName()+indentifier,0);

        final boolean[] check1 = {false};


        final int tid = mhu.getServer().getScheduler().scheduleSyncRepeatingTask(mhu, new Runnable() {
            public void run() {

                textwait.put(sender.getName()+indentifier,textwait.get(sender.getName()+indentifier)+1);


                //END
                if (textwait.get(sender.getName()+indentifier)==2&& check1[0] ==false) {

                    titleManagerAPI.sendTitle(sender, text);

                    TextendTask(sender.getName()+indentifier);

                }
                else if (textwait.get(sender.getName()+indentifier)==2&& check1[0] ==true) {

                    sender.sendMessage(text);

                    check1[0] = false;


                }
            }
        }, 0, ticks); //schedule task with the ticks specified in the arguments

        taskID.put(sender.getName()+indentifier, tid); //put the player in a hashmap


    }


    public void DelayedTeleport(Player sender,Player sender2 ,long ticks,String indentifier){


        textwait.put(sender.getName()+indentifier,0);

        final boolean[] check1 = {false};


        final int tid = mhu.getServer().getScheduler().scheduleSyncRepeatingTask(mhu, new Runnable() {
            public void run() {

                textwait.put(sender.getName()+indentifier,textwait.get(sender.getName()+indentifier)+1);


                //END
                if (textwait.get(sender.getName()+indentifier)==2) {


                    sender2.teleport(sender.getLocation());



                    TextendTask(sender.getName()+indentifier);

                }
            }
        }, 0, ticks); //schedule task with the ticks specified in the arguments

        taskID.put(sender.getName()+indentifier, tid); //put the player in a hashmap


    }




    public void DelayedCommand(Player sender, long ticks,String indentifier, String Command){


        textwait.put(sender.getName()+indentifier,0);

        final boolean[] check1 = {false};


        final int tid = mhu.getServer().getScheduler().scheduleSyncRepeatingTask(mhu, new Runnable() {
            public void run() {

                textwait.put(sender.getName()+indentifier,textwait.get(sender.getName()+indentifier)+1);


                //END
                if (textwait.get(sender.getName()+indentifier)==2) {



                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),Command);



                    TextendTask(sender.getName()+indentifier);

                }
            }
        }, 0, ticks); //schedule task with the ticks specified in the arguments

        taskID.put(sender.getName()+indentifier, tid); //put the player in a hashmap


    }


    public void ImmuneMythicMobs(String ImmuneMob) throws IOException {

        File folder = new File("plugins/MHUBattles/ImmuneMythicMobsAgainstQuirks");

        if(!folder.exists())
            folder.mkdir();

        File file = new File("plugins/MHUBattles/ImmuneMythicMobsAgainstQuirks/" + "ImmuneMobs" + ".txt");


        FileWriter objectName = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(objectName);

        pw.write(ImmuneMob + "\n");

        pw.flush();
        pw.close();

        objectName.close();


    }


    public void writeFirstLoginElements(Player player) throws IOException {

        File folder = new File("plugins/MHUBattles/FirstLoginElements");

        if(!folder.exists())
            folder.mkdir();

        File file = new File("plugins/MHUBattles/FirstLoginElements/" + "CompletedFirstLoginPlayerList" + ".txt");


        FileWriter objectName = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(objectName);

        pw.write(player.getUniqueId() + "\n");

        pw.flush();
        pw.close();

        objectName.close();


    }


    public void DelayedCommandServer(ConsoleCommandSender sender, long ticks, String indentifier, String Command){


        textwait.put(sender.getName()+indentifier,0);

        final boolean[] check1 = {false};


        final int tid = mhu.getServer().getScheduler().scheduleSyncRepeatingTask(mhu, new Runnable() {
            public void run() {

                textwait.put(sender.getName()+indentifier,textwait.get(sender.getName()+indentifier)+1);


                //END
                if (textwait.get(sender.getName()+indentifier)==2) {



                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),Command);



                    TextendTask(sender.getName()+indentifier);

                }
            }
        }, 0, ticks); //schedule task with the ticks specified in the arguments

        taskID.put(sender.getName()+indentifier, tid); //put the player in a hashmap


    }


    public void TimerRandomSpawn(Player player, long ticks, String DelayMobType, String indentifier){


        int random = (int) Math.random();


        textwait.put(indentifier,0);

        final boolean[] check1 = {false};


        final int tid = mhu.getServer().getScheduler().scheduleSyncRepeatingTask(mhu, new Runnable() {
            public void run() {

                textwait.put(indentifier,textwait.get(indentifier)+1);


                //END
                if (textwait.get(indentifier)==2) {


                    if(DelayMobType.equals("Drugdealer")){


                        mhu.drugdealerwait.put(player,false);

                        //player.sendMessage("Done Drugdealer timer");

                        TextendTask(indentifier);


                    }



                   if(DelayMobType.equals("Shady Trader")){


                        mhu.shadydealerwait.put(player,false);

                        //player.sendMessage("Done Shady Trader timer");

                       TextendTask(indentifier);

                    }

                   if(DelayMobType.equals("hobo")){


                        mhu.hobowait.put(player,false);

                        //player.sendMessage("Done Hobo timer");

                       TextendTask(indentifier);

                    }

                    if(DelayMobType.equals("mobtimer")){


                        mhu.mobtimer.put(player,false);

                        //player.sendMessage("Done Mobtimer");

                        TextendTask(indentifier);
                    }


                }
            }
        }, 0, ticks); //schedule task with the ticks specified in the arguments

        taskID.put(indentifier, tid); //put the player in a hashmap


    }



    public void CitizenSpawner(Player sender){

        if(sender.hasPermission("mhu.fzone")) {

            int nearbymobs = mm.getRandomSpawningManager().getMobsNearPlayer(adapt(sender));


            mobList.clear();

            mobList();

            Location loc = sender.getLocation();

            int numberofCitizens = (int) (Math.random() * 24);

            Location mobloc1 = new Location(loc.getWorld(), loc.getX() + 15, loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

            Location mobloc4 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw() + 15, loc.getPitch());
                    //END
                    if (nearbymobs < 5) {

                        mm.getMobManager().spawnMob("Citizen"+numberofCitizens, mobloc1);
                        mm.getMobManager().spawnMob("Citizen"+numberofCitizens, mobloc4);


                    }



        }

    }


    public void DelayedRandomSpawn(Player sender, long ticks,String indentifier,String mobname, Location mobloc){

        //MythicMobs.inst().getAPIHelper().getMythicMobInstance(e).setDespawned();

       // mm.getAPIHelper().getMythicMobInstance((Entity) am).setDespawned();

        //MythicMob ms = null;

        //ms.setBaseHealth(0.0);



        if(sender.hasPermission("mhu.fzone")) {

            int nearbymobs = mm.getRandomSpawningManager().getMobsNearPlayer(adapt(sender));


            mobList.clear();

            mobList();

            Location loc = sender.getLocation();





//Random for which mob to be chosen out of the list
            int random = (int) (Math.random() * mobList.size());

//Random 1-5 blocks infront of the player or beside
            int mobspawnrandom = (int) (Math.random() * 5);



            Location mobloc1 = new Location(loc.getWorld(), loc.getX() + mobspawnrandom, loc.getY(), loc.getZ() + mobspawnrandom, loc.getYaw(), loc.getPitch());

            textwait.put(sender.getName() + indentifier, 0);


            final int tid = mhu.getServer().getScheduler().scheduleSyncRepeatingTask(mhu, new Runnable() {
                public void run() {

                    textwait.put(sender.getName() + indentifier, textwait.get(sender.getName() + indentifier) + 1);

                    //END
                    if (textwait.get(sender.getName() + indentifier) == 2 && !(nearbymobs > 5)) {

//Random for which mob to be chosen out of the list


                        DelayedRandomSpawn(sender, 100, indentifier, mobList.get(random), mobloc1);

                        TextendTask(sender.getName() + indentifier);

                    }
                }
            }, 0, ticks); //schedule task with the ticks specified in the arguments
            taskID.put(sender.getName() + indentifier, tid); //put the player in a hashmap


                mm.getMobManager().spawnMob(mobList.get(random), mobloc1);

                mhu.getServer().getScheduler().getPendingTasks().remove(this);

            }

        }



    public int MMAroundPlayer(Player sender){

        ArrayList<Entity> mythicEntities = new ArrayList<>();

        mhu.randomSpawnTest1 = 0;

        for(ActiveMob uno2 : mm.getMobManager().getActiveMobs()){

            mythicEntities.add(uno2.getEntity().getBukkitEntity());

        }



        for(Entity mobs : sender.getNearbyEntities(sender.getLocation().getX(),sender.getLocation().getY(),sender.getLocation().getZ())){


            if(mythicEntities.contains(mobs)&&mobs.getName()!="Citizen"){


                if(mobs.getLocation().distance(sender.getLocation())<=20)

                    mhu.randomSpawnTest1++;


            }

            if(mobs.getLocation().distance(sender.getLocation())<=30&&mobs.getName()!="Citizen")
                mhu.MMDistance = 30;

        }


        return mhu.randomSpawnTest1;}



        public int CitizensAroundPlayer(Player sender){


        mhu.randomSpawnTest = 0;

            ArrayList<Entity> mythicEntities = new ArrayList<>();

            for(ActiveMob uno2 : mm.getMobManager().getActiveMobs()){

                mythicEntities.add(uno2.getEntity().getBukkitEntity());

            }



            for(Entity mobs : sender.getNearbyEntities(sender.getLocation().getX(),sender.getLocation().getY(),sender.getLocation().getZ())){


                if(mythicEntities.contains(mobs)){


                    if(mobs.getLocation().distance(sender.getLocation())<=20)

                    mhu.randomSpawnTest++;


                }

                if(mobs.getLocation().distance(sender.getLocation())<=30)
                    mhu.HeroDistance = 30;

            }


        return mhu.randomSpawnTest;}




    public int MMHero_Villain(Player sender){

        ArrayList<Entity> mythicEntities = new ArrayList<>();

        for(ActiveMob uno2 : mm.getMobManager().getActiveMobs()){

            mythicEntities.add(uno2.getEntity().getBukkitEntity());

        }


try {

    Location senderloc = new Location(sender.getWorld(),sender.getLocation().getX(),sender.getLocation().getY(),sender.getLocation().getZ());

    for (Entity mobs : getNearbyEntities(senderloc,27)) {


        if (mythicEntities.contains(mobs) && !mobs.getName().equals("Citizen") && !mobs.getName().equals("Hobo") && !mobs.getName().equals("Shady Trader") && !mobs.getName().equals("Drugdealer")) {


            mhu.MobsAroundPlayer.put(sender, (int) mobs.getLocation().distance(sender.getLocation()));

            //sender.sendMessage("Distance: " + (int) mobs.getLocation().distance(sender.getLocation()));

            return (int) mobs.getLocation().distance(sender.getLocation());


        }


    }

} catch (NullPointerException e) {

    sender.sendMessage(e.getMessage());

    e.printStackTrace();

    mhu.MobsAroundPlayer.put(sender, (int) 27);

}


        return 30;}




    public void DelayedRandomSpawnCitizen(Player sender, long ticks,String indentifier){

        int nearbymobs = CitizensAroundPlayer(sender);


        if(sender.hasPermission("mhu.fzone")) {


            sender.sendMessage("Mobs Around You : " + nearbymobs);



            textwait.put(sender.getName() + indentifier, 0);





            final int tid = mhu.getServer().getScheduler().scheduleSyncRepeatingTask(mhu, new Runnable() {
                public void run() {

                    textwait.put(sender.getName() + indentifier, textwait.get(sender.getName() + indentifier) + 1);



                    //END
                    if (textwait.get(sender.getName() + indentifier) == 2 && nearbymobs < 3) {

//Random for which mob to be chosen out of the list

                        if(nearbymobs<5){
                        DelayedRandomSpawnCitizen(sender, 50, indentifier);

                        TextendTask(sender.getName() + indentifier);}

                    }
                }
            }, 0, ticks); //schedule task with the ticks specified in the arguments
            taskID.put(sender.getName() + indentifier, tid); //put the player in a hashmap

            int numberofCitizens = (int) (Math.random() * 24);


            String[] mobs = {"SuneaterDummy", "DenkiDummy", "DabiDummy", "ShotoDummy"};



//Random for which mob to be chosen out of the list
            int randomMobChoose = (int)Math.floor((Math.random()*mobs.length)+0);

            int randomChance = (int)Math.floor((Math.random()*1)+4);

            io.lumine.xikage.mythicmobs.mobs.MythicMob m = null;

            if(randomChance!=3||randomChance!=2){
                sender.sendMessage("Citizen Spawned");
            m = mm.getMobManager().getMythicMob("Citizen"+numberofCitizens);}
            else if(randomChance==3||randomChance==2) {

                sender.sendMessage("Mob Spawned");
                m = mm.getMobManager().getMythicMob("Shoto");
            }

            int random = (int)Math.floor((Math.random()*0)+1);;

            if(random!=0)
            random = (int)Math.floor((Math.random()*7)+3);
            else random = (int)Math.floor((Math.random()*-7)-3);


            Location playerLoc = null;
            if(random!=0)
            playerLoc = new Location(sender.getWorld(),sender.getLocation().getX()+random,sender.getLocation().getY(),sender.getLocation().getZ()+random);

            else
                playerLoc = new Location(sender.getWorld(),sender.getLocation().getX()+random,sender.getLocation().getY(),sender.getLocation().getZ());

            Location loc = new Location(sender.getWorld(),68,480,1000);

            ActiveMob mob = m.spawn(adapt(loc),0);

            mob.getEntity().getBukkitEntity().teleport(playerLoc);


            mhu.getServer().getScheduler().getPendingTasks().remove(this);

        }

    }




    public void DelayedSpawnMob(Player sender, long ticks,String indentifier, String mobname, Location mobloc){


        textwait.put(sender.getName()+indentifier,0);

        final boolean[] check1 = {false};


        final int tid = mhu.getServer().getScheduler().scheduleSyncRepeatingTask(mhu, new Runnable() {
            public void run() {

                textwait.put(sender.getName()+indentifier,textwait.get(sender.getName()+indentifier)+1);


                //END
                if (textwait.get(sender.getName()+indentifier)==2) {


                    mm.getMobManager().spawnMob(mobname,mobloc);



                    TextendTask(sender.getName()+indentifier);

                }
            }
        }, 0, ticks); //schedule task with the ticks specified in the arguments

        taskID.put(sender.getName()+indentifier, tid); //put the player in a hashmap


    }



    public void Text(final Player sender, long ticks ,String text,String indentifier){


        textwait.put(sender.getName()+indentifier,0);

        final boolean[] check1 = {false};


        final int tid = mhu.getServer().getScheduler().scheduleSyncRepeatingTask(mhu, new Runnable() {
            public void run() {

                textwait.put(sender.getName()+indentifier,textwait.get(sender.getName()+indentifier)+1);


                //END
                if (textwait.get(sender.getName()+indentifier)==2) {

                    sender.sendMessage(text);

                    TextendTask(sender.getName()+indentifier);

                }
            }
        }, 0, ticks); //schedule task with the ticks specified in the arguments

        taskID.put(sender.getName()+indentifier, tid); //put the player in a hashmap


    }



    public void TextendTask(String p){
        if(taskID.containsKey(p)){
            int tid = taskID.get(p); //get the ID from the hashmap
            mhu.getServer().getScheduler().cancelTask(tid); //cancel the task
            taskID.remove(p); //remove the player from the hashmap

        }
    }










}
