package com.archangel.project.dialogs;

import java.util.Calendar;
import com.archangel.project.R;
import com.archangel.project.adapters.CurrencySpinnerAdapter;
import com.archangel.project.adapters.ExpenseCategorySpinnerAdapter;
import com.archangel.project.converters.GoogleCurrencyConverter;
import com.archangel.project.data.CommonData;
import com.archangel.project.data.ExpenseCategoryData;
import com.archangel.project.data.TransactionData;
import com.archangel.project.dialogs.HSVColorPickerDialog.OnColorSelectedListener;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class TransactionDialog extends DialogFragment implements OnClickListener {
	
	public static final int TRANS_NEW = 0;
	public static final int TRANS_EDIT = 1;
	public static final int BUDGET_NEW = 2;
	public static final int BUDGET_EDIT = 3;
	private int Mode;
	
	private Button mOneButton, mTwoButton, mThreeButton, mFourButton, mFiveButton,
	mSixButton, mSevenButton, mEightButton, mNineButton, mZeroButton, mDotButton, 
	mDelButton, mOKButton, mCancelButton;
	
	private Spinner mExpenseCategorySpinner, mFromCurrencySpinner;
	private Button mDatePickerButton;
	private DatePickerDialog mDatePicker;
	private EditText mMemoEditText;
	private EditText mCategoryNameEditText;
	private Button mCategoryColorPickButton;
	private HSVColorPickerDialog mColorPickerDialog;
	private LinearLayout mTransactionExtraLayout;
	private LinearLayout mBudgetExtraLayout;
	private TextView amountTextView;
	private Calendar mCalendar = Calendar.getInstance();
	private CommonData mCommonData = CommonData.getInstance();
	
	private TransactionData mTransactionData;
	private ExpenseCategoryData mExpenseCategoryData;
	private String value;
	
	private OnTransactionDialogCloseListener mListener;
	
	public static TransactionDialog newInstance(TransactionData mData, int Mode) {
		TransactionDialog mKeyPad = new TransactionDialog();
		Bundle args = new Bundle();
        args.putParcelable("TransactionData", mData);
        args.putInt("MODE", Mode);
        mKeyPad.setArguments(args);
		return mKeyPad;	  
	}
	
	public static TransactionDialog newInstance(ExpenseCategoryData mData, int Mode) {
		TransactionDialog mKeyPad = new TransactionDialog();
		Bundle args = new Bundle();
        args.putParcelable("ExpenseCategoryData", mData);
        args.putInt("MODE", Mode);
        mKeyPad.setArguments(args);
		return mKeyPad;	  
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (OnTransactionDialogCloseListener) activity;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		this.Mode = getArguments().getInt("MODE");
		if (Mode == BUDGET_NEW || Mode == BUDGET_EDIT) {
			this.mExpenseCategoryData = getArguments().getParcelable("ExpenseCategoryData");
			this.value = String.format("%.2f", mExpenseCategoryData.monthlyBudget);
		} else {
			this.mTransactionData = getArguments().getParcelable("TransactionData");
			this.value = String.format("%.2f", mTransactionData.amount);
		}		
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		   Bundle savedInstanceState) {	
		View mView = inflater.inflate(R.layout.dialogfragment_transaction, null);
        
        // Initialize Amount TextView
        amountTextView = (TextView) mView.findViewById(R.id.amount_textView);
        
        // Initialize Buttons
        mOneButton = (Button) mView.findViewById(R.id.key_one_button);
        mOneButton.setOnClickListener(this);
        
        mTwoButton = (Button) mView.findViewById(R.id.key_two_button);
        mTwoButton.setOnClickListener(this);
        
        mThreeButton = (Button) mView.findViewById(R.id.key_three_button);
        mThreeButton.setOnClickListener(this);
        
        mFourButton = (Button) mView.findViewById(R.id.key_four_button);
        mFourButton.setOnClickListener(this);
        
        mFiveButton = (Button) mView.findViewById(R.id.key_five_button);
        mFiveButton.setOnClickListener(this);
        
        mSixButton = (Button) mView.findViewById(R.id.key_six_button);
        mSixButton.setOnClickListener(this);
        
        mSevenButton = (Button) mView.findViewById(R.id.key_seven_button);
        mSevenButton.setOnClickListener(this);
        
        mEightButton = (Button) mView.findViewById(R.id.key_eight_button);
        mEightButton.setOnClickListener(this);
        
        mNineButton = (Button) mView.findViewById(R.id.key_nine_button);
        mNineButton.setOnClickListener(this);
        
        mZeroButton = (Button) mView.findViewById(R.id.key_zero_button);
        mZeroButton.setOnClickListener(this);
        
        mDotButton = (Button) mView.findViewById(R.id.key_dot_button);
        mDotButton.setOnClickListener(this);
        
        mDelButton = (Button) mView.findViewById(R.id.key_del_button);
        mDelButton.setOnClickListener(this);
        
        mOKButton = (Button) mView.findViewById(R.id.key_ok_button);
        mOKButton.setOnClickListener(this);
        
        mCancelButton = (Button) mView.findViewById(R.id.key_cancel_button);
        mCancelButton.setOnClickListener(this);
        
        // Initialize Spinner
        mExpenseCategorySpinner = (Spinner) mView.findViewById(R.id.expense_category_spinner);
        mExpenseCategorySpinner.setAdapter(new ExpenseCategorySpinnerAdapter(getActivity()));
        
        mFromCurrencySpinner = (Spinner) mView.findViewById(R.id.currency_spinner);
        mFromCurrencySpinner.setAdapter(new CurrencySpinnerAdapter(getActivity()));
        
        mDatePickerButton = (Button) mView.findViewById(R.id.expense_date_button);
        
        // Initialize Memo Edit Text
        mMemoEditText = (EditText) mView.findViewById(R.id.expense_memo_editText);
        
        mCategoryNameEditText = (EditText) mView.findViewById(R.id.expense_category_name_editText);
        
        mCategoryColorPickButton = (Button) mView.findViewById(R.id.expense_category_colorpicker_button);
        mCategoryColorPickButton.setOnClickListener(this);
        
        // Extra layout
        mTransactionExtraLayout = (LinearLayout) mView.findViewById(R.id.expense_extra_layout);
        mBudgetExtraLayout = (LinearLayout) mView.findViewById(R.id.budget_extra_layout);
        
        if (value == "0.00") {
			value = "0";
		}
        amountTextView.setText(value);
        
        switch (Mode) {
		case TRANS_NEW:
			getDialog().setTitle("New Expenditure");	
			break;
		case TRANS_EDIT: {
			getDialog().setTitle("Edit Expenditure");
			String date[] = mTransactionData.date.split("-", 3);
			mCalendar.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));			
			break;
		}
		case BUDGET_EDIT: 
			getDialog().setTitle("Edit Budget");		
			break;
		case BUDGET_NEW: 
			getDialog().setTitle("New Budget");		
			break;
		}        
        
        if (Mode == TRANS_NEW || Mode == TRANS_EDIT) {
        	mBudgetExtraLayout.setVisibility(View.GONE);
        	mExpenseCategorySpinner.setSelection(mTransactionData.category_id - 1);
			mMemoEditText.setText(mTransactionData.memo);
        	mDatePickerButton.setText(CommonData.dateFormat(mCalendar.getTime(), CommonData.DATE_yyyy_MM_dd));
        	mDatePickerButton.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				mDatePicker = new DatePickerDialog(v.getContext(), mDateSetListenerSatrt, 
    		        		mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
    				mDatePicker.show();
    			}
    		});
		} else {
			mTransactionExtraLayout.setVisibility(View.GONE);
			mFromCurrencySpinner.setVisibility(View.GONE);
			mCategoryNameEditText.setText(mExpenseCategoryData.name);
			mCategoryColorPickButton.setBackgroundColor(Color.parseColor(mExpenseCategoryData.color));
			mCategoryColorPickButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mColorPickerDialog = new HSVColorPickerDialog(v.getContext(), Color.parseColor(mExpenseCategoryData.color), new OnColorSelectedListener() {
						
						@Override
						public void colorSelected(Integer color) {
							mCategoryColorPickButton.setBackgroundColor(color);
							mExpenseCategoryData.color = String.format("#%06X", (0xFFFFFF & color));
							
						}
					});
					mColorPickerDialog.setTitle("Pick a Color");
					mColorPickerDialog.show();
				}
			});
		}
        return mView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.key_cancel_button: {
			this.dismiss();
			break;
		}
			
		case R.id.key_ok_button: {
			new CurrencyConvertAsyncTask().execute(v);
			break;
		}
		
		case R.id.key_dot_button: {
			if (!value.contains(".")) {
				value += ".";
			}	
			break;
		}
		
		case R.id.key_del_button: {
			if (value.length() > 1) {
				value = value.substring(0, value.length()-1);
			} else {
				value="0";
			}
			break;
		}
		
		default: {
			if (value.equals("0.00") || value.equals("0")) {
				if (v.getId() != R.id.key_zero_button) {
					value = ((Button) v).getText().toString();
				}			
			} else if (!value.contains(".") || (value.contains(".") && value.length() - value.indexOf('.') <= 2)) {
				value += ((Button) v).getText();
			}
			break;
		}
		}
		
		amountTextView.setText(value);
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListenerSatrt = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			mCalendar.set(Calendar.MONTH, monthOfYear);
			mCalendar.set(Calendar.YEAR, year);
			mDatePickerButton.setText(CommonData.dateFormat(mCalendar.getTime(), CommonData.DATE_yyyy_MM_dd));
		}
	};
	
	public interface OnTransactionDialogCloseListener {
		public abstract void onCloseTransactionDialog(View v);
	}

	private class CurrencyConvertAsyncTask extends AsyncTask<View, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dismiss();
		}

		@Override
		protected Void doInBackground(View... params) {
			if (Mode == TRANS_NEW || Mode == TRANS_EDIT) {
				double exRate = 1.0;
				String fromCurrencyCode = mCommonData.currencyDataList.get(mFromCurrencySpinner.getSelectedItemPosition()).currencyCode;
				Log.i("Selected Currency Code", fromCurrencyCode);
				if (!fromCurrencyCode.equals("CAD")) {
					try {
						exRate = new GoogleCurrencyConverter().convert(fromCurrencyCode, "CAD");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}			
				mTransactionData.amount = exRate * Double.parseDouble(value);
				mTransactionData.category_id = mExpenseCategorySpinner.getSelectedItemPosition() + 1;
				mTransactionData.date = CommonData.dateFormat(mCalendar.getTime(), CommonData.DATE_yyyy_MM_dd);
				mTransactionData.memo = mMemoEditText.getText().toString();
			} else {
				mExpenseCategoryData.monthlyBudget = Double.parseDouble(value);
				mExpenseCategoryData.name = mCategoryNameEditText.getText().toString();
			}
			switch (Mode) {
			case TRANS_NEW:	{
				if (mTransactionData.amount > 0) {
					mCommonData.insertTransaction(mTransactionData);

				}
				break;
			}
			case TRANS_EDIT: {
				if (mTransactionData.id > 0 && mTransactionData.amount > 0) {
					mCommonData.updateTransaction(mTransactionData);
				}
				break;
			}
			case BUDGET_EDIT: {	
				if (mExpenseCategoryData.id > 0) {
					mCommonData.updateBudget(mExpenseCategoryData);
				}
				break;
			}
			case BUDGET_NEW: {
				mCommonData.insertBudget(mExpenseCategoryData);
			}
			}		    	
			
			mListener.onCloseTransactionDialog(params[0]);
			return null;
		}
		
	}
}
