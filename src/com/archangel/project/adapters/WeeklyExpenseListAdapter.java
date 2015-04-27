package com.archangel.project.adapters;

import java.util.List;

import com.archangel.project.R;
import com.archangel.project.data.CommonData;
import com.archangel.project.data.ExpenseCategoryData;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;


public class WeeklyExpenseListAdapter extends BaseAdapter {
	
	private List<Object> mWeeklyExpenseObjList;
	private LayoutInflater mInflater;
	private CommonData mCommonData = CommonData.getInstance();
	
	public WeeklyExpenseListAdapter(Context context, List<Object> list) {
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mWeeklyExpenseObjList = list;	
	}

	@Override
	public int getCount() {
		return mWeeklyExpenseObjList.size();
	}

	@Override
	public Object getItem(int position) {
		return mWeeklyExpenseObjList.get(position);
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
			convertView = mInflater.inflate(R.layout.list_item_weekly_expense, parent, false);
			mHolder = new viewHolder();
			
			mHolder.categoryName = (TextView) convertView.findViewById(R.id.category_name_textView);
			mHolder.weeklyExpense = (TextView) convertView.findViewById(R.id.weekly_expense_textView);
			mHolder.isOverBudget = (TextView) convertView.findViewById(R.id.expense_over_budget_textView);
			mHolder.expenseDistribution = (ProgressBar) convertView.findViewById(R.id.expense_distribution_progressBar);
			
			ShapeDrawable pgDrawable = new ShapeDrawable();
			pgDrawable.getPaint().setColor(Color.parseColor(mData.color));
			mHolder.expenseDistribution.setProgressDrawable(new ClipDrawable(pgDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL));
			
			convertView.setTag(mHolder);
		} else {
			mHolder = (viewHolder) convertView.getTag();
		}
		
		mHolder.categoryName.setText(mData.name);
		mHolder.weeklyExpense.setText(String.format("$ %.2f", mData.weeklyAmount));		
		if (mData.monthlyAmount <= mData.monthlyBudget) {
			mHolder.isOverBudget.setVisibility(View.GONE);
		}
		
		Double distribution = mData.weeklyAmount * 100 / mCommonData.getWeeklyExpenseAmount();
		distribution = distribution < 1 ? 1 : distribution;	
		
		mHolder.expenseDistribution.setProgress(distribution.intValue()); 

		return convertView;
	}
	
	static class viewHolder {
		TextView categoryName;
		TextView weeklyExpense;
		TextView isOverBudget;
		ProgressBar expenseDistribution;
	}

}
