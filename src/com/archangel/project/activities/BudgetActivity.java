package com.archangel.project.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.archangel.project.R;
import com.archangel.project.adapters.BudgetListAdapter;
import com.archangel.project.data.CommonData;
import com.archangel.project.data.ExpenseCategoryData;
import com.archangel.project.dialogs.TransactionDialog;
import com.archangel.project.dialogs.TransactionDialog.OnTransactionDialogCloseListener;

public class BudgetActivity extends SherlockActivity implements OnItemClickListener, OnTransactionDialogCloseListener {	
	private ListView mBudgetListView;
	private CommonData mCommonData = CommonData.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_budget);
		// Custom Action Bar
        getSupportActionBar().hide();
        
        // Initialize List View
        mBudgetListView = (ListView) findViewById(R.id.budget_listView);
        mBudgetListView.setOnItemClickListener(this);
        refreshBudget();
        
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View mView, int position, long id) {
		ExpenseCategoryData mData = (ExpenseCategoryData) parent.getItemAtPosition(position);
		TransactionDialog mKeyPadDialog = TransactionDialog.newInstance(mData, TransactionDialog.BUDGET_EDIT);
		mKeyPadDialog.show(getFragmentManager(), "Buget KeyPad Dialog");	
	}
	
	public void refreshBudget() {
		new RefreshDataAsyncTask().execute();
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		super.onBackPressed();
	}

	@Override
	public void onCloseTransactionDialog(View v) {
		refreshBudget();	
	}
	
	private class RefreshDataAsyncTask extends AsyncTask<Void, Void, Void> {
		
		/** Object list for budget data */
		private List<Object> mBudgetObjList = new ArrayList<Object>();

		@Override
		protected Void doInBackground(Void... params) {
			mCommonData.refresh();
			Iterator<ExpenseCategoryData> iterator = mCommonData.expenseCategoryDataList.values().iterator();
			while (iterator.hasNext()) {
				ExpenseCategoryData data = iterator.next();
				mBudgetObjList.add(data);			
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mBudgetListView.setAdapter(new BudgetListAdapter(BudgetActivity.this, mBudgetObjList));
			super.onPostExecute(result);
		}
		
		
	}
}