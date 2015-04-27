package com.archangel.project.adapters;

import java.util.List;

import com.archangel.project.R;
import com.archangel.project.data.ExpenseCategoryData;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BudgetListAdapter extends BaseAdapter {
	
	private List<Object> mBudgetObjList;
	private LayoutInflater mInflater;
	
	public BudgetListAdapter(Context context, List<Object> list) {
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mBudgetObjList = list;	
	}

	@Override
	public int getCount() {
		return mBudgetObjList.size();
	}

	@Override
	public Object getItem(int position) {
		return mBudgetObjList.get(position);
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
			convertView = mInflater.inflate(R.layout.list_item_budget, parent, false);
			mHolder = new viewHolder();	
			
			mHolder.categoryColor = (ImageView) convertView.findViewById(R.id.category_color_imageView);
			mHolder.categoryName = (TextView) convertView.findViewById(R.id.category_name_textView);
			mHolder.monthlyBudget = (TextView) convertView.findViewById(R.id.monthly_budget_textView);
			mHolder.monthlyExpense = (TextView) convertView.findViewById(R.id.monthly_expense_textView);
			mHolder.budgetCompleteness = (ProgressBar) convertView.findViewById(R.id.budget_completeness_progressBar);
			
			convertView.setTag(mHolder);		
		} else {
			mHolder = (viewHolder) convertView.getTag();
		}
		
		mHolder.categoryColor.setBackgroundColor(Color.parseColor(mData.color));
		mHolder.categoryName.setText(mData.name);
		mHolder.monthlyBudget.setText(String.format("$ %.2f", mData.monthlyBudget));
		mHolder.monthlyExpense.setText(String.format("$ %.2f", mData.monthlyAmount));
		
		Double completeness = mData.monthlyBudget > 0 ? (mData.monthlyAmount * 100 / mData.monthlyBudget) : 0;
		mHolder.budgetCompleteness.setProgress(completeness.intValue()); 
		
		return convertView;
	}
	
	static class viewHolder {
		ImageView categoryColor;
		TextView categoryName;
		TextView monthlyBudget;
		TextView monthlyExpense;
		ProgressBar budgetCompleteness;
	}

}
