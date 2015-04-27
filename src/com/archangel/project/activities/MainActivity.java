package com.archangel.project.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import com.archangel.project.R;
import com.archangel.project.adapters.TotalExpenseListAdapter;
import com.archangel.project.adapters.WeeklyExpenseListAdapter;
import com.archangel.project.data.CommonData;
import com.archangel.project.data.ExpenseCategoryData;
import com.archangel.project.data.TransactionData;
import com.archangel.project.dialogs.TransactionDialog;
import com.archangel.project.dialogs.TransactionDialog.OnTransactionDialogCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SlidingActivity implements OnTransactionDialogCloseListener, OnClickListener {

	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	
	private SlidingMenu mSlidingMenu;
	private ImageButton mNewExpenseButton;
	private Button mEditBudgetButton;
	private Button mVoiceRecognitionButton;
	private RelativeLayout mDailyRow;
	private RelativeLayout mMonthlyRow;
	
	private CommonData mCommonData = CommonData.getInstance();
	
	private ListView mWeeklyExpenseListView;
	private ListView mTotalDistributionListView;
	private TextView weekDurationTextView;
	private TextView dailyRowMonthTextView;
	private TextView dailyRowDayTextView;
	private TextView dailyRowExAmtTextView;
	private TextView monthlyRowYearTextView;
	private TextView monthlyRowMonthTextView;
	private TextView monthlyRowExAmtTextView;
	private TextView totalBudgetTextView;
	private TextView remainingBudgetTextView;	
	private TextView weeklyExAmountTextView;
	private ProgressBar budgetProgressBar;
	
	/** The main series that will include all the data. */
	private CategorySeries mSeries = new CategorySeries("All Time Expense");
	private ArrayList<Integer> mCategoryIdxHolder = new ArrayList<Integer>();
	/** The main renderer for the main dataset. */
	private DefaultRenderer mRenderer = new DefaultRenderer();
	/** The chart view that displays the data. */
	private GraphicalView mChartView;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);			
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
			ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
			
			addExpenseByVoice(matches);
			refreshData();
		}
		if (resultCode == RESULT_OK){
			refreshData();
		}	
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		setBehindContentView(R.layout.activity_behind);
		
		weekDurationTextView = (TextView) findViewById(R.id.week);
		weeklyExAmountTextView = (TextView) findViewById(R.id.weeklyEx_textView);
		totalBudgetTextView = (TextView) findViewById(R.id.budget_textView);
		remainingBudgetTextView = (TextView) findViewById(R.id.percentage_textView);
		budgetProgressBar = (ProgressBar) findViewById(R.id.budget_progressBar);
		dailyRowMonthTextView = (TextView) findViewById(R.id.dailyRow_month_textView);
		dailyRowDayTextView = (TextView) findViewById(R.id.dailyRow_day_textView);
		dailyRowExAmtTextView = (TextView) findViewById(R.id.dailyEx_textView);
		monthlyRowYearTextView = (TextView) findViewById(R.id.monthlyRow_year_textView);
		monthlyRowMonthTextView = (TextView) findViewById(R.id.monthlyRow_month_textView);
		monthlyRowExAmtTextView = (TextView) findViewById(R.id.monthlyEx_textView);
		
		mNewExpenseButton = (ImageButton) findViewById(R.id.new_expense_button);
		mNewExpenseButton.setOnClickListener(this);
		
		mEditBudgetButton = (Button) findViewById(R.id.edit_budget_button);
		mEditBudgetButton.setOnClickListener(this);
		
		mVoiceRecognitionButton = (Button) findViewById(R.id.voice_recog_button);
		mVoiceRecognitionButton.setOnClickListener(this);
		
		mDailyRow = (RelativeLayout) findViewById(R.id.dailyRow);
		mDailyRow.setOnClickListener(this);
		
		mMonthlyRow = (RelativeLayout) findViewById(R.id.monthlyRow);
		mMonthlyRow.setOnClickListener(this);
				
		// Set Sliding Menu both LEFT and RIGHT
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		mSlidingMenu.setMode(SlidingMenu.RIGHT);	
		mSlidingMenu.setBehindOffset(50);
		mSlidingMenu.setFadeDegree(0.35f);
		mSlidingMenu.setFadeEnabled(true);
		
		// Hide Action Bar
        getSupportActionBar().hide();
        
        // Initialize Budget ListView
        weeklyExAmountTextView = (TextView) findViewById(R.id.weeklyEx_textView);
        mWeeklyExpenseListView = (ListView) findViewById(R.id.weeklyEx_listView);
        mTotalDistributionListView = (ListView) findViewById(R.id.all_time_expense_listView);
        
        // Chart Engine
        //mRenderer.setDisplayValues(false);
        mRenderer.setAntialiasing(true);
        mRenderer.setShowLabels(false);
        mRenderer.setStartAngle(180);
        mRenderer.setLegendTextSize(15);
        mRenderer.setChartTitleTextSize(50);
        mRenderer.setZoomEnabled(false);
        mRenderer.setPanEnabled(false);
                
        refreshData();	
	}
	
	/**
     * Fire an intent to start the speech recognition activity.
     */
    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }
	


	public void refreshData() {
		new RefreshDataAsyncTask().execute();
	}

	@Override
	public void onCloseTransactionDialog(View v) {
		refreshData();		
	}
	
	/**
	 * Not yet been implemented, just a simple demo.
	 */
	private void addExpenseByVoice(ArrayList<String> matches) {
		String result = matches.get(0);
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		
		TransactionData mData = new TransactionData(0, 0, 0, CommonData.getDate(-1), null);
		
		String[] fragments = result.split(" ");
		for (int i = 0; i < fragments.length; i++) {
			if (fragments[i].contains("$")) {
				try {
					mData.amount = Double.parseDouble(fragments[i].substring(1));
				} catch (Exception e) {
					e.getStackTrace();
				}
				
			} else if (fragments[i].equals("dollors")) {
				try {
					mData.amount = Double.parseDouble(fragments[i-1]);
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
			Iterator<ExpenseCategoryData> iterator = mCommonData.expenseCategoryDataList.values().iterator();
			while (iterator.hasNext()) {
				ExpenseCategoryData categoryData = iterator.next();
				if (fragments[i].equals(categoryData.name.toLowerCase())) {
					mData.category_id = categoryData.id;
					break;
				}
			}
		}
		if (mData.amount > 0 && mData.category_id > 0) {
			mCommonData.insertTransaction(mData);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_expense_button: {
			TransactionData mData = new TransactionData(0, 0, 0, null, null);
			TransactionDialog mDialog = TransactionDialog.newInstance(mData, TransactionDialog.TRANS_NEW);
			mDialog.show(getFragmentManager(), "New Expense Dialog");
			break;
		}
		case R.id.edit_budget_button: {
			Intent mIntent = new Intent(this, BudgetActivity.class);
			startActivityForResult(mIntent, 100);
			//startVoiceRecognitionActivity();
			break;
		}
		case R.id.dailyRow: {
			Intent mIntent = new Intent(this, TransactionListActivity.class);
			mIntent.putExtra("mode", TransactionListActivity.MODE_DAILY);
			startActivityForResult(mIntent, 100);
			break;
		}
		case R.id.monthlyRow: {
			Intent mIntent = new Intent(this, TransactionListActivity.class);
			mIntent.putExtra("mode", TransactionListActivity.MODE_MONTHLY);
			startActivityForResult(mIntent, 100);
			break;
		}
		case R.id.voice_recog_button: {
			startVoiceRecognitionActivity();
			break;
		}
		default:
			break;
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	    if (mChartView == null) {
	    	LinearLayout layout = (LinearLayout) findViewById(R.id.chart_holder);
	    	mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
	    	mRenderer.setClickEnabled(true);
	    	mChartView.setOnClickListener(new View.OnClickListener() {
	    		
	    		@Override
	    		public void onClick(View v) {
	    			SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
	    			if (seriesSelection == null) {
	    				Toast.makeText(MainActivity.this, "No chart element selected", Toast.LENGTH_SHORT).show();
	    			} else {
	    				for (int i = 0; i < mSeries.getItemCount(); i++) {
	    					mRenderer.getSeriesRendererAt(i).setHighlighted(i == seriesSelection.getPointIndex());
	    				}
	    				mChartView.repaint();
	    				Toast.makeText(MainActivity.this, "You have spent $ " + 
	    												  String.format("%.2f", mCommonData.expenseCategoryDataList.get(mCategoryIdxHolder.get(seriesSelection.getPointIndex())).totalAmount) + 
	    												  " on " + 
	    												  mCommonData.expenseCategoryDataList.get(mCategoryIdxHolder.get(seriesSelection.getPointIndex())).name, 
	    												  Toast.LENGTH_LONG).show();
	    			}
	    		}
	    	});
	    	
	    	layout.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	    } else {
	    	mChartView.repaint();
	    }
	}
	
	
	/**
	 * AsyncTask for refresh the overview data.
	 */
	private class RefreshDataAsyncTask extends AsyncTask<Void, Void, Void> {
		
		/** Object list for weekly expense in every category */
		private List<Object> mWeeklyExpenseObjList = new ArrayList<Object>();
		/** Object list for total expense in every category */
		private List<Object> mTotalExpenseObjList = new ArrayList<Object>();

		@Override
		protected Void doInBackground(Void... params) {
			
			// Clean dataset for pie chart
			mSeries.clear();
			mCategoryIdxHolder.clear();
			mRenderer.removeAllRenderers();
			
			// Refresh common data
			mCommonData.refresh();
			
			Iterator<ExpenseCategoryData> iterator = mCommonData.expenseCategoryDataList.values().iterator();
			while (iterator.hasNext()) {
				ExpenseCategoryData data = iterator.next();
				
				if (data.weeklyAmount != 0) {
					mWeeklyExpenseObjList.add(data);
				} 
				
				if (data.totalAmount != 0) {
					mSeries.add(data.name, data.totalAmount);
					mCategoryIdxHolder.add(data.id);
					SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
					renderer.setColor(Color.parseColor(data.color));
					mRenderer.addSeriesRenderer(renderer);
					mTotalExpenseObjList.add(data);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			setCurrentWeek(weekDurationTextView);
			weeklyExAmountTextView.setText(String.format("$ %.2f", mCommonData.getWeeklyExpenseAmount()));
			totalBudgetTextView.setText(String.format("$ %.2f", mCommonData.getMonthlyBudgetAmount()));
			
			double remainingBudgetPercentage = mCommonData.getMonthlyBudgetAmount() > 0 ? (mCommonData.getMonthlyBudgetAmount() - mCommonData.getMonthlyExpenseAmount())*100/mCommonData.getMonthlyBudgetAmount() : 0;
			remainingBudgetTextView.setText(String.format("%.1f%%", remainingBudgetPercentage));
			budgetProgressBar.setProgress((int) (remainingBudgetPercentage < 0 ? 0 : remainingBudgetPercentage));
			dailyRowMonthTextView.setText(CommonData.getCurrentMonth(Integer.parseInt(mCommonData.getMonth())-1));
			dailyRowDayTextView.setText(mCommonData.getDay());
			dailyRowExAmtTextView.setText(String.format("$ %.2f", mCommonData.getDailyExpenseAmount()));
			monthlyRowYearTextView.setText(mCommonData.getYear());
			monthlyRowMonthTextView.setText(CommonData.getCurrentMonth(Integer.parseInt(mCommonData.getMonth())-1));
			monthlyRowExAmtTextView.setText(String.format("$ %.2f", mCommonData.getMonthlyExpenseAmount()));
			mWeeklyExpenseListView.setAdapter(new WeeklyExpenseListAdapter(MainActivity.this, mWeeklyExpenseObjList));	
			mTotalDistributionListView.setAdapter(new TotalExpenseListAdapter(MainActivity.this, mTotalExpenseObjList));
			
			mChartView.repaint();
			
			super.onPostExecute(result);
		}
		
		private void setCurrentWeek(TextView tv) {
			GregorianCalendar tempDate = new GregorianCalendar(Locale.CANADA);
			tempDate.set(Calendar.DAY_OF_WEEK, tempDate.getFirstDayOfWeek());
			String wkStart = CommonData.dateFormat(tempDate.getTime(), CommonData.DATE_MMM_dd);
			tempDate.add(Calendar.DAY_OF_WEEK, 6);
			String wkEnd = CommonData.dateFormat(tempDate.getTime(), CommonData.DATE_MMM_dd);
			tv.setText(wkStart+" - "+wkEnd);
		}
		
	}
}