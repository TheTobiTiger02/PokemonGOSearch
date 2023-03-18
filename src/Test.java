
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class Test {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException {

        Connection con1;
        PreparedStatement insert;
        //Class.forName("com.mysql.jdbc.Driver");
        con1 = DriverManager.getConnection("jdbc:mysql://sql7.freesqldatabase.com/sql7606827", "sql7606827", "uAPhstaBJb");
        //insert = con1.prepareStatement("insert into Pokemon(number,name)values(1,?)");
        //insert.setString(1, "Bulbasaur");
        //insert.executeUpdate();
        insert = con1.prepareStatement("select * from pokemon ORDER BY number DESC");
        ResultSet rs = insert.executeQuery();
        ResultSetMetaData rss = rs.getMetaData();
        int c = rss.getColumnCount();



        while(rs.next()){

            String s = "";

            for(int a = 1; a < c; a++){
                s += rs.getString("number") + " ";
                s += rs.getString("name");

            }
            System.out.println(s);
        }



    }
}
