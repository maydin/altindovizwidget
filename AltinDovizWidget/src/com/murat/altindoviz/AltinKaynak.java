package com.murat.altindoviz;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="altinkaynak")
public class AltinKaynak {

	@ElementList(inline=true)
	private List<Doviz> list = new ArrayList<Doviz>();
	
	public List<Doviz> getList()
	{
		return list;
	}
	
	public String[] getValues()
	{
		String[] values = new String[]{"","","","","","",""};
		for (Doviz doviz : list) {
			
			if(doviz.getAdi().contains("USD"))
			{
				values[0]= doviz.getAlis();
				values[1]= doviz.getSatis();
			}
			else if(doviz.getAdi().contains("EUR"))
			{
				values[2]= doviz.getAlis();
				values[3]= doviz.getSatis();
			}
			else if(doviz.getAdi().contains("HH"))
			{
				values[4]= doviz.getAlis();
				values[5]= doviz.getSatis();
			}
			else if(doviz.getAdi().contains("Tarih"))
			{
				values[6]= doviz.getAlis();
			}
		}
		
		return values;
	}
}
