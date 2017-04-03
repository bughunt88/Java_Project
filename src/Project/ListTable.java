package Project;

public class ListTable {

	private int no;
	public int getNo() {
		return no;
	}
	private String today;
	private int pay;
	private String memo;
	private int price;
	private int balance;
	

	public void setNo(int no) {
		this.no = no;
	}
	public String getToday() {
		return today;
	}
	public void setToday(String today) {
		this.today = today.replace("-", "/"); 
	}
	public int getPay() {
		return pay;
	}
	public void setPay(int pay) {
		this.pay = pay;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "ListTable [no=" + no + ", today=" + today + ", pay=" + pay + ", memo=" + memo + ", price=" + price
				+ ", balance=" + balance + "]";
	}
	

	

	
}
