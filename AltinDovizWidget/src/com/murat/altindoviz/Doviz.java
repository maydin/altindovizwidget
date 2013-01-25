package com.murat.altindoviz;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="DOVIZ")
public class Doviz {

	@Element(name="ADI")
	String adi;
	
	@Element(name="ALIS")
	String alis;
	
	@Element(name="SATIS",required=false)
	String satis;
	
	public String getAdi()
	{
		return adi;
	}
	
	public String getAlis()
	{
		return alis;
	}
	
	public String getSatis()
	{
		return satis;
	}
}
