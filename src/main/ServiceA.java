package main;

public class ServiceA {
	private DaoA da;
	private DaoB db;
	public DaoA getDa() {
		return da;
	}
	public void setDa(DaoA da) {
		this.da = da;
	}
	public DaoB getDb() {
		return db;
	}
	public void setDb(DaoB db) {
		this.db = db;
	}
	public int getSum() {
		System.out.println("this is getSum in service a");
		return da.getNumber() + db.getNumber();
	}
	public void isThisWillCalled() {
		System.out.println("hahaha");
	}
	public void callInnerMethod(boolean param) {
		if(param)
			isThisWillCalled();
	}
	public void getInnerBoolean(DaoA da) {
		if(da.getBoolean()) {
			da.getNumber();
			da.getStr("yes");
		}
	}
}
