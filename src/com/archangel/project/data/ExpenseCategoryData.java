package com.archangel.project.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ExpenseCategoryData implements Parcelable {
	public int id;
	public String name;
	public double monthlyBudget;
	public String color;
	public double dailyAmount = 0.0;
	public double weeklyAmount = 0.0;
	public double monthlyAmount = 0.0;
	public double totalAmount = 0.0;
	
	public ExpenseCategoryData(int id, String name, double monthlyBudget, String colorHex) {
		this.id = id;
		this.name = name;
		this.monthlyBudget = monthlyBudget;	
		this.color = colorHex;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeDouble(monthlyBudget);
		dest.writeString(color);
		dest.writeDouble(dailyAmount);
		dest.writeDouble(weeklyAmount);
		dest.writeDouble(monthlyAmount);
		dest.writeDouble(totalAmount);
		
	}
	
	public static final Parcelable.Creator<ExpenseCategoryData> CREATOR = new Parcelable.Creator<ExpenseCategoryData>() {
		@Override
		public ExpenseCategoryData createFromParcel(Parcel in) {
			return new ExpenseCategoryData(in);
		}
			
		@Override
		public ExpenseCategoryData[] newArray(int size) {
			return new ExpenseCategoryData[size];
		}
	};
	
	private ExpenseCategoryData(Parcel in) {
		id = in.readInt();
		name = in.readString();
		monthlyBudget = in.readDouble();
		color = in.readString();
		dailyAmount = in.readDouble();
		weeklyAmount = in.readDouble();
		monthlyAmount = in.readDouble();
		totalAmount = in.readDouble();
	}
}
