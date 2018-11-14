package servlets;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/kalk")
public class KalkServlet extends HttpServlet {

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
		
		response.getWriter().println(
				"<table border='2'>"
				+ "<tr><th>Nr raty</th><th>Czesc kapitalowa</th><th>Czesc odsetkowa</th><th>Oplaty stale</th><th>Rata</th></tr>"
				);
		if(typ.equals("stala"))
		{
			double q = 1 + (proc/12);
			double rata = (kwota * Math.pow(q, ilosc) * (q - 1) / (Math.pow(q, ilosc) - 1)) + oplata/ilosc;
			
			for(int i = 1; i<= ilosc; i++)
			{
				
				response.getWriter().println(
					"<tr><td>" + i + "</td>"
							+ "<td>" + format.format(kwota/ilosc) + "</td>"
									+ "<td>" + format.format(rata - kwota/ilosc - oplata/ilosc) + "</td>"
									+ "<td>" + format.format(oplata/ilosc) + "</td>"
									+ "<td>" + format.format(rata) + "</td></tr>"
					);
			}
		}
		
		if(typ.equals("malejaca"))
		{
			double doSplaty = kwota;
			for(int i = 1; i<= ilosc; i++)
			{
				double rata = kwota/ilosc + oplata/ilosc + (doSplaty * proc/100/ilosc);
				response.getWriter().println(
					"<tr><td>" + i + "</td>"
							+ "<td>" + format.format(kwota/ilosc) + "</td>"
									+ "<td>" + format.format(doSplaty * proc/100/ilosc) + "</td>"
									+ "<td>" + format.format(oplata/ilosc) + "</td>"
									+ "<td>" + format.format(rata) + "</td></tr>"
					);
				doSplaty = doSplaty-rata;
			}
		}
		
		response.getWriter().println("</table>" +
				"<form action=\"/getpdf\" method=\"post\">\r\n"
					+ "<input type=\"hidden\" value='" + kwota + "' name=\"kwota\">\r\n"
						+ "<input type=\"hidden\" value='" + ilosc + "' name=\"ilosc\">\r\n"
							+ "<input type=\"hidden\" value='" + proc + "' name=\"proc\">\r\n"
								+ "<input type=\"hidden\" value='" + oplata + "' name=\"oplata\">\r\n" 
									+"<input type=\"hidden\" value='" + typ + "' name=\"rata\">\r\n"
										+"<input type=\"submit\" value=\"Pobierz\">\r\n"
				+ "</form>"
				
				);
	}
}