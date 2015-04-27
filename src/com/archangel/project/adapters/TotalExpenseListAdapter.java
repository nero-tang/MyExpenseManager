package com.archangel.project.adapters;

import java.util.List;

import com.archangel.project.R;
import com.archangel.project.data.CommonData;
import com.archangel.project.data.ExpenseCategoryData;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TotalExpenseListAdapter extends BaseAdapter {
	
	private List<Object> mTotalExpenseObjList;
	private LayoutInflater mInflater;
	private CommonData mCommonData = CommonData.getInstance();
	
	public TotalExpenseListAdapter(Context context, List<Object> list) {
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mTotalExpenseObjList = list;	
	}

	@Override
	public int getCount() {
		return mTotalExpenseObjList.size();
	}

	@Override
	public Object getItem(int position) {
		return mTotalExpenseObjList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ExpenseCategoryData mData = (ExpenseCategoryData) getItem(position);
		viewHolder mHolder;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item_total_expense, parent, false);
			mHolder = new viewHolder();
			
			mHolder.categoryName = (TextView) convertView.findViewById(R.id.category_name_textView);
			mHolder.totalExpense = (TextView) convertView.findViewById(R.id.total_expense_textView);
			mHolder.expenseDistribution = (TextView) convertView.findViewById(R.id.expense_distribution_textView);
			
			convertView.setTag(mHolder);
		} else {
			mHolder = (viewHolder) convertView.getTag();
		}
		
		mHolder.categoryName.setText(mData.name);
		mHolder.categoryName.setTextColor(Color.parseColor(mData.color));
		mHolder.totalExpense.setText(String.format("$ %.2f", mData.totalAmount));
		
		Double distribution = mData.totalAmount * 100 / mCommonData.getTotalExpenseAmount();
		mHolder.expenseDistribution.setText(String.format("%.1f%%", distribution));
		
		return convertView;
	}
	
	static class viewHolder {
		TextView categoryName;
		TextView totalExpense;
		TextView expenseDistribution;
	}
	
}