import java.sql.*;
import java.util.*;

public class TimelisteDb {
    private Connection connection;

    public TimelisteDb(Connection connection) {
        this.connection = connection;
    }

    public void printTimelister() throws SQLException {
      Statement stmt = null;
      String spoerring = "select timelistenr, status, beskrivelse from tliste;";
      stmt = connection.createStatement();
      ResultSet result = stmt.executeQuery(spoerring);
      while(result.next()){
        int listenr = result.getInt("timelistenr");
        String status = result.getString("status");
        String beskrivelse = result.getString("beskrivelse");
        System.out.println("Timelistenr: " + listenr + "\nstatus: " + status
         + "\nBeskrivelse: " + beskrivelse);
        System.out.println();
      }
    }

    public void printTimelistelinjer(String timelisteNr) throws SQLException {
      Statement stmt = null;
      String spoerring = "select linjenr, timeantall, beskrivelse from tlistelinje where timelistenr = " + timelisteNr + ";";
      stmt = connection.createStatement();
      ResultSet result = stmt.executeQuery(spoerring);
      int counter = 0;
      while(result.next()){
        int linjenr = result.getInt("linjenr");
        int timeantall = result.getInt("timeantall");
        String beskrivelse = result.getString("beskrivelse");
        counter += timeantall;
        System.out.println("linjenummer:" + linjenr + "\ntimeantall:" + timeantall
         + "\nkumulativt_timeantall: " + counter);
         System.out.println();
      }
    }

    public double medianTimeantall(int timelisteNr) throws SQLException {
      Statement stmt = null;
      String spoerring = "select timeantall from tlistelinje where timelistenr = " + timelisteNr + " order by timeantall;";
      stmt = connection.createStatement();
      ResultSet result = stmt.executeQuery(spoerring);
      List<Integer> list = new ArrayList<Integer>();
      while(result.next()){
        int timeantall = result.getInt("timeantall");
        list.add(timeantall);

      }
      return median(list);
    }

    public void settInnTimelistelinje(int timelisteNr, int antallTimer, String beskrivelse) throws SQLException {
      PreparedStatement stmt = null;
      String spoerring = String.join(" ",
                                  "select max(linjenr) as linje",
                                  "from tlistelinje",
                                  "where timelistenr = ?");
      stmt = connection.prepareStatement(spoerring);
      stmt.setInt(1, timelisteNr);
      ResultSet result = stmt.executeQuery();
      result.next();
      int linje = result.getInt("linje");
      linje++;

      spoerring = "insert into tlistelinje (timelistenr, linjenr, timeantall, beskrivelse) values (?,?,?,?);";
      stmt = connection.prepareStatement(spoerring);
      stmt.setInt(1, timelisteNr);
      stmt.setInt(2, linje);
      stmt.setInt(3, antallTimer);
      stmt.setString(4, "'" + beskrivelse + "'");
      stmt.executeUpdate();
    }

    public void regnUtKumulativtTimeantall(int timelisteNr) throws SQLException {
      Statement stmt = null;
      String spoerring = "select linjenr, timeantall from tlistelinje where timelistenr = " + timelisteNr + ";";
      stmt = connection.createStatement();
      ResultSet result = stmt.executeQuery(spoerring);
      int counter = 0;
      while(result.next()){
        int linjenr = result.getInt("linjenr");
        int timeantall = result.getInt("timeantall");
        counter += timeantall;
        PreparedStatement stmt2 = null;
        spoerring = "update tlistelinje set kumulativt_timeantall = ? where timelistenr = ? and linjenr = ?";
        stmt2 = connection.prepareStatement(spoerring);
        stmt2.setInt(1, counter);
        stmt2.setInt(2, timelisteNr);
        stmt2.setInt(3, linjenr);
        stmt2.executeUpdate();


      }
    }
    /**
     * Hjelpemetode som regner ut medianverdien i en SORTERT liste. Kan slettes om du ikke har bruk for den.
     * @param list Tar inn en sortert liste av integers (f.eks. ArrayList, LinkedList osv)
     * @return medianverdien til denne listen
     */
    private double median(List<Integer> list) {
        int length = list.size();
        if (length % 2 == 0) {
            return (list.get(length / 2) + list.get(length / 2 - 1)) / 2.0;
        } else {
            return list.get((length - 1) / 2);
        }
    }
}
