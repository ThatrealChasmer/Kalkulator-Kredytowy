package servlets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@WebServlet("/getpdf")
public class GetPDF extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html");
		String typ = request.getParameter("rata");
		double kwota = Double.parseDouble(request.getParameter("kwota"));
		double ilosc = Double.parseDouble(request.getParameter("ilosc"));
		double proc = Double.parseDouble(request.getParameter("proc"))/100;
		double oplata = Double.parseDouble(request.getParameter("oplata"));
		DecimalFormat format = new DecimalFormat("#.##");
		
		format.setRoundingMode(RoundingMode.CEILING);
		ByteArrayOutputStream tempPDFstorage = new ByteArrayOutputStream();
		
		Document pdf = new Document();
		
		try {
			PdfWriter writer = PdfWriter.getInstance(pdf, tempPDFstorage);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pdf.open();
		
		PdfPTable table = new PdfPTable(5);
		ArrayList<String> nazwy_kolumn = new ArrayList<String>();
		nazwy_kolumn.add("Nr Raty");
		nazwy_kolumn.add("Kwota Kapitalu");
		nazwy_kolumn.add("Kwota Odsetek");
		nazwy_kolumn.add("Oplaty Stale");
		nazwy_kolumn.add("Rata");
		for (String s : nazwy_kolumn) {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(2);
			header.setPhrase(new Phrase(s));
			table.addCell(header);
		}
		
		if(typ.equals("stala"))
		{
			double q = 1 + (proc/12);
			double rata = (kwota * Math.pow(q, ilosc) * (q - 1) / (Math.pow(q, ilosc) - 1)) + oplata/ilosc;
			
			for(int i = 1; i<= ilosc; i++)
			{
				table.addCell(String.valueOf(i));
				table.addCell(String.valueOf(format.format(kwota/ilosc)));
				table.addCell(String.valueOf(format.format(rata - kwota/ilosc - oplata/ilosc)));
				table.addCell(String.valueOf(format.format(oplata/ilosc)));
				table.addCell(String.valueOf(format.format(rata)));
			}
		}
		
		if(typ.equals("malejaca"))
		{
			double doSplaty = kwota;
			for(int i = 1; i<= ilosc; i++)
			{
				double rata = kwota/ilosc + oplata/ilosc + (doSplaty * proc/100/ilosc);
				
				table.addCell(String.valueOf(i));
				table.addCell(String.valueOf(format.format(kwota/ilosc)));
				table.addCell(String.valueOf(format.format(doSplaty * proc/100/ilosc)));
				table.addCell(String.valueOf(format.format(oplata/ilosc)));
				table.addCell(String.valueOf(format.format(rata)));
				
				doSplaty = doSplaty-rata;
			}
		}
		
		try {
			pdf.add(table);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		pdf.close();
		response.reset();
		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "attachement; filename=Raty.pdf");
		response.setContentLength((int)tempPDFstorage.size());
		byte[] pdfBytes = tempPDFstorage.toByteArray();
		ByteArrayInputStream pdfOut = new ByteArrayInputStream(pdfBytes);
		int bytes;
		while ((bytes = pdfOut.read()) != -1) {
			response.getOutputStream().write(bytes);
		}
	}
}
