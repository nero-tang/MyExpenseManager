package com.archangel.project.data;

import android.graphics.drawable.Drawable;

public class CurrencyData {
	public Drawable icon;
	public String currencyCode;
	
	public CurrencyData() {
		
	}
	
	public CurrencyData(Drawable icon, String currencyCode) {
		this.icon = icon;
		this.currencyCode = currencyCode;
	}
}