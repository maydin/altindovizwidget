package com.murat.altindoviz;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="Kur")
public class Kur {

	@Element(name="Kod")
	String kodu;
	
	@Element(name="Alis")
	String alis;
	
	@Element(name="Satis",required=false)
	String satis;
	
	@Element(name="Aciklama")
	String aciklama;
	
	@Element(name="GuncellenmeZamani")
	String guncellenmeZamani;
	
	public String getKodu()
	{
		return kodu;
	}
	
	public String getAlis()
	{
		return alis;
	}
	
	public String getSatis()
	{
		return satis;
	}
	
	public String getGuncellemeZamani()
	{
		return guncellenmeZamani;
	}
}
