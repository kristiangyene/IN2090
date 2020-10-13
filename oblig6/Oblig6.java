import java.sql.SQLException;
import java.util.Scanner;

public class Oblig6 {
    public static void main(String[] args) {
        // Sett inn brukernavnet ditt nedenfor
        Tilkobling t = new Tilkobling("kristsgy", "kristsgy");
        TimelisteDb db = new TimelisteDb(t.getConnection());

        try {
            System.out.println("Alle timelister:");
            db.printTimelister();

            Scanner scanner = new Scanner(System.in);
            System.out.println("\nVelg en timeliste:");
            System.out.print("Timelistenr: ");
            String timelisteNr = scanner.nextLine();
            db.printTimelistelinjer(timelisteNr);

            System.out.printf("Median-timeantall for timeliste %d: ", timelisteNr);
            double medianTimeantall = db.medianTimeantall(Integer.parseInt(timelisteNr));
            System.out.println(medianTimeantall);

            System.out.println("\nSett inn timelistelinje [trykk enter uten verdi for aa hoppe over innsetting]:");
            try {
                System.out.print("Antall timer: ");
                int antallTimer = Integer.parseInt(scanner.nextLine());
                System.out.print("Beskrivelse: ");
                String beskrivelse = scanner.nextLine();
                db.settInnTimelistelinje(Integer.parseInt(timelisteNr), antallTimer, beskrivelse);

            } catch (NumberFormatException e) {
                System.out.println("Hopper over innsetting av ny timeliste...");
            }

            db.regnUtKumulativtTimeantall(Integer.parseInt(timelisteNr));

            System.out.println("\nTimelistelinjer med oppdatert kumulativt timeantall:");
            db.printTimelistelinjer(timelisteNr);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
