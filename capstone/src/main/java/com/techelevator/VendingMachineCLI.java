package com.techelevator;

import com.techelevator.view.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";//adds the option to exit out of the program
	private static final String MAIN_MENU_OPTION_REPORT = "";
	private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT, MAIN_MENU_OPTION_REPORT};
	private static final int MAX_STOCK_LEVEL = 5;
	private Scanner userInput = new Scanner(System.in);//added
	private double moneyInserted = 0;//keep count of the money inserted by user;
	private double grossBalance = 0;
	private Menu menu;
	private List<Vendable> stock = new ArrayList<>();
	private static final DecimalFormat f = new DecimalFormat("0.00");

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {
		File dataFile = new File("C:\\MAJAVA\\Capstones\\module-1-capstone\\capstone\\vendingmachine.csv");
		try (Scanner dataInput = new Scanner(dataFile)) {
			while(dataInput.hasNextLine()) {
				String lineOfInput = dataInput.nextLine();
				String[] splitInput = lineOfInput.split("\\|");
				Vendable stockItem = new Vendable(
						splitInput[0],
						splitInput[1],
						Double.parseDouble(splitInput[2]),
						splitInput[3],
						MAX_STOCK_LEVEL
				);
				stock.add(stockItem);
			}
		} catch (FileNotFoundException e) {
			System.err.println("The file does not exist.");
		}
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				// display vending machine items
				vendingMachineItems();
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				// do purchase
				purchasingProcessMenu();
			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				System.exit(0);
			} else if (choice.equals(MAIN_MENU_OPTION_REPORT)) {
				//secret sales report option
				reportSales();
			}
		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}

	//when entering 1 displays a list of vending machine items
	public void vendingMachineItems() {
		for(Vendable item : stock) {
			String slotID = item.getSlotLocation();
			String itemName = item.getProductName();
			double itemPrice = item.getPurchasePrice();
			String priceString = f.format(itemPrice);
			String itemType = item.getProductType();
			int numberRemaining = item.getNumberInStock();
			String remaining = "SOLD OUT";
			if(numberRemaining > 0) remaining = Integer.toString(numberRemaining);
			System.out.println(slotID + "|" + itemName + "|" + priceString + "|" + itemType + "|" + remaining + " in stock");
		}
	}

	//when entering 2 displays options for purchasing menu
	public void purchasingProcessMenu() {
		String[] purchasingOptions = {"(1) Feed Money\n" + "(2) Select Product\n" + "(3) Finish Transaction"};
		boolean valid = false;
		while(!valid) {
			for (String options : purchasingOptions) System.out.println(options);
			String userSelection = userInput.nextLine();
			try {
				Integer.parseInt(userSelection);
				int menuSelection = Integer.parseInt(userSelection);
				if (menuSelection == 1) {
					//FEED MONEY
					feedMoney();
				} else if (menuSelection == 2) {
					//SELECT A PRODUCT
					purchaseProduct();

				} else if (menuSelection == 3) {
					//FINISH TRANSACTION
					valid = finishedTransaction();

				} else {
					System.out.println("Invalid selection.  Please select an available menu option.");
				}
			}
			catch( Exception e ) {
				System.out.println("Invalid selection.  Please select an available menu option.");
			}
		}
	}

	public void feedMoney(){
		System.out.println("Current Money Provided: $" + f.format(moneyInserted));
		boolean feeding = true;
		while (feeding) {
			System.out.println("Please insert a dollar amount: 1, 2, 5, 10 or DONE");
			String tendered = userInput.nextLine();
			if(tendered.equalsIgnoreCase("done")) {
				feeding = false;
			}
			else if(Integer.parseInt(tendered) == 1
					|| Integer.parseInt(tendered) == 2
					|| Integer.parseInt(tendered) == 5
					|| Integer.parseInt(tendered) == 10
			) {
				moneyInserted += Double.parseDouble(tendered);
			}
			else {
				System.out.println("Invalid input.  Please select a listed option.");
			}
			System.out.println("Current Money Provided: $" + f.format(moneyInserted));
			AuditLog.log("FEED MONEY: $" + tendered + " $" + f.format(moneyInserted));
		}
	}

	public void purchaseProduct(){
		vendingMachineItems();
		System.out.println("Please select an item by its slot ID");
		String productSelected = userInput.nextLine();
		boolean exists = false;
		for(Vendable pick : stock) {
			if (pick.getSlotLocation().equals(productSelected)) {
				exists = true;
				if (pick.getNumberInStock() == 0) {
					System.out.println("This item has sold out.");
					break;
				} else {
					if (moneyInserted >= pick.getPurchasePrice()) {
						System.out.println("Vending: "
								+ pick.getProductName()
								+ " $"
								+ f.format(pick.getPurchasePrice())
								+ " Funds Remaining: $"
								+ f.format(moneyInserted - pick.getPurchasePrice())
						);
						if (pick.getProductType().equals("Chip")) System.out.println("Crunch Crunch, Yum!");
						else if (pick.getProductType().equals("Candy")) System.out.println("Munch Munch, Yum!");
						else if (pick.getProductType().equals("Drink")) System.out.println("Glug Glug, Yum!");
						else if (pick.getProductType().equals("Gum")) System.out.println("Chew Chew, Yum!");
						double moneyLeftOver = moneyInserted - pick.getPurchasePrice();
						AuditLog.log(pick.getProductName()
								+ " "
								+ pick.getSlotLocation()
								+ " $"
								+ f.format(moneyInserted)
								+ " $"
								+ f.format(moneyLeftOver)
						);
						grossBalance += pick.getPurchasePrice();
						moneyInserted -= pick.getPurchasePrice();
						int newValue = pick.getNumberInStock() - 1;
						pick.setNumberInStock(newValue);
					} else {
						System.out.println("This item costs more than the amount tendered.");
						break;
					}
				}
			}
		}
		if(!exists) {
				System.out.println("This product code does not exist.");
		}

	}

	public boolean finishedTransaction(){
		System.out.println("Total change: $" + f.format(moneyInserted));
		AuditLog.log("GIVE CHANGE: $" + f.format(moneyInserted) + " $0.00");
		int nickels = 0;
		int dimes = 0;
		int quarters = 0;
		while(moneyInserted > 0) {
			if(moneyInserted >= 0.25) {
				moneyInserted -= 0.25;
				quarters++;
			}
			else if(moneyInserted >= 0.1) {
				moneyInserted -= 0.1;
				dimes++;
			}
			else if(moneyInserted >= 0.05) {
				moneyInserted -= .05;
				nickels++;
			}
			else if (moneyInserted < 0.05) {
				moneyInserted = 0;
			}
			else {
				System.out.println("Tendering error: cannot return $" + f.format(moneyInserted));
			}
		}
		if(quarters > 0) System.out.println("Quarters returned: " + quarters);
		if(dimes > 0) System.out.println("Dimes returned: " + dimes);
		if(nickels > 0) System.out.println("Nickels returned: " + nickels);
		return true;
	}

	public void reportSales() {
		// set up format for timestamp on sales report filename
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss_a");
		Date timeStamp = new Date(System.currentTimeMillis());
		//build path and filename for sales report file (adapt this to the local directory structure)
		String dataPath = "C:\\MAJAVA\\Capstones\\module-1-capstone\\capstone\\src\\main\\java\\com\\techelevator\\";
		String dataFile = dataPath + "Vending_Machine_Sales_Report_" + formatter.format(timeStamp) + ".txt";
		//write statistics to sales report
		File reportFile = new File(dataFile);
		try (PrintWriter dataOutput = new PrintWriter(reportFile)) {
			//loop through all stock items and report number of each item sold since restarting the vending machine
			for(Vendable stockItem : stock) {
				//build report for each individual stock item
				String stockName = stockItem.getProductName();
				int stockSold = MAX_STOCK_LEVEL - stockItem.getNumberInStock();
				//write the item's statistics to the sales report file
				dataOutput.println(stockName + "\\|" + stockSold);
			}
			//add a final line to the sales report displaying total gross sales since the last restart of the vending machine
			dataOutput.println("\nTOTAL SALES: $" + f.format(grossBalance));
		} catch (Exception e) {
			System.out.println("An error has occurred.");
		}
	}
}
