package com.fabulousapps;

import java.sql.*;
import java.util.*;

import java.sql.ResultSet;

public class JabberServer {

	private static String dbcommand = "jdbc:postgresql://127.0.0.1:5432/JABBER";
	private static String db = "postgres";
	private static String pw = "05092015";
	private static Connection c;

    static {
        try {
            c = DriverManager.getConnection(dbcommand,db,pw);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static Connection conn;

    public static Connection getConnection() {
        return conn;
    }

    public static void main(String[] args) {

    }



    public static  ArrayList<String > getAllUserIds () {
        ArrayList<String > uids = new ArrayList<>();
        try{
        Statement stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery("select * from jabberuser");
        while (rs.next()) {

            int id2 = rs.getInt("userid");
            String id4 = rs.getString("username");
            String id3 = rs.getString("emailadd");
            uids.add(String.valueOf(id2));
            uids.add(id4);
            uids.add(id3);



        }

    } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);

    }
        return  uids;
    }
    public static ArrayList<String> getFollowerUserIDs(int userid)  {
        ArrayList<String> ids = new ArrayList<>();
        try {


            Statement stmt = c.createStatement();
            int arg = userid + 1;
            ResultSet rs = stmt.executeQuery("select useridA from follows where useridB = '"+ userid + "'");
            int id2 = 0;
            while (rs.next()) {

                id2 = rs.getInt("useridA");
                ids.add(String.valueOf(id2));
                System.out.println(id2);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);

        }
        return ids;

    }

    public static ArrayList<String> getFollowingUserIDs(int userid) {
        ArrayList<String > fuserids = new ArrayList<>();
        try {
            Statement stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("select useridB from follows where useridA = '"+ userid + "'");
            while (rs.next()) {

                int id = rs.getInt("useridB");
                fuserids.add(String.valueOf(id));

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);

        }
        return fuserids;
        /*
         * Add your code to this method here.
         * Remove the 'return null' statement and add your own return statement.
         */


    }

    public static ArrayList<ArrayList<String>> getMutualFollowUserIDs() {



        ArrayList<ArrayList<String>> mFollowIDs = new ArrayList<>();
        try {
            ArrayList<String> mIDS = null;

            Statement stmt = c.createStatement();

            ResultSet rsl = stmt.executeQuery("select  userida , useridB FROM follows as Q where Q.useridB in (select useridA from follows where useridB = Q.userida)");
            while (rsl.next()) {
                mIDS = new ArrayList<>();
                int idA = rsl.getInt("useridA");
                int idB = rsl.getInt("useridB");
                mIDS.add(String.valueOf(idA));
                mIDS.add(String.valueOf(idB));
                mFollowIDs.add(mIDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);

        }
        return mFollowIDs;
    }
//33
    public static ArrayList<ArrayList<String>> getLikesOfUser(int userid) {
        ArrayList<ArrayList<String>> jabcontent = new ArrayList<>();
        try {
            ArrayList<String> likes = new ArrayList<>();

            Statement stmt = c.createStatement();

            ResultSet rsl = stmt.executeQuery("select jabid, userid from likes where userid = '"+ userid + "'");
            while (rsl.next()) {

                  int id2 = rsl.getInt("jabid");
                  int uid = rsl.getInt("userid");
                  String username = getUserByUserId(uid);
                  String jabtext = getJabtextByJabId(id2);
                  likes.add(username);
                  likes.add(jabtext);
                  jabcontent.add(likes);




            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);

        }
        return jabcontent;
        /*
         * Add your code to this method here.
         * Remove the 'return null' statement and add your own return statement.
         */


    }

    public static ArrayList<ArrayList<String>> getTimelineOfUser(int userid) {
        ArrayList<ArrayList<String>> timeline = new ArrayList<>();
        /*
         * Add your code to this method here.
         * Remove the 'return null' statement and add your own return statement.
         */
        try {
            ArrayList<String> partoftimeline = new ArrayList<>();
            Statement stmt = c.createStatement();
            int arg = userid + 1;
            ResultSet rs = stmt.executeQuery("select jabtext from jab where userid = "+ userid);

            while (rs.next()) {
                String id2 = rs.getString("jabtext");
                String username = getUserByUserId(userid);
                partoftimeline.add(username);
                partoftimeline.add(id2);
                timeline.add(partoftimeline);

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);

        }
        return timeline;

    }

    public static void addJab(int jabid, Integer userid, String jabtext) {

        try {
            Statement stmt = c.createStatement();

            stmt.executeUpdate("insert into jab (jabid, userid, jabtext)" +
                    "values (" + jabid + "," + userid + ", '" + jabtext + "');");


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);

        }
    }

    public static void addUser(String username, String emailadd) {

        try {
            Statement stmt = c.createStatement();
            Random random = new Random();
            stmt.executeUpdate("insert into jabberuser (userid, username, emailadd)" +
                    "values (" + random.nextInt(50) + ",'" + username + "', '" + emailadd + "');");


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);

        }
    }

    public static void addFollower(int userida, int useridb) {
        try {
            Statement stmt = c.createStatement();
            Random random = new Random();
            stmt.executeUpdate("insert into follows (useridA, useridB)" +
                    "values (" + userida + ", " + useridb + ");");


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);

        }

    }

    public static void addLike(int userid, int jabid) {
        try {
            Statement stmt = c.createStatement();
            Random random = new Random();
            stmt.executeUpdate("insert into likes (userid, jabid)" +
                    "values (" + userid + ", " + jabid + ");");


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);

        }
    }

    public static ArrayList<String> getUsersWithMostFollowers() {
        ArrayList<String> mFollowers = new ArrayList<>();
        try {

            Statement stmt = c.createStatement();


            ResultSet rs = stmt.executeQuery("SELECT useridB, COUNT(*) FROM follows  GROUP BY useridB HAVING  COUNT(*) = (SELECT MAX(Q.C) FROM (SELECT COUNT(*) AS C FROM follows GROUP BY useridB) AS Q)");



            while (rs.next()) {
                String id2 = rs.getString("useridB");


                mFollowers.add(id2);


            }


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);

        }
        return mFollowers;

    }



    public static void connectToDatabase() {

        try {
            conn = DriverManager.getConnection(dbcommand,db,pw);

        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static String getUserByUserId(int userid) {

        try {
            Statement stmt = c.createStatement();
            int arg = userid + 1;
            ResultSet rs = stmt.executeQuery("select username from jabberuser where userid = "+ userid);

            rs.next();
            String id2 = rs.getString("username");

            return id2;


        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getJabtextByJabId(int jabid) {

        try {
            Statement stmt = c.createStatement();
            int arg = jabid + 1;
            ResultSet rs = stmt.executeQuery("select jabtext from jab where jabid = "+ jabid);

            rs.next();
            String id2 = rs.getString("jabtext");

            return id2;


        }catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return null;
    }

    private static void print2(ArrayList<ArrayList<String>> list) {

        for (ArrayList<String> s: list) {
            print1(s);
            System.out.println();
        }
    }


    private static void print1(ArrayList<String> list) {

        for (String s: list) {
            System.out.print(s + " ");
        }
    }

    public void resetDatabase() {

    }
}
