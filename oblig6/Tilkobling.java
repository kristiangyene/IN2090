import java.awt.Window;
import java.awt.event.ActionEvent;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.*;

public class Tilkobling {
    // Om du installerer PostgreSQL paa egen maskin, kan du bruke "jdbc:postgresql://127.0.0.1/"
    private String databaseServer = "jdbc:postgresql://dbpg-ifi-kurs.uio.no/";
    private String url;
    private String brukernavn;
    private char[] passord;

    private Connection connection = null;

    public Tilkobling(String databaseNavn, String brukernavn) {
        if (databaseNavn.equals("brukernavn") && brukernavn.equals("brukernavn")) {
            System.err.println("Feil: Bytt ut kristsgy med brukernavnet ditt i Oblig6.java");
            System.exit(1);
        }

        this.url = databaseServer + databaseNavn;
        this.brukernavn = brukernavn;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Finner ikke PostgreSQL-driveren! Har du husket aa inkludere .jar-filen?");
            e.printStackTrace();
            System.exit(2);
        }

        // Gir private char[] passord en verdi
        passwordPrompt();
    }

    public Connection getConnection() {
        try {
            System.out.print("Kobler til... ");
            this.connection = DriverManager.getConnection(url, brukernavn, new String(passord));
            System.out.println("Tilkoblet\n");
        } catch (SQLException e) {
            System.err.println("\nKunne ikke koble til!");
            e.printStackTrace();
            System.exit(3);
        }
        return this.connection;
    }

    private void passwordPrompt() {
        if (System.console() != null) {
            System.out.print("Passord: ");
            passord = System.console().readPassword();
        } else {
            // Med en IDE som IntelliJ eller Eclipse, kan ikke passordet skrives rett inn i terminalen
            // Vi har derfor en fallback til Swing som gir et nytt vindu hvor passordet kan skrives inn
            JDialog dialog = new JDialog((Window)null, "Skriv inn passord");
            JPasswordField passwordField = new JPasswordField(20);
            JButton button = new JButton("OK");

            JPanel panel = new JPanel();
            dialog.add(panel);
            panel.add(passwordField);
            panel.add(button);

            Action setPassword = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    passord = passwordField.getPassword();
                    dialog.dispose();
                }
            };
            button.addActionListener(setPassword);
            passwordField.addActionListener(setPassword);

            dialog.setModal(true);
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }
    }
}
