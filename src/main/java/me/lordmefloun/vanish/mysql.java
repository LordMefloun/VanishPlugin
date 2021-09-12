package me.lordmefloun.vanish;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class mysql {

    Vanish plugin = Vanish.getPlugin(Vanish.class);

    public boolean getVanishState(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE player=?");
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

    public boolean VanishStateExists(UUID uuid){
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE player=?");
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
        try {
            if(!VanishStateExists(uuid)){
                PreparedStatement statement = plugin.getConnection().prepareStatement("INSERT INTO " + plugin.table + " (player, vanished) VALUES (?,?)");
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
        try {

            if(VanishStateExists(uuid)) {
                PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE " + plugin.table + " SET vanished=? WHERE player=?");

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

}
