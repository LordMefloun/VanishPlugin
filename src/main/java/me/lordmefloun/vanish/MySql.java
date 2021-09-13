package me.lordmefloun.vanish;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.*;
import java.util.UUID;

public class MySql {


    Vanish plugin;

    private Connection connection;
    public String host, database, username, password, table;
    public int port;


    public MySql(Vanish plugin){

        this.plugin = plugin;

    }


    public void mysqlSetup() {
        host = plugin.getConfig().getString("database.host");
        port = plugin.getConfig().getInt("database.port");
        database = plugin.getConfig().getString("database.database");
        username = plugin.getConfig().getString("database.user");
        password = plugin.getConfig().getString("database.password");
        table = plugin.getConfig().getString("database.table");


        try{
            synchronized (this){
                if (getConnection() != null && !getConnection().isClosed() ) {
                    return;
                }

                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" +
                        this.port + "/" + this.database, this.username, this.password));
                Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "MySQL connection successfull");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        try {
            PreparedStatement statement = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `"+ database +"`.`" + table + "` ( `id` INT(225) NOT NULL AUTO_INCREMENT , `player` VARCHAR(225) NOT NULL , `vanished` BOOLEAN NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean getVanishState(UUID uuid) {
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + table + " WHERE player=?")){
            statement.setString(1, uuid.toString());


            ResultSet result = statement.executeQuery();

            if (result.next()){
                return result.getBoolean("vanished");

            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean vanishStateExists(UUID uuid){
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + table + " WHERE player=?")){
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()){
                return true;
            }
            else{
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



    public void createVanishState(UUID uuid, boolean state){
        try (PreparedStatement statement = getConnection().prepareStatement("INSERT INTO " + table + " (player, vanished) VALUES (?,?)")){
            if(!vanishStateExists(uuid)){
                statement.setString(1, uuid.toString());
                statement.setBoolean(2, state);

                statement.executeUpdate();
            }
            else {
                System.out.println("Cannot create new player in mysql because already exists");
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setVanishState(UUID uuid, boolean state){
        try (PreparedStatement statement = getConnection().prepareStatement("UPDATE " + table + " SET vanished=? WHERE player=?")){

            if(vanishStateExists(uuid)) {

                statement.setBoolean(1, state);
                statement.setString(2, uuid.toString());


                statement.executeUpdate();
            }
            else{
                createVanishState(uuid, state);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }






}
