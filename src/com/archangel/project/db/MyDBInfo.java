package com.archangel.project.db;


public class MyDBInfo {
	
	private static String TableNames[] = {
		"TBL_EXPENDITURE_CATEGORY",
		"TBL_EXPENDITURE",
	};
	
	private static String FieldNames[][] = {
		{"ID", "NAME", "BUDGET", "COLOR"},
		{"ID", "AMOUNT", "EXPENDITURE_CATEGORY_ID", "DATE", "MEMO"},
	};
	
	private static String FieldTypes[][] = {
		{"INTEGER PRIMARY KEY AUTOINCREMENT", "TEXT", "DOUBLE", "TEXT"},
		{"INTEGER PRIMARY KEY AUTOINCREMENT", "DOUBLE", "INTEGER", "TEXT", "TEXT"},
	};

	public static String[] getTableNames() {
		return TableNames;
	}

	public static String[][] getFieldNames() {
		return FieldNames;
	}

	public static String[][] getFieldTypes() {
		return FieldTypes;
	}	
}

