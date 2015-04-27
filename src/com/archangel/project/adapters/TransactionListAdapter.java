package com.archangel.project.adapters;

import java.util.List;

import com.archangel.project.R;
import com.archangel.project.activities.TransactionListActivity;
import com.archangel.project.data.CommonData;
import com.archangel.project.data.TransactionData;
import com.archangel.project.dialogs.TransactionDialog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionListAdapter extends BaseAdapter {
	
	private List<Object> mTransactionObjList;
	private Context mContext;
	private CommonData mCommonData = CommonData.getInstance();
	private int mode;
	
	public TransactionListAdapter(Context context, List<Object> list, int mode) {
		this.mContext = context;
		this.mTransactionObjList = list;
		this.mode = mode;
	}

	@Override
	public int getCount() {
		return mTransactionObjList.size();
	}

	@Override
	public Object getItem(int position) {
		return mTransactionObjList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final TransactionData mData = (TransactionData) getItem(position);
		viewHolder mHolder;
		
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.list_item_transaction, parent, false);
			mHolder = new viewHolder();
			
			mHolder.categoryColor = (ImageView) convertView.findViewById(R.id.category_color_imageView);
			mHolder.categoryName = (TextView) convertView.findViewById(R.id.category_name_textView);
			mHolder.expenseAmount = (TextView) convertView.findViewById(R.id.expense_amount_textView);
			mHolder.expenseDate = (TextView) convertView.findViewById(R.id.expense_date_textView);
			mHolder.expenseMemo = (TextView) convertView.findViewById(R.id.expense_memo_textView);
			mHolder.deleteEntryButton = (Button) convertView.findViewById(R.id.expense_delete_button);
			mHolder.editEntryButton = (Button) convertView.findViewById(R.id.expense_edit_button);
			
			convertView.setTag(mHolder);
		} else {
			mHolder = (viewHolder) convertView.getTag();
		}
		
		int categoryColor = Color.parseColor(mCommonData.expenseCategoryDataList.get(mData.category_id).color);
		mHolder.categoryColor.setBackgroundColor(categoryColor);
		
		String categoryName = mCommonData.expenseCategoryDataList.get(mData.category_id).name;
		mHolder.categoryName.setText(categoryName);
		
		mHolder.expenseAmount.setText(String.format("$ %.2f", mData.amount));
		
		if (mode == TransactionListActivity.MODE_DAILY && mData.date.equals(CommonData.getDate(-1))) {
			mHolder.expenseDate.setText("Today");
		} else {
			mHolder.expenseDate.setText(mData.date);
		}
		
		if (mData.memo != null && !mData.memo.isEmpty()) {
			mHolder.expenseMemo.setText(mData.memo);
		}
		
		mHolder.editEntryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TransactionDialog mDialog = TransactionDialog.newInstance(mData, TransactionDialog.TRANS_EDIT);
				mDialog.show(((TransactionListActivity) mContext).getFragmentManager(), "Edit Transaction Dialog");	
			}
		});
		
		mHolder.deleteEntryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCommonData.deleteTransaction(mData);
				((TransactionListActivity) mContext).refreshList();
				Toast.makeText(mContext, "Entry Deleted!", Toast.LENGTH_SHORT).show();
			}
		});
		
		return convertView;
	}
	
	static class viewHolder {
		ImageView categoryColor;
		TextView categoryName;
		TextView expenseAmount;
		TextView expenseDate;
		TextView expenseMemo;
		Button deleteEntryButton;
		Button editEntryButton;
	}

}
