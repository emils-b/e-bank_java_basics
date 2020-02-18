package usable_app_proj;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class web_scraping_USD {
	
	public web_scraping_USD() {
	Document doc = null;
	try{
		doc = Jsoup.connect("https://www.bank.lv/statistika/dati-statistika/valutu-kursi/aktualie").userAgent("Mozilla/72.0.2").get();
	}
	
	catch (IOException e) {
		e.printStackTrace();
	}
	
	Elements curVal=doc.select("td.value");
	double USD=Double.parseDouble(curVal.get(510).getElementsByTag("td").first().text());
	}
	
}
