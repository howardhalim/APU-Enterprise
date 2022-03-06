/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author Steven-
 */
public class generatePDF {
    List<List<String>> data = new ArrayList<>();
    public generatePDF (List<List<String>>  data){
        this.data = data;
    }
    
     void addTableHeader(PdfPTable table) {
        Stream.of("Index", "Item Name", "Brand", "Category", "Stock", "Price", "Date Added")
        .forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(columnTitle));
            table.addCell(header);
        }   );
    }
     void addRows(PdfPTable table) {
         
        for(int i = 0;i<data.size();i++){
            
            String index = String.valueOf(i+1);
            table.addCell(index);
            for(int j = 0 ; j<6;j++){
               
               table.addCell(data.get(i).get(j));
            }
        } 
         
        
    }
    
    void addCustomRows(PdfPTable table) 
    throws URISyntaxException, BadElementException, IOException {
        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }
}
