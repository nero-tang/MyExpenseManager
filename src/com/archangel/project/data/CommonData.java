package com.archangel.project.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.util.Log;

import com.archangel.project.R;
import com.archangel.project.activities.SplashScreenActivity;
import com.archangel.project.db.MyDBInfo;
import com.archangel.project.db.MyDBTools;


public class CommonData {
	public static final int DATE_MMM_dd = 0;
	public static final int DATE_yyyy_MM_dd = 1;
	public static final int DATE_WEEK_START = 0;
	public static final int DATE_WEEK_END = 1;
	public static final int DATE_MONTH_START = 2;
	public static final int DATE_MONTH_END = 3;
	private static final String Month[] = {"Jan","Feb","Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};	
	
	private static CommonData instance = null;
	private static MyDBTools dbTools = null;
	private static Resources mResources = null;
	
	private String year = null;
	private String month = null;
	private String day = null;
	private String today = null;
	private String weekStart = null;
	private String weekEnd = null;
	private String monthStart = null;
	private String monthEnd = null;
	private double dailyExpenseAmount = 0.0;
	private double weeklyExpenseAmount = 0.0;
	private double monthlyExpenseAmount = 0.0;
	private double monthlyBudgetAmount = 0.0;
	private double totalExpenseAmount = 0.0;
	
	@SuppressLint("UseSparseArrays")
	public HashMap<Integer, ExpenseCategoryData> expenseCategoryDataList = new HashMap<Integer, ExpenseCategoryData>();
	
	public List<CurrencyData> currencyDataList = new ArrayList<CurrencyData>();
	
	private void initDate() {
		GregorianCalendar mCalendar = new GregorianCalendar(Locale.CANADA);
		year = String.valueOf(mCalendar.get(Calendar.YEAR));
		month = String.valueOf(mCalendar.get(Calendar.MONTH)+1);
		if(Integer.parseInt(month)<10) {
			month = "0" + month;
		}
			
		day = String.valueOf(mCalendar.get(Calendar.DATE));
		if(Integer.parseInt(day)<10) {
			day = "0" + day;
		}
		today = getDate(-1);
		weekStart = getDate(DATE_WEEK_START);
		weekEnd = getDate(DATE_WEEK_END);	
		monthStart = getDate(DATE_MONTH_START);
		monthEnd = getDate(DATE_MONTH_END);
	}
	
	
	public static CommonData getInstance() {
		if (instance == null) {
			instance = new CommonData();
		}	
		return instance;
	}
	
	public void load() {
		dbTools = SplashScreenActivity.dbTools;
		mResources = SplashScreenActivity.mResources;
		refresh();
	}
	
	public void refresh() {
		initDate();
		loadExpenseCategoryData();
		loadCurrencyData();
		updateExpense();
	}
	
	public void loadCurrencyData() {
		currencyDataList.clear();
		String[] currencyCodeArr = mResources.getStringArray(R.array.currency_codes);
		TypedArray countryFlagArr = mResources.obtainTypedArray(R.array.country_flag);
		for (int i = 0; i < currencyCodeArr.length; i++) {
			CurrencyData mData = new CurrencyData(countryFlagArr.getDrawable(i), currencyCodeArr[i]);
			currencyDataList.add(mData);
		}
		countryFlagArr.recycle();
	}
	
	public void loadExpenseCategoryData() {
		expenseCategoryDataList.clear();		
		Cursor cursor = dbTools.select(MyDBInfo.getTableNames()[0], MyDBInfo.getFieldNames()[0], null, null, null, null, null);
		while (cursor.moveToNext()) {
			expenseCategoryDataList.put(cursor.getInt(0), new ExpenseCategoryData(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3)));
		}
		cursor.close();
		updateBudgetAmount();
	}
	
	
	private void updateBudgetAmount()
	{
		Cursor cursor = dbTools.rawQuery("select sum(BUDGET) from TBL_EXPENDITURE_CATEGORY", null);
		if (cursor.moveToNext()) {
			monthlyBudgetAmount = cursor.getDouble(0);
		}
		cursor.close();		
	}
	
	public void updateBudget(ExpenseCategoryData data) {
		expenseCategoryDataList.put(data.id, data);
		dbTools.update(MyDBInfo.getTableNames()[0], new String[]{"NAME", "BUDGET", "COLOR"}, new String[]{data.name, String.valueOf(data.monthlyBudget), data.color},"ID="+data.id,null);
		updateBudgetAmount();
	}
	
	public void insertBudget(ExpenseCategoryData data) {
		dbTools.insert(MyDBInfo.getTableNames()[0], new String[]{"NAME", "BUDGET", "COLOR"}, new String[]{data.name, String.valueOf(data.monthlyBudget), data.color});
		loadExpenseCategoryData();
	}
	
	public void updateTransaction(TransactionData data) {
		String [] fieldNames = new String[] {"AMOUNT", 
				   "EXPENDITURE_CATEGORY_ID", 
				   "DATE", 
				   "MEMO"};

		String [] values = new String[] {String.valueOf(data.amount), 
			   String.valueOf(data.category_id), 
			   data.date, 
			   data.memo};	
		
		dbTools.update(MyDBInfo.getTableNames()[1], fieldNames, values, "ID="+data.id, null);
	}
	
	public void insertTransaction(TransactionData data) {
		String [] fieldNames = new String[] {"AMOUNT", 
				   "EXPENDITURE_CATEGORY_ID", 
				   "DATE", 
				   "MEMO"};

		String [] values = new String[] {String.valueOf(data.amount), 
			   String.valueOf(data.category_id), 
			   data.date, 
			   data.memo};	
		
		dbTools.insert(MyDBInfo.getTableNames()[1], fieldNames, values);
	}
	
	public void deleteTransaction(TransactionData data) {
		dbTools.delete(MyDBInfo.getTableNames()[1], "ID=?", new String[] {String.valueOf(data.id)});
	}
	
	public static MyDBTools getDbTools() {
		return dbTools;
	}

	public String getYear() {
		return year;
	}

	public String getMonth() {
		return month;
	}

	public String getDay() {
		return day;
	}

	public String getWeekStart() {
		return weekStart;
	}

	public String getWeekEnd() {
		return weekEnd;
	}

	public double getMonthlyBudgetAmount() {
		return monthlyBudgetAmount;
	}
	
	public double getDailyExpenseAmount() {
		return dailyExpenseAmount;
	}
	
	public double getWeeklyExpenseAmount() {
		return weeklyExpenseAmount;
	}
	
	public double getMonthlyExpenseAmount() {
		return monthlyExpenseAmount;
	}
	
	public double getTotalExpenseAmount() {
		return totalExpenseAmount;
	}
	
	public static String getCurrentMonth(int idx) {
		return Month[idx];
	}
	
	public static String dateFormat(java.util.Date date, int type) {
		String dateString = null;
		switch (type) {
		case DATE_MMM_dd:
			dateString = new SimpleDateFormat("MMM. dd", Locale.CANADA).format(date);
			break;
		case DATE_yyyy_MM_dd:
			dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(date);
			break;
		}
		return dateString;
	}
	
	public static String getDate(int type) {
		GregorianCalendar tempDate = new GregorianCalendar(Locale.CANADA);
		switch (type) {
		case DATE_WEEK_START:
			tempDate.set(Calendar.DAY_OF_WEEK, tempDate.getFirstDayOfWeek());
			break;
		case DATE_WEEK_END:
			tempDate.set(Calendar.DAY_OF_WEEK, tempDate.getFirstDayOfWeek());
			tempDate.add(Calendar.DAY_OF_WEEK, 6);
			break;
		case DATE_MONTH_START:
			tempDate.set(Calendar.DATE, tempDate.getActualMinimum(Calendar.DATE));
			break;
		case DATE_MONTH_END:
			tempDate.set(Calendar.DATE, tempDate.getActualMaximum(Calendar.DATE));
			break;
		default:
			break;
		}
		return dateFormat(tempDate.getTime(), DATE_yyyy_MM_dd);
	}
	
	public void updateExpense() {
		
		// Update daily expense amount of all categories
		Cursor cursor = dbTools.rawQuery("select sum(AMOUNT) from TBL_EXPENDITURE where strftime('%Y-%m-%d',DATE)==?", 
				new String[]{today});
		if (cursor.moveToNext()) {
			dailyExpenseAmount = cursor.getDouble(0);
			Log.i("Daily Expense Amount", ""+dailyExpenseAmount);
		}
		
		// Update weekly expense amount of all categories
		cursor = dbTools.rawQuery("select sum(AMOUNT) from TBL_EXPENDITURE where strftime('%Y-%m-%d',DATE)>=? and strftime('%Y-%m-%d',DATE)<=?", 
				new String[]{weekStart, weekEnd});
		if (cursor.moveToNext()) {
			weeklyExpenseAmount = cursor.getDouble(0);
			Log.i("Weekly Expense Amount", ""+weeklyExpenseAmount);
		}
		
		// Update monthly expense amount of all categories
		cursor = dbTools.rawQuery("select sum(AMOUNT) from TBL_EXPENDITURE where strftime('%Y-%m-%d',DATE)>=? and strftime('%Y-%m-%d',DATE)<=?", 
				new String[]{monthStart, monthEnd});
		if (cursor.moveToNext()) {
			monthlyExpenseAmount = cursor.getDouble(0);
			Log.i("monthyly Expense Amount", ""+monthlyExpenseAmount);
		}
		
		// Update total expense amount of all categories
		cursor = dbTools.rawQuery("select sum(AMOUNT) from TBL_EXPENDITURE", null);
		if (cursor.moveToNext()) {
			totalExpenseAmount = cursor.getDouble(0);
			Log.i("Total Expense Amount", ""+totalExpenseAmount);
		}
		
		Iterator<ExpenseCategoryData> iterator = expenseCategoryDataList.values().iterator();
		while (iterator.hasNext()) {
			int id = iterator.next().id;
			
			cursor = dbTools.rawQuery("select sum(AMOUNT) from TBL_EXPENDITURE where EXPENDITURE_CATEGORY_ID==? and strftime('%Y-%m-%d',DATE)==?", 
					new String[]{String.valueOf(id), today});
			if (cursor.moveToNext()) {
				expenseCategoryDataList.get(id).dailyAmount = cursor.getDouble(0);
			}			
			
			// Update weekly expense amount for every categories
			cursor = dbTools.rawQuery("select sum(AMOUNT) from TBL_EXPENDITURE where EXPENDITURE_CATEGORY_ID==? and strftime('%Y-%m-%d',DATE)>=? and strftime('%Y-%m-%d',DATE)<=?", 
					new String[]{String.valueOf(id), weekStart, weekEnd});
			if (cursor.moveToNext()) {
				expenseCategoryDataList.get(id).weeklyAmount = cursor.getDouble(0);
			}
			
			// Update monthly expense amount for every categories
			cursor = dbTools.rawQuery("select sum(AMOUNT) from TBL_EXPENDITURE where EXPENDITURE_CATEGORY_ID==? and strftime('%Y-%m-%d',DATE)>=? and strftime('%Y-%m-%d',DATE)<=?", 
					new String[]{String.valueOf(id), monthStart, monthEnd});
			if (cursor.moveToNext()) {
				expenseCategoryDataList.get(id).monthlyAmount = cursor.getDouble(0);
			}
			
			// Update total expense amount for every categories
			cursor = dbTools.rawQuery("select sum(AMOUNT) from TBL_EXPENDITURE where EXPENDITURE_CATEGORY_ID==?", new String[]{String.valueOf(id)});
			if (cursor.moveToNext()) {
				expenseCategoryDataList.get(id).totalAmount = cursor.getDouble(0);
			}
			
		}
		cursor.close();
	}
	
	
}