package com.archangel.project.adapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.archangel.project.R;
import com.archangel.project.data.CommonData;
import com.archangel.project.data.ExpenseCategoryData;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ExpenseCategorySpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
	
	private LayoutInflater mInflater;
	private List<Object> mCategoryList = new ArrayList<Object>();
	private CommonData mCommonData = CommonData.getInstance();
	
	public ExpenseCategorySpinnerAdapter(Context context) {
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Iterator<ExpenseCategoryData> iterator = mCommonData.expenseCategoryDataList.values().iterator();
		while (iterator.hasNext()) {
			ExpenseCategoryData data = iterator.next();
			this.mCategoryList.add(data);		
		}
	}

	@Override
	public int getCount() {
		return mCategoryList.size();
	}

	@Override
	public Object getItem(int position) {
		return mCategoryList.get(position);
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
		
		final ExpenseCategoryData mData = (ExpenseCategoryData) getItem(position);
		viewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_category_spinner, parent, false);
			mHolder = new viewHolder();
			
			mHolder.categoryColor = (ImageView) convertView.findViewById(R.id.category_color);
			mHolder.categoryName = (TextView) convertView.findViewById(R.id.category_name);
			
			convertView.setTag(mHolder);
		} else {
			mHolder = (viewHolder) convertView.getTag();
		}
		
		mHolder.categoryColor.setBackgroundColor(Color.parseColor(mData.color));
		mHolder.categoryName.setText(mData.name);
		
		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
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

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		final ExpenseCategoryData mData = (ExpenseCategoryData) getItem(position);
		viewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_category_spinner, parent, false);
			mHolder = new viewHolder();
			
			mHolder.categoryColor = (ImageView) convertView.findViewById(R.id.category_color);
			mHolder.categoryName = (TextView) convertView.findViewById(R.id.category_name);
			
			convertView.setTag(mHolder);
		} else {
			mHolder = (viewHolder) convertView.getTag();
		}
		
		mHolder.categoryColor.setBackgroundColor(Color.parseColor(mData.color));
		mHolder.categoryName.setText(mData.name);
		
		return convertView;
	}
	
	static class viewHolder {
		ImageView categoryColor;
		TextView categoryName;
	}
	
}