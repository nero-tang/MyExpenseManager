package com.archangel.project.data;

import android.os.Parcel;
import android.os.Parcelable;

public class TransactionData implements Parcelable{
	public int id;			
	public double amount;
	public int category_id;
	public String date;
	public String memo;
	
	public static final Parcelable.Creator<TransactionData> CREATOR = new Parcelable.Creator<TransactionData>() {
		
		@Override
		public TransactionData createFromParcel(Parcel in) {
			return new TransactionData(in);
		}
	
		@Override
		public TransactionData[] newArray(int size) {
			return new TransactionData[size];
		}
	};
	
	private TransactionData(Parcel in) {
		id = in.readInt();
		amount = in.readDouble();
		category_id = in.readInt();
		date = in.readString();
		memo = in.readString();
	}
	
	public TransactionData(int id, double amount, int category_id, String date, String memo) {
		this.id = id;
		this.amount = amount;
		this.category_id = category_id;
		this.date = date;
		this.memo = memo;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeInt(id);
		dest.writeDouble(amount);
		dest.writeInt(category_id);
		dest.writeString(date);
		dest.writeString(memo);
		
	}
}