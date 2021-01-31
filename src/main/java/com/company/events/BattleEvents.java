    package com.company.events;

    import com.company.MHUBattles;
    import com.company.commands.BattleCMD;
    import com.company.util.Util;
    import com.nisovin.magicspells.Spellbook;
    import com.sk89q.worldguard.bukkit.BukkitUtil;
    import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
    import com.sk89q.worldguard.protection.ApplicableRegionSet;
    import com.sk89q.worldguard.protection.managers.RegionManager;
    import io.lumine.xikage.mythicmobs.MythicMobs;
    import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
    import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
    import org.bukkit.Bukkit;
    import org.bukkit.ChatColor;
    import org.bukkit.Location;
    import org.bukkit.Material;
    import org.bukkit.block.BlockFace;
    import org.bukkit.entity.Player;
    import org.bukkit.event.EventHandler;
    import org.bukkit.event.Listener;
    import org.bukkit.event.entity.FoodLevelChangeEvent;
    import org.bukkit.event.entity.PlayerDeathEvent;
    import org.bukkit.event.inventory.CraftItemEvent;
    import org.bukkit.event.inventory.InventoryAction;
    import org.bukkit.event.player.*;
    import org.bukkit.permissions.PermissionAttachmentInfo;
    import org.bukkit.util.Vector;

    import java.io.*;
    import java.nio.file.Files;
    import java.nio.file.Paths;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.Scanner;

    import static io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter.adapt;
    import static java.lang.Integer.parseInt;

    public class BattleEvents extends BattleCMD implements Listener  {


        int i = 0;
        int v = 0;

        boolean loaded = false;

        Util u = new Util();

        private static final MHUBattles mhu;

        static {
            mhu = (MHUBattles) Bukkit.getServer().getPluginManager().getPlugin("MHUBattles");
        }

        HashMap<String,ArrayList<Double>> List = new HashMap<>();

        ArrayList<String> friendList = new ArrayList();
        ArrayList friends = new ArrayList();
        ArrayList<String> mobList = new ArrayList();
        com.company.MHUBattles autoevent;

        public MythicMobs mm = (MythicMobs) Bukkit.getServer().getPluginManager().getPlugin("MythicMobs");

        public WorldGuardPlugin wg = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");


        @EventHandler
        public void onInventoryClick(CraftItemEvent event) {

            Player player = (Player) event.getWhoClicked();
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Crafting is disabled on this server!");
        }


        //Default nick command replaced

        @EventHandler
        public void CommandExecutedEvent(PlayerCommandPreprocessEvent cmd){

            if(cmd.equals("nick"))
                cmd.setCancelled(true);


            if(cmd.getMessage().contains("nick ")){

                cmd.setCancelled(true);

                Player p1 = cmd.getPlayer();

                String name = cmd.getMessage().split(" ")[1];
                String surname = es.getUser(p1.getPlayer())._getNickname().split(" ")[1];
                String gender;

                try{

                    gender = es.getUser(p1.getPlayer())._getNickname().split(" ")[2];

                    es.getUser(p1.getPlayer()).setNickname(ChatColor.translateAlternateColorCodes('&',(name + " " + surname + " " + gender)));
                    mhu.surnameCheck.add(p1.getUniqueId());

                    p1.sendMessage("§a§lYour nick is now " + cmd.getMessage().split(" ")[1]);}

                catch (Exception e){

                    try {

                        es.getUser(p1.getPlayer()).setNickname(ChatColor.translateAlternateColorCodes('&', (name + " " + surname)));
                        mhu.surnameCheck.add(p1.getUniqueId());

                        p1.sendMessage("§a§lYour nick is now " + cmd.getMessage().split(" ")[1]);

                    }catch (Exception e1){

                        es.getUser(p1.getPlayer()).setNickname(ChatColor.translateAlternateColorCodes('&', (p1.getName())));

                        mhu.surnameCheck.add(p1.getUniqueId());
                        p1.sendMessage("This one ");
                        p1.sendMessage("§a§lYour nick is now " + cmd.getMessage().split(" ")[1]);
                    }
                }}
        }


        //Annoying Player Section


        @EventHandler public void HageshiiItamiDeathMessage (PlayerDeathEvent player){

            if(player.getEntity().getName().equals("HageshiiItami"))
            player.setDeathMessage(player.getEntity().getName() + " §c§lkilled by a child on a tricycle!");

        }

        @EventHandler
        public void HageshiiItami(AsyncPlayerChatEvent chat){

            Player player = (Player) chat.getPlayer();

            int random = (int) (Math.random() * 10) + 0;

            if(player.getName().equals("HageshiiItami")&&random==2){

                chat.getMessage().toLowerCase();
                chat.setMessage("§7"+chat.getMessage().toLowerCase() + "\n" + " " + "\n" + "§c§l - Naruto Best Owner §4§l❤");
            }


            //MHU FAMILIY

            boolean married = false;
            boolean adopted = false;


             if(chat.getMessage().toLowerCase().equals("yes")){

                 for(PermissionAttachmentInfo permission : player.getEffectivePermissions()) {

                     if (permission.getPermission().contains("adopted."))
                         adopted = true;

                     //if(adopted==true&&permission.getPermission().contains("adopt.proposal."))
                         //player.sendMessage("§a§lYou are already adopted");
                 }

                 for(PermissionAttachmentInfo permission : player.getEffectivePermissions()) {

                     if(permission.getPermission().contains("adopt.proposal.")&&adopted==false){

                         chat.setCancelled(true);
                         Player parent = null;
                         String playername = permission.getPermission().split("adopt.proposal.")[1];

                         Player p2 = Bukkit.getPlayer(playername);

                         for(PermissionAttachmentInfo parentperm : p2.getEffectivePermissions()){

                             if(parentperm.getPermission().contains("married.")){

                                 String parentname = parentperm.getPermission().split("married.")[1];
                                 String filteredparentname = parentname.split("[.]")[0];
                                 parent = Bukkit.getPlayer(filteredparentname);

                             }
                         }

                         if(!p2.equals(player)){

                             titleText(p2, 0, "§a§lThey answered....§a§l", "1", false);
                             titleText(p2, 40, "§a§lYES!!!", "3", false);


                             String gender = es.getUser(player.getPlayer())._getNickname().split(" ")[2];
                             String surname = es.getUser(p2.getPlayer())._getNickname().split(" ")[1];
                             String name = es.getUser(player.getPlayer())._getNickname().split(" ")[0];

                             es.getUser(player.getPlayer()).setNickname(ChatColor.translateAlternateColorCodes('&',(name + " " + surname + " " + gender)));
                             //es.getUser(p2.getPlayer()).setNickname(ChatColor.translateAlternateColorCodes('&',(name + " " + surname + " " + gender)));

                             Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p2.getName() + " set adopted." + player.getName() + "." + surname + " true");
                             Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + chat.getPlayer().getName() + " set adopted." + player.getName() + "." + surname + " true");
                             Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p2.getName() + " set adopted" + " true");
                             Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p2.getName() + " set MixedQuirk" + " true");
                             Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + chat.getPlayer().getName() + " set adopted" + " true");


                             if(parent==null)
                                 player.sendMessage("NULL");
                             else
                                 player.sendMessage(parent.getName() + " not null");

                             for(Player onlineplayer : Bukkit.getOnlinePlayers()){


                                 onlineplayer.sendMessage("§4§l❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
                                 onlineplayer.sendMessage("§a§l" + player.getName() + " §f§lis now a §d§lchild §f§lof " + "§a§l"+ p2.getName() + " §f§land§a§l " + parent.getName());
                                 onlineplayer.sendMessage("§4§l❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");



                             }

                             Spellbook parent1 = com.nisovin.magicspells.MagicSpells.getSpellbook(p2);
                             Spellbook parent2 = com.nisovin.magicspells.MagicSpells.getSpellbook(parent);

                             String spellbook1 = parent1.getSpellBookGroup();
                             String spellbook2 = parent2.getSpellBookGroup();

                             Bukkit.dispatchCommand(player, "mixquirks " + spellbook1 + " " + spellbook2);


                         }



                     }}


                     for(PermissionAttachmentInfo permission1 : player.getEffectivePermissions()) {

                         if (permission1.getPermission().matches("married"))
                             married = true;
                     }

                      //if(married==true)
                     //player.sendMessage("§a§lYou are already married");

                 for(PermissionAttachmentInfo permission : player.getEffectivePermissions()) {

                    if(permission.getPermission().contains("marrige.proposal.")&&married==false){

                        chat.setCancelled(true);
                        String playername = permission.getPermission().split("marrige.proposal.")[1];
                        Player p2 = Bukkit.getPlayer(playername);

                        if(!p2.equals(player)){

                        titleText(p2, 0, "§a§lThey answered....§a§l", "1", false);
                        titleText(p2, 40, "§a§lYES!!!", "3", false);


                        String gender = es.getUser(player.getPlayer())._getNickname().split(" ")[2];
                        String surname = es.getUser(p2.getPlayer())._getNickname().split(" ")[1];
                        String name = es.getUser(player.getPlayer())._getNickname().split(" ")[0];

                            es.getUser(player.getPlayer()).setNickname(ChatColor.translateAlternateColorCodes('&',(name + " " + surname + " " + gender)));
                            //es.getUser(p2.getPlayer()).setNickname(ChatColor.translateAlternateColorCodes('&',(name + " " + surname + " " + gender)));


                            //p2

                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p2.getName() + " set married." + player.getName() + "." + surname + " true");
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p2.getName() + " set married" + " true");

                        //player

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + chat.getPlayer().getName() + " set married." + p2.getName() + "." + surname + " true");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + chat.getPlayer().getName() + " set married" + " true");

                        for(Player onlineplayer : Bukkit.getOnlinePlayers()){

                            onlineplayer.sendMessage("§4§l❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
                            onlineplayer.sendMessage("§a§l"+ player.getName() + " §d§land " + "§a§l"+ p2.getName() + " §d§lhave gotten married!!");
                            onlineplayer.sendMessage("§4§l❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
                        }

                    }}}



            }else if(chat.getMessage().toLowerCase().equals("no")){

                 for(PermissionAttachmentInfo permission : player.getEffectivePermissions()){

                     //player.sendMessage(permission.getPermission());

                     if(permission.getPermission().contains("marrige.proposal.")){

                         String playername = permission.getPermission().split("marrige.proposal.")[1];

                         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + chat.getPlayer().getName() + " unset marrige.proposal." + playername + " false");
                 Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + chat.getPlayer().getName() + " set marrige.proposal." + playername + " false");

                     }

                     else if(permission.getPermission().contains("adopt.proposal.")){

                         String playername = permission.getPermission().split("adopt.proposal.")[1];

                         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + chat.getPlayer().getName() + " unset adopt.proposal." + playername + " false");
                         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + chat.getPlayer().getName() + " set adopt.proposal." + playername + " false");

                     }

             }}


             //END MHU FAMILIY


        }

        int clicked = 0;

        @EventHandler
        public void MobEvent(PlayerInteractAtEntityEvent m) {

            Player player = m.getPlayer();



            if(m.getRightClicked().getName().equals("Shady Trader")){

                clicked++;

                if(clicked==1){

                    player.sendMessage(" ");
                player.sendMessage("§7Hey " + player.getName() + " since i like you.. ");
                player.sendMessage("§7I'll give you a §c§lQuirk Key §750% §7off");
                    player.sendMessage("§7Aka §a§l$1000");
                    player.sendMessage(" ");

                ClickableChat(player, "/blackmarket quirkkeys", "/blackmarketActivate");


                mhu.mob = m.getRightClicked();
                mhu.PlayerRightClicked = player;

                }
            }



            if(m.getRightClicked().getName().equals("Hobo")) {

                clicked++;

                if (clicked == 1) {

                    player.sendMessage(" ");
                    player.sendMessage("§2§lExcuse me " + player.getName() + ".");
                    player.sendMessage("§2§lYou don't happen to have some spare change?");
                    player.sendMessage("§2§lI need 50$ for the buss you see..");
                    player.sendMessage(" ");

                    ClickableChat(player, "/blackmarket hobo", "/blackmarketActivate");


                    mhu.hobo = m.getRightClicked();

                    mhu.PlayerRightClicked = player;


                }
            }

            if(m.getRightClicked().getName().equals("Drugdealer")) {

                clicked++;

                if (clicked == 1) {

                    player.sendMessage(" ");
                    player.sendMessage("§f§lHey kid! ");
                    player.sendMessage("§f§lYou want to feel something you never felt before?");
                    player.sendMessage("§f§lSomthing that will boost your quirk immensely!");
                    player.sendMessage("§f§lI only ask §a§l$1500 §f§lfor this miracle of a drug..");
                    player.sendMessage("§f§lUh I mean medicine :D");
                    player.sendMessage(" ");

                    ClickableChat(player, "/blackmarket drugdealer", "/blackmarketActivate");


                    mhu.drugdealer = m.getRightClicked();

                    mhu.PlayerRightClicked = player;


                }
            }

            if(clicked==2){

                clicked=0;
            }

        }





        @EventHandler
        public void InventoryClickEvent(org.bukkit.event.inventory.InventoryClickEvent event)
        {
            Player player = (Player) event.getWhoClicked();

            if (event.getInventory().getTitle().equals("Friends")){

                if(event.isRightClick())
                {
                    event.setCancelled(true);
                    player.updateInventory();


                }else if(event.isLeftClick())
                {
                    event.setCancelled(true);
                    player.updateInventory();
                    player.closeInventory();
                    String enable = event.getCurrentItem().getItemMeta().getDisplayName();


                }
                if(event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                    event.setCancelled(true);
                }

                if(event.getClick().isKeyboardClick())
                {
                    event.setCancelled(true);
                }
            }

            return;


        }

        public boolean inRegion(Location loc)
        {
            WorldGuardPlugin guard = wg;
            com.sk89q.worldedit.Vector v = BukkitUtil.toVector(loc);
            RegionManager manager = guard.getRegionManager(loc.getWorld());
            ApplicableRegionSet set = manager.getApplicableRegions(v);
            return set.size() > 0;
        }


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


        @EventHandler
        public void ResourcePackChecker(PlayerResourcePackStatusEvent move) throws IOException {

            if(!move.getPlayer().hasPermission("mhu.texturepack.disabled")&&move.getStatus().equals(PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED)) {

                if(!mhu.surnameCheck.contains(move.getPlayer().getUniqueId())&&!checkFirstLoginElements(move.getPlayer())){

                    DelayedCommand(move.getPlayer(),10,"1", "warp tutorialStart " + move.getPlayer().getName());
                    loaded = true;
                }
            }
        }


        public int RandomNumber(){


            int randomChance = (int) (Math.random() * 16);


        return randomChance;}

                @EventHandler
        public void PlayerMove(PlayerMoveEvent move) throws InterruptedException, IOException {


                    ArrayList <String> mobs = new ArrayList<>(); // u.Reader("plugins/MHUBattles/", "BattleMobs", "");

                    mobs.add("Shoto");
                    mobs.add("Tamaki");
                    mobs.add("Denki");
                    mobs.add("Kurogiri");
                    mobs.add("Tomura");
                    mobs.add("Shiozaki");
                    mobs.add("Thirteen");
                    mobs.add("Nejire");
                    mobs.add("WarpingVillain");


                    int randomMobChoose = (int) (Math.random() * mobs.size());


                    try {
                        MMHero_Villain(move.getPlayer());
                    } catch (Exception e) {

                        move.getPlayer().sendMessage("Something wrong");

                        e.printStackTrace();

                    }


                    //CITIZENS

                    if (inRegion(move.getPlayer().getLocation()) == false && CitizensAroundPlayer(move.getPlayer()) < 3 && (move.getPlayer().getWorld().getName().equals("Oshode")) && !move.getPlayer().hasPermission("mhu.texturepack.disabled") && !(move.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR)

                    && !(move.getPlayer().getLocation().getY()>73.0)) {

                        Player sender = move.getPlayer();


                        Vector vec = sender.getLocation().toVector(); // get player's location vector
                        Vector dir = sender.getLocation().getDirection(); // get player's facing
                        vec = vec.add(dir.multiply(7)); // add player's direction * 3 to the location (getting ~3 blocks in front of player)
                        Location playerLoc1 = vec.toLocation(sender.getWorld());


                        int numberofCitizens = (int) (Math.random() * 1020);


                        io.lumine.xikage.mythicmobs.mobs.MythicMob m = mm.getMobManager().getMythicMob("Citizen" + numberofCitizens);
                        ;

                        int chance = (int) (Math.random() * 1);

                        int random = 0;

                        if (chance == 0)
                            random = (int) (Math.random() * -11);
                        else
                            random = (int) (Math.random() * 11);


                        Location meh = new Location(sender.getWorld(), playerLoc1.getX() + random + 4, sender.getLocation().getY(), playerLoc1.getZ() + random + 4);


                        Location loc = new Location(sender.getWorld(), 68, 480, 1000);


                        //Random for which mob to be chosen out of the list

                        int randomChance = RandomNumber();


                        if (randomChance != 15) {

                            try {
                                m = mm.getMobManager().getMythicMob("Citizen" + numberofCitizens);
                                ActiveMob mob = m.spawn(adapt(loc), 0);
                                mob.getEntity().getBukkitEntity().teleport(meh);
                                
                            } catch (Exception e) {

                                System.out.print("------------" + "\n" + "CAN*T GET THE MOB" + "\n" + "-----------------");

                            }

                        }


                        //Location playerLoc = new Location(sender.getWorld(),sender.getEyeLocation().getX()+random,sender.getLocation().getY(),sender.getEyeLocation().getZ()+random);


                        // convert back to location


                    }

                    //END CITIZENS

                    //MOBS

                    if (inRegion(move.getPlayer().getLocation()) == false && move.getPlayer().getWorld().getName().equals("Oshode") && !move.getPlayer().hasPermission("mhu.texturepack.disabled") && !(move.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR)) {

                        Player sender = move.getPlayer();

                        Vector vec = sender.getLocation().toVector(); // get player's location vector
                        Vector dir = sender.getLocation().getDirection(); // get player's facing
                        vec = vec.add(dir.multiply(7)); // add player's direction * 3 to the location (getting ~3 blocks in front of player)
                        Location playerLoc1 = vec.toLocation(sender.getWorld());


                        int numberofCitizens = (int) (Math.random() * 24);


                        io.lumine.xikage.mythicmobs.mobs.MythicMob m = mm.getMobManager().getMythicMob("Citizen" + numberofCitizens);
                        ;

                        int chance = (int) (Math.random() * 1);
                        int random = 0;

                        if (chance == 0)
                            random = (int) (Math.random() * -11);
                        else
                            random = (int) (Math.random() * 11);


                        Location meh = new Location(sender.getWorld(), playerLoc1.getX() + random + 4, sender.getLocation().getY(), playerLoc1.getZ() + random + 4);


                        Location loc = new Location(sender.getWorld(), 68, 480, 1000);


                        boolean shadydealer = false;
                        boolean drugdealer = false;
                        boolean hobo = false;
                        boolean mobtimer = false;

                        if (mhu.shadydealerwait.get(sender) != null)
                            shadydealer = mhu.shadydealerwait.get(sender);

                        if (mhu.drugdealerwait.get(sender) != null)
                            drugdealer = mhu.drugdealerwait.get(sender);

                        if (mhu.hobowait.get(sender) != null)
                            hobo = mhu.hobowait.get(sender);

                        if (mhu.mobtimer.get(sender) != null)
                            mobtimer = mhu.mobtimer.get(sender);

                        /*sender.sendMessage("  ");
                        sender.sendMessage("------");
                        sender.sendMessage("Shady Dealer : " + shadydealer);
                        sender.sendMessage("Drug Dealer : " + drugdealer);
                        sender.sendMessage("Hobo : " + hobo);
                        sender.sendMessage("Mob : " + mobtimer);
                        sender.sendMessage("------");
                        sender.sendMessage("  ");*/

                        //Random for which mob to be chosen out of the list

                        int randomChance = RandomNumber();

                        Player player = move.getPlayer();

                        //sender.sendMessage("Distance " + mhu.HeroDistance);


                        int blackmarketchance = (int) (Math.random() * 20);

                        int hobochance = (int) (Math.random() * 25);

                        int drugdealerchance = (int) (Math.random() * 31);

                        if (!sender.hasPermission("mhu.peaceful")) {

                            try {
                                if (mhu.MobsAroundPlayer.get(sender) >= 25) {

                                    //DO NOTHING just a test to see if the actual statment works

                                }
                            } catch (Exception e) {

                                mhu.MobsAroundPlayer.put(sender, 26);

                            }

                            if (randomChance == 15 && MMHero_Villain(sender) >= 25 && mobtimer == false) {

                                mhu.mobtimer.put(sender, true);

                                mhu.MobsAroundPlayer.remove(sender);

                                m = mm.getMobManager().getMythicMob((String) mobs.get(randomMobChoose));

                                ActiveMob mob = m.spawn(adapt(loc), 0);


                                if (mob.getFaction().equals("Hero"))
                                    sender.sendMessage("§f§lA §e§lHero §f§lspawned nearby!");
                                else if (mob.getFaction().equals("Villain"))
                                    sender.sendMessage("§f§lA §4§lVillain §f§lspawned nearby!");


                                mob.getEntity().getBukkitEntity().teleport(move.getPlayer().getLocation());

                                int time = 2300;

                                if (sender.hasPermission("mhu.grind"))
                                    time = 500;

                                TimerRandomSpawn(move.getPlayer(), time, "mobtimer", "mobtimer" + sender.getName());


                            } else if (MMHero_Villain(sender) >= 25 && CitizensAroundPlayer(move.getPlayer()) < 3 && shadydealer == false && drugdealer == false && hobo == false && drugdealerchance == 30 && mobtimer == false && MMAroundPlayer(sender) >= 1) {

                                mhu.drugdealerwait.put(sender, true);


                                TimerRandomSpawn(move.getPlayer(), 2500, "Drugdealer", "drugdealer" + sender.getName());

                                sender.sendMessage("§f§lA Drugdealer §f§lspawned nearby!\n" + "§c§lRight Click §f§lhim to uh say no?!");

                                m = mm.getMobManager().getMythicMob("Drugdealer");

                                ActiveMob mob = m.spawn(adapt(loc), 0);

                                mob.getEntity().getBukkitEntity().teleport(move.getPlayer().getLocation());


                            } else if (shadydealer == false && CitizensAroundPlayer(move.getPlayer()) < 3 && hobo == false && drugdealer == false && hobochance == 24 && mobtimer == false && MMAroundPlayer(sender) >= 1) {

                                mhu.hobowait.put(sender, true);

                                TimerRandomSpawn(move.getPlayer(), 2000, "hobo", "hobo" + sender.getName());

                                sender.sendMessage("§7§lA Hobo §f§lspawned nearby!\n" + "§c§lRight Click §f§lhim to help the poor guy out!");

                                m = mm.getMobManager().getMythicMob("Hobo");

                                ActiveMob mob = m.spawn(adapt(loc), 0);

                                mob.getEntity().getBukkitEntity().teleport(move.getPlayer().getLocation());


                            } else if (shadydealer == false && CitizensAroundPlayer(move.getPlayer()) < 3 && drugdealer == false && hobo == false && blackmarketchance == 19 && mobtimer == false && MMAroundPlayer(sender) >= 1) {

                                mhu.shadydealerwait.put(sender, true);

                                TimerRandomSpawn(move.getPlayer(), 2500, "Shady Trader", "shadytrader" + sender.getName());

                                sender.sendMessage("§f§lA §7§lShady Trader §f§lspawned nearby!\n" + "§c§lRight Click §f§lhim to see what he wants!");

                                m = mm.getMobManager().getMythicMob("BlackMarket");

                                ActiveMob mob = m.spawn(adapt(loc), 0);

                                mob.getEntity().getBukkitEntity().teleport(move.getPlayer().getLocation());


                            }
                        }

                        //END MOBS


                        //Location playerLoc = new Location(sender.getWorld(),sender.getEyeLocation().getX()+random,sender.getLocation().getY(),sender.getEyeLocation().getZ()+random);


                        // convert back to location





                    /*}if (move.getPlayer().hasPermission("BrainWash")) {

                        Player p1 = move.getPlayer();

                        int v = 0;

                        int sheep1 = 0;

                        for (Player sheep : Bukkit.getOnlinePlayers()) {

                            if (!sheep.equals(p1)) {

                                sheep1++;
                                sheep.teleport(getLoc(2 * sheep1));
                                sheep.setGlowing(true);
                                mhu.BrainWashedPlayer = sheep;
                            }


                        }*/

                        /*for(ActiveMob mob : mm.getMobManager().getActiveMobs()){

                            v++;

                            Entity mob1 = mob.getEntity().getBukkitEntity();

                            mob1.teleport(getLoc(2*v));
                            mob1.setGlowing(true);

                /*else if(v>=mm.getMobManager().getActiveMobs().size()/2)
                    mob1.teleport(getLoc(-2*v));



                            mob1.setVelocity(naruto.getVelocity());


                        }*/


                    }


                    if (loaded = true && mhu.surnameCheck.contains(move.getPlayer().getUniqueId()) == false && !checkFirstLoginElements(move.getPlayer()) && !mhu.genderCheck.contains(move.getPlayer().getUniqueId())) {


                        DelayedCommand(move.getPlayer(), 10, "1", "warp tutorialStart " + move.getPlayer().getName());

                        Text(move.getPlayer(), 20, "§f§lYAAWN", "2");
                        Text(move.getPlayer(), 40, "§f§lDo /surname", "3");
                        Text(move.getPlayer(), 60, "§f§lTo write your surname.", "4");


                    }

                    if (mhu.surnameCheck.contains(move.getPlayer().getUniqueId()) && !checkFirstLoginElements(move.getPlayer())) {


                        if (!mhu.genderCheck.contains(move.getPlayer().getUniqueId())) {

                            DelayedCommandServer(mhu.getServer().getConsoleSender(), 20, "5", "c forcecast " + move.getPlayer().getName() + " GenderMenu");


                        }

                    }


                    if (move.getPlayer().getName().equals("_MilesMorales")) {


                        move.getPlayer().sendMessage("https://i.ytimg.com/an_webp/dQw4w9WgXcQ/mqdefault_6s.webp?du=3000&sqp=CNDY7_4F&rs=AOn4CLBcfmgQ6Id4A8LvC3W7Zmm8tWHwxw");


                    }

                    Player p3 = Bukkit.getPlayer("Eliejr");

                    Player p4 = Bukkit.getPlayer("lecter67");

                    Player naruto = Bukkit.getPlayer("Naruto");

                    ArrayList<Player> Players = new ArrayList();

                    for (Player player : Bukkit.getOnlinePlayers()) {


                        if (!player.equals(naruto))
                            Players.add(player);


                    }


                }




        @EventHandler
        public void LoginEvent(PlayerLoginEvent killer1){

            Player killer = killer1.getPlayer();

            killer.setGlowing(false);

            if(killer.getName().contains("*")){


                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"warp SportsArena " + killer.getName());


            }


        }




        public Location getLoc(int distance){


            Player naruto = Bukkit.getPlayer("Naruto");


            Location narutoloc = new Location(naruto.getWorld(),naruto.getEyeLocation().getX(),naruto.getLocation().getY(),naruto.getEyeLocation().getZ()+distance,naruto.getEyeLocation().getYaw(),naruto.getEyeLocation().getPitch());

        return narutoloc;}



        @EventHandler
        public void LeaveEvent(PlayerQuitEvent killer1){

            System.out.print("LeaveEventActivated");

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




        @EventHandler
        public void CancelHunger(FoodLevelChangeEvent event) throws IOException {

            event.setCancelled(true);}

        @EventHandler
        public void AddExp(MythicMobDeathEvent d) throws IOException {


            Player player = (Player) d.getKiller();


            String mobname = d.getMobType().getInternalName();


            if(mobname.contains("Dummy")){


            }else if(d.getMob().getFaction().contains("Hero")||d.getMob().getFaction().contains("Villain")) {

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"gl addexp " + player.getName() + " " +  10);

                NearbyFriendsExp(player,50);

                for (int i = 0; i<friends.size(); i++){

                    if(!player.getWorld().getName().toLowerCase().equals("oshode")){

                /*Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"warp spawn " +  Bukkit.getPlayer(friends.get(i).toString()).getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"warp spawn " +  player.getName());*/


                    }}

                player.sendMessage("§f§l[§4§lMHU§c-§f§lBattles§f§l]§f§l You defeated §a§l" + mobname);

                int exp = 10;
                String mobfilename = "Bob";



                try{
                    exp = parseInt(util1.Reader("MHU_GlobalScore/MythicMobs","MobExp",mobname+": ").get(0));
                    player.sendMessage("Exp: " + exp);} catch (NumberFormatException e) {
                    mobfilename = util1.Reader("MHU_GlobalScore/MythicMobs","MobExp",mobname).get(0).split(":")[0];
                } catch (IOException e) {
                    e.printStackTrace();
                }


                if(mobname.equals(mobfilename)){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"gl addexp " + player.getName() + " " + exp+1);
                   }
                else{
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"gl addexp " + player.getName() + " " +  10);
                }




                double mobExp = 10;


                if (!NearbyFriendsExp(player,20).isEmpty()) {


                    NearbyFriendsExp(player, 20);


                    for (int i = 0; i<friends.size(); i++){
                        Bukkit.getPlayer(friends.get(i).toString()).sendMessage("§f§l[§4§lMHU§c-§f§lBattles§f§l] §a§l" + player.getName() + "§f§l defeated §a§l" + mobname + "\n" + "§f§l and you get §a§l" + mobExp / 2 + "§f§l exp for helping / being emotional support");

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gl addexp " + Bukkit.getPlayer(friends.get(i).toString()).getName() + " " + mobExp / 2);
                    }
                }
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

        public String NearbyFriendsExp(Player player, int radius) throws FileNotFoundException {

            friendList.clear();


            listoffriends(player);

            Location user = player.getLocation();


            for(int i = 0; i<friendList.size(); i++){

                if(Bukkit.getOfflinePlayer(friendList.get(i)).isOnline()&&Bukkit.getPlayer(friendList.get(i)).getWorld().equals(user.getWorld())){

                    if(Bukkit.getPlayer(friendList.get(i)).getLocation().distance(user) < radius){


                        friends.clear();

                        friends.add(Bukkit.getPlayer(friendList.get(i)).getName().toString());

                        return Bukkit.getPlayer(friendList.get(i)).getName();


                    }

                    else {


                        return "";}
                }

            } return "";}


        public double onlinefactionmembers(){
            int i = 0;
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {

                //if (player.hasPermission("expshare.lvl1")){

                i++;//}

            }
            return i;
        }


    }
