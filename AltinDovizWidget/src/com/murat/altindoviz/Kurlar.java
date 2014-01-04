package com.murat.altindoviz;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="Kurlar")
public class Kurlar {

	@ElementList(inline=true)
	private List<Kur> list = new ArrayList<Kur>();
	
	public List<Kur> getList()
	{
		return list;
	}
	
	public void addKur(Kurlar newKur)
	{
		this.list.addAll(newKur.getList());
	}
	
	public String[] getValues()
	{
		String[] values = new String[]{"","","","","","",""};
		for (Kur doviz : list) {
			
			if(doviz.getKodu().contains("USD"))
			{
				values[0]= doviz.getAlis();
				values[1]= doviz.getSatis();
			}
			else if(doviz.getKodu().contains("EUR"))
			{
				values[2]= doviz.getAlis();
				values[3]= doviz.getSatis();
			}
			else if(doviz.getKodu().contains("HH_T"))
			{
				values[4]= doviz.getAlis();
				values[5]= doviz.getSatis();
				values[6]= doviz.getGuncellemeZamani();
			}
		}
		
		return values;
	}
}
