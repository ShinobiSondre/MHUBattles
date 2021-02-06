package com.company.util;


import com.company.MHUBattles;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;



public class Util {

    public HashMap<Player, Player> players = new HashMap<>();
    public HashMap<Player,Timer> timer = new HashMap<>();
    public Timer t = new Timer();
    public Map<String, Integer> taskID = new HashMap<String, Integer>();
    public boolean stop = false;




    private static final MHUBattles mhu;

    static {
        mhu = (MHUBattles) Bukkit.getServer().getPluginManager().getPlugin("MHUBattles");
    }



    //Text


    public TitleManagerAPI titleManagerAPI = (TitleManagerAPI) Bukkit.getServer().getPluginManager().getPlugin("TitleManager");
    public MythicMobs mm = (MythicMobs) Bukkit.getServer().getPluginManager().getPlugin("MythicMobs");
    public HashMap<String,Integer>textwait = new HashMap<>();



    //QUIRK TUTORIAL






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




    public void DelayedCommand(final Player sender, long ticks,String indentifier){


        textwait.put(sender.getName()+indentifier,0);

        final boolean[] check1 = {false};


        final int tid = mhu.getServer().getScheduler().scheduleSyncRepeatingTask(mhu, new Runnable() {
            public void run() {

                textwait.put(sender.getName()+indentifier,textwait.get(sender.getName()+indentifier)+1);


                //END
                if (textwait.get(sender.getName()+indentifier)==2) {





                    TextendTask(sender.getName()+indentifier);

                }
            }
        }, 0, ticks); //schedule task with the ticks specified in the arguments

        taskID.put(sender.getName()+indentifier, tid); //put the player in a hashmap


    }


    public void DelayedCommand(Player sender, long ticks, String indentifier, String commandserver){


        textwait.put(sender.getName()+indentifier,0);

        final boolean[] check1 = {false};


        final int tid = mhu.getServer().getScheduler().scheduleSyncRepeatingTask(mhu, new Runnable() {
            public void run() {

                //END
                if (textwait.get(sender.getName()+indentifier)==2) {

                    CommandSender cmdsender = Bukkit.getConsoleSender();

                    Bukkit.dispatchCommand(cmdsender,commandserver);



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



    public void giveDonatorRanks(String group) throws IOException {

        ArrayList<String> doublesCheck = new ArrayList();

        for(String info : Reader("plugins", "Donators","Plus")) {


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


    public ArrayList<String> Reader(String filepath, String filename, String keyword) throws IOException {


        ArrayList <String> data = new ArrayList();

        try {
            File Filepath = new File(filepath + "/" + filename + ".txt");

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

                    if(keyword!="")
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


            //END


            reader.close();

        } finally {

        }

        return data;}

    public void WriterYML(String filepath, String filename, String input) throws IOException {


        // "plugins/MHU_GlobalScore/PlayerData/" + uuid + ".txt"  Basically filepath then the file u generate at the end which is uuid + ".txt"

        File file = new File(filepath + "/" + filename + ".yml");


        //Creates a new file if it doesn't exist granted that the folder exists
        //You can make it auto generate the folder, but if ppl somehow get a hold of the plugin it won't work without them knowing to create a folder
        //Thats why I like to make it not autogenerate the folder

        file.createNewFile();


        //append set to false rewrites the file basically clearing it and writing data again if you set append to true you just add stuff to the file (Which is not recommended).

        FileWriter objectName = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(objectName);


        //Try and Catch works like this: Try something if it doesn't work then do this thing in Catch

        try {

            pw.write(input);

            System.out.print("-------------------");
            System.out.print("Wrote Files to: " + filepath + "/" + filename + ".txt");
            System.out.print("-------------------");

        }catch (Exception e){

            pw.write("Filepath / Folder / Filename is wrong!!!");

        }

        //everything after this point just closes stuff and makes sure it doesnt keep anything open / writing

        pw.flush();
        pw.close();

        objectName.close();





    }




    public void Writer(String filepath, String filename, String input) throws IOException {


        // "plugins/MHU_GlobalScore/PlayerData/" + uuid + ".txt"  Basically filepath then the file u generate at the end which is uuid + ".txt"

        File file = new File(filepath + "/" + filename + ".txt");

        //Creates a new file if it doesn't exist granted that the folder exists
        //You can make it auto generate the folder, but if ppl somehow get a hold of the plugin it won't work without them knowing to create a folder
        //Thats why I like to make it not autogenerate the folder

        file.createNewFile();


        //append set to false rewrites the file basically clearing it and writing data again if you set append to true you just add stuff to the file (Which is not recommended).

        FileWriter objectName = new FileWriter(file, false);
        PrintWriter pw = new PrintWriter(objectName);


        //Try and Catch works like this: Try something if it doesn't work then do this thing in Catch

        try {

            pw.write(input);

            System.out.print("-------------------");
            System.out.print("Wrote Files to: " + filepath + "/" + filename + ".txt");
            System.out.print("-------------------");

        }catch (Exception e){

            pw.write("Filepath / Folder / Filename is wrong!!!");

        }

        //everything after this point just closes stuff and makes sure it doesnt keep anything open / writing

        pw.flush();
        pw.close();

        objectName.close();





    }


}


