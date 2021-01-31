package com.company;

import com.company.commands.BattleCMD;
import com.company.events.BattleEvents;
import com.company.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public final class MHUBattles extends JavaPlugin {
    Logger logger;
    public boolean stop = true;

    public boolean fade = false;
    public int fadetime = 5;
    public int intensity = 1;
    public int minhearts = -1;

    public int random = 0;

    public int randomSpawnTest = 0;
    public int HeroDistance = 30;
    public int MMDistance = 30;
    public int randomSpawnTest1 = 0;

    public Player BrainWashedPlayer = null;


    //MHURANDOMSPAWN BlackMarket

    public Entity mob = null;
    public Entity hobo = null;
    public Entity drugdealer = null;

    public HashMap<Player,Boolean> drugdealerwait = new HashMap<>();
    public HashMap<Player,Boolean> hobowait = new HashMap<>();
    public HashMap<Player,Boolean> shadydealerwait = new HashMap<>();

    public HashMap<Player,Boolean> mobtimer = new HashMap<>();

    public boolean generelltimer = false;

    public HashMap<Player,Integer> MobsAroundPlayer = new HashMap<>();

    public Player PlayerRightClicked = null;


    //MHUFAMILIY

    public ArrayList<UUID> genderCheck = new ArrayList();

    public ArrayList<UUID> surnameCheck = new ArrayList();


    @Override
    public void onEnable() {
        // Plugin startup logic
        logger = Bukkit.getLogger();
        logger.info( "\n" + "\n" + "\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" + "--------------------------------------" + "\n" + "MHU-Battles" + "\n" + "--------------------------------------"

                + "\n" + "\n" + "\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n");


        logger.info("Registering commands!");
        commandLoader();

        logger.info("Registering BattleEvents");
            eventLoader();}



    public void eventLoader() {

        this.getServer().getPluginManager().registerEvents(new BattleEvents(), this);
    }

    public void commandLoader() {

        this.getCommand("mmbattle").setExecutor(new BattleCMD());
        this.getCommand("BattleEnd").setExecutor(new BattleCMD());
        this.getCommand("setpoint").setExecutor(new BattleCMD());
        this.getCommand("setarena").setExecutor(new BattleCMD());
        this.getCommand("arenalist").setExecutor(new BattleCMD());
        this.getCommand("randomspawner").setExecutor(new BattleCMD());
        this.getCommand("addmob").setExecutor(new BattleCMD());
        this.getCommand("moblist").setExecutor(new BattleCMD());
        this.getCommand("friends").setExecutor(new BattleCMD());
        this.getCommand("addfriend").setExecutor(new BattleCMD());
        this.getCommand("removefriend").setExecutor(new BattleCMD());
        this.getCommand("accept").setExecutor(new BattleCMD());
        this.getCommand("decline").setExecutor(new BattleCMD());
        this.getCommand("check").setExecutor(new BattleCMD());
        this.getCommand("mhu").setExecutor(new BattleCMD());
        this.getCommand("updatemob").setExecutor(new BattleCMD());
        this.getCommand("mhuteleport").setExecutor(new BattleCMD());
        this.getCommand("marry").setExecutor(new BattleCMD());
        this.getCommand("surname").setExecutor(new BattleCMD());
        this.getCommand("male").setExecutor(new BattleCMD());
        this.getCommand("female").setExecutor(new BattleCMD());
        this.getCommand("adopt").setExecutor(new BattleCMD());
        this.getCommand("blackmarket").setExecutor(new BattleCMD());
        this.getCommand("robstart").setExecutor(new BattleCMD());
        this.getCommand("blackmarketactivate").setExecutor(new BattleCMD());
        this.getCommand("divorce").setExecutor(new BattleCMD());
        this.getCommand("gdr").setExecutor(new BattleCMD());
        this.getCommand("autobind").setExecutor(new BattleCMD());
        this.getCommand("mobskin").setExecutor(new BattleCMD());}


    @Override
    public void onDisable() {




        // Do something on restarts or Stops

        System.out.print("Restart Event Activated");

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {



        }





    }
}