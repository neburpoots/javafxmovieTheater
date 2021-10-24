package nl.inholland.javafx.views.windows.components.pdftemplates;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import nl.inholland.javafx.models.Showing;
import nl.inholland.javafx.models.Ticket;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TicketsForShowing extends HBox {
    private String orderName;
    private int amount;
    private Showing showing;
    private List<Integer> seatNumbers;
    private GridPane globalGridPane;
    private Label titleLBL;
    private Label startLBL;
    private Label endLBL;
    private Label seatsLBL;
    private Label priceLBL;
    private List<Ticket> tickets;


    public TicketsForShowing(String orderName, int amount, Showing showing) {
        this.orderName = orderName;
        this.amount = amount;
        this.showing = showing;
        setUpSeatNumbers();
        setUpPDF();
    }

    private void setUpSeatNumbers() {
        seatNumbers = new ArrayList<>();
        Integer firstnumber = showing.getSoldTickets().size() - amount;
        for(int i = amount; i > 0; i--) {
            seatNumbers.add(firstnumber + 1);
            firstnumber++;
        }
    }

    private void setUpPDF() {
        DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //Sets up document name
        String timeNow = LocalDateTime.now().format(form);
        Document doc = new Document();
        String printName = orderName.replace(' ','_');

        try {
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream
                    ("src/main/resources/files/ticketspdf/"
                            + printName + "-" + timeNow + ".pdf"));

            doc.open();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            for(int i = 0; i < amount; i++) {

                //new page after four tickets.
                if(i % 4 == 0) {
                    doc.newPage();
                }

                int number = i + 1;
                doc.add(new Paragraph("Ticket " + number));
                doc.add(new Paragraph("Title: " + showing.getTitle()));
                doc.add(new Paragraph("Start: " + showing.getStart().format(formatter)));
                doc.add(new Paragraph("End: " + showing.getStart().format(formatter)));
                doc.add(new Paragraph("Seats: " + showing.getRoom().getSeats()));
                doc.add(new Paragraph("Price: " + showing.getMovie().getPrice()));
                doc.add(new Paragraph("Name: " + orderName));
                doc.add(new Paragraph("Seat number: " + seatNumbers.get(i)));
                doc.add(new Paragraph("\n"));
            }

            doc.close();
            writer.close();

        } catch (DocumentException e)
        {
            e.printStackTrace();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
