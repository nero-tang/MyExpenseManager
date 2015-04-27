package com.archangel.project.adapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.archangel.project.R;
import com.archangel.project.data.CommonData;
import com.archangel.project.data.CurrencyData;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class CurrencySpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
	
	private LayoutInflater mInflater;
	private List<Object> mCurrencyList = new ArrayList<Object>();
	private CommonData mCommonData = CommonData.getInstance();
	
	public CurrencySpinnerAdapter(Context context) {
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Iterator<CurrencyData> iterator = mCommonData.currencyDataList.iterator();
		while (iterator.hasNext()) {
			CurrencyData mData = iterator.next();
			mCurrencyList.add(mData);		
		}
	}

	@Override
	public int getCount() {
		return mCurrencyList.size();
	}

	@Override
	public Object getItem(int position) {
		return mCurrencyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CurrencyData mData = (CurrencyData) getItem(position);
		viewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_currency_spinner, parent, false);
			mHolder = new viewHolder();
			mHolder.countryFlag = (ImageView) convertView.findViewById(R.id.country_flag);
			mHolder.currencyCode = (TextView) convertView.findViewById(R.id.currency_code);
			convertView.setTag(mHolder);
		} else {
			mHolder = (viewHolder) convertView.getTag();
		}
		mHolder.countryFlag.setImageDrawable(mData.icon);
		mHolder.currencyCode.setText(mData.currencyCode);
		
		
		return convertView;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		final CurrencyData mData = (CurrencyData) getItem(position);
		viewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_currency_spinner, parent, false);
			mHolder = new viewHolder();
			mHolder.countryFlag = (ImageView) convertView.findViewById(R.id.country_flag);
			mHolder.currencyCode = (TextView) convertView.findViewById(R.id.currency_code);
			convertView.setTag(mHolder);
		} else {
			mHolder = (viewHolder) convertView.getTag();
		}
		mHolder.countryFlag.setImageDrawable(mData.icon);
		mHolder.currencyCode.setText(mData.currencyCode);
		
		
		return convertView;
		
	}
	
	static class viewHolder {
		ImageView countryFlag;
		TextView currencyCode;
	}
	
	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}
	
	
}