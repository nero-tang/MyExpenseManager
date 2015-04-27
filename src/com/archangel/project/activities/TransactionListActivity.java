package com.archangel.project.activities;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import com.actionbarsherlock.app.SherlockActivity;
import com.archangel.project.R;
import com.archangel.project.adapters.TransactionListAdapter;
import com.archangel.project.data.CommonData;
import com.archangel.project.data.TransactionData;
import com.archangel.project.db.MyDBTools;
import com.archangel.project.dialogs.TransactionDialog.OnTransactionDialogCloseListener;
import com.fortysevendeg.swipelistview.SwipeListView;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class TransactionListActivity extends SherlockActivity implements OnClickListener, OnTransactionDialogCloseListener {
	
	public final static int MODE_DAILY = 0;
	public final static int MODE_MONTHLY = 1;
	int MODE = MODE_DAILY;
	
	GregorianCalendar startDate;
	GregorianCalendar endDate;
	
	private TextView expenseTitleTextView, expenseDateTextView;
	private ImageButton lastTimePeriodButton, nextTimePeriodButton;
	SwipeListView transactionListView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction_list);
		getSupportActionBar().hide();
		
		expenseTitleTextView = (TextView) findViewById(R.id.title_textView);
		expenseDateTextView = (TextView) findViewById(R.id.date_textView);
		transactionListView = (SwipeListView) findViewById(R.id.detail_expense_listView);
		lastTimePeriodButton = (ImageButton) findViewById(R.id.last_date_period_button);
		nextTimePeriodButton = (ImageButton) findViewById(R.id.next_date_period_button);
		
		lastTimePeriodButton.setOnClickListener(this);
		nextTimePeriodButton.setOnClickListener(this);
		
		Intent mIntent = getIntent();
		MODE = mIntent.getIntExtra("mode", MODE_DAILY);
		
		if (MODE == MODE_DAILY) {
			expenseTitleTextView.setText("Daily Expense");
			expenseDateTextView.setText(CommonData.getDate(-1));
			startDate = endDate = new GregorianCalendar(Locale.CANADA);
		} else {
			expenseTitleTextView.setText("Monthly Expense");
			expenseDateTextView.setText(CommonData.getDate(CommonData.DATE_MONTH_START)+" to "+CommonData.getDate(CommonData.DATE_MONTH_END));
			startDate = new GregorianCalendar(Locale.CANADA);
			startDate.set(Calendar.DATE, startDate.getActualMinimum(Calendar.DATE));
			endDate = new GregorianCalendar(Locale.CANADA);
			endDate.set(Calendar.DATE, endDate.getActualMaximum(Calendar.DATE));
		}
		refreshList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.last_date_period_button: {
			if (MODE == MODE_DAILY) {
				startDate.add(Calendar.DATE, -1);
				expenseDateTextView.setText(CommonData.dateFormat(startDate.getTime(), CommonData.DATE_yyyy_MM_dd));
			} else {
				startDate.add(Calendar.MONTH, -1);
				endDate.add(Calendar.MONTH, -1);
				endDate.set(Calendar.DATE, endDate.getActualMaximum(Calendar.DATE));
				expenseDateTextView.setText(CommonData.dateFormat(startDate.getTime(), CommonData.DATE_yyyy_MM_dd)+" to "+CommonData.dateFormat(endDate.getTime(), CommonData.DATE_yyyy_MM_dd));
			}
			
			break;
		}
			
		case R.id.next_date_period_button: {
			if (MODE == MODE_DAILY) {
				startDate.add(Calendar.DATE, 1);
				expenseDateTextView.setText(CommonData.dateFormat(startDate.getTime(), CommonData.DATE_yyyy_MM_dd));
			} else {
				startDate.add(Calendar.MONTH, 1);
				endDate.add(Calendar.MONTH, 1);
				endDate.set(Calendar.DATE, endDate.getActualMaximum(Calendar.DATE));
				expenseDateTextView.setText(CommonData.dateFormat(startDate.getTime(), CommonData.DATE_yyyy_MM_dd)+" to "+CommonData.dateFormat(endDate.getTime(), CommonData.DATE_yyyy_MM_dd));
			}
			break;
		}
		default:
			return;
		}
		refreshList();
	}
	
	public void refreshList() {
		new RefreshDataAsyncTask().execute();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			refreshList();
		}
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		super.onBackPressed();
	}

	@Override
	public void onCloseTransactionDialog(View v) {
		refreshList();		
	}
	
	private class RefreshDataAsyncTask extends AsyncTask<Void, Void, Void> {
		
		private List<Object> mTransactionObjList = new ArrayList<Object>();
		private List<TransactionData> list = new ArrayList<TransactionData>();
		private MyDBTools dbTools = null;
		private final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);

		@Override
		protected Void doInBackground(Void... params) {
			this.dbTools = CommonData.getDbTools();
			
			Cursor cursor = dbTools.rawQuery("select * from TBL_EXPENDITURE where strftime('%Y-%m-%d',DATE)>=? and strftime('%Y-%m-%d',DATE)<=? order by ID desc", 
					new String[] {CommonData.dateFormat(startDate.getTime(), CommonData.DATE_yyyy_MM_dd),
					              CommonData.dateFormat(endDate.getTime(), CommonData.DATE_yyyy_MM_dd)});
			while (cursor.moveToNext()) {
				list.add(new TransactionData(
						cursor.getInt(0),
						cursor.getDouble(1),
						cursor.getInt(2),
						cursor.getString(3),
						cursor.getString(4)));
			}
			
			Collections.sort(list, new Comparator<TransactionData>() {

				@Override
				public int compare(TransactionData arg0, TransactionData arg1) {
					int ret = 0;
					try {
						ret = mDateFormat.parse(arg0.date).compareTo(mDateFormat.parse(arg1.date));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					return ret;
				}
			});
			
			ListIterator<TransactionData> iterator = list.listIterator();
			while(iterator.hasNext()){
				TransactionData data = iterator.next();
				mTransactionObjList.add(data);
			}
			
			cursor.close();	
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			transactionListView.setAdapter(new TransactionListAdapter(TransactionListActivity.this, mTransactionObjList, MODE));
			super.onPostExecute(result);
		}
		
	}
	
}
