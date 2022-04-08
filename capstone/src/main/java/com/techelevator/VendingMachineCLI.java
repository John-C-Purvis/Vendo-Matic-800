package com.techelevator;

import com.techelevator.view.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";//adds the option to exit out of the program
	private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT};
	private static final int MAX_STOCK_LEVEL = 5;
	private Scanner userInput = new Scanner(System.in);//added
	private double moneyInserted = 0;//keep count of the money inserted by user;
	private double grossBalance = 0;
	private Menu menu;
	private List<Vendable> stock = new ArrayList<>();
	//DecimalFormat f = new DecimalFormat("##.00");
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
			} else if (choice.equals(MAIN_MENU_OPTION_EXIT))
				System.exit(0);
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
			int menuSelection = Integer.parseInt(userInput.nextLine());
			if (menuSelection == 1) {
				//FEED MONEY
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
			} else if (menuSelection == 2) {
				//SELECT A PRODUCT
				boolean selecting = true;
				while (selecting) {
					vendingMachineItems();
					System.out.println("Please select an item by its slot ID");
					String productSelected = userInput.nextLine();
					boolean exists = false;
					for(Vendable pick : stock) {
						if(pick.getSlotLocation().equals(productSelected)) {
							exists = true;
							if(pick.getNumberInStock() == 0) {
								System.out.println("This item has sold out.");
								selecting = false;
								break;
							}
							else {
								if(moneyInserted >= pick.getPurchasePrice()) {
									System.out.println("Vending: "
											+ pick.getProductName()
											+ " $"
											+ f.format(pick.getPurchasePrice())
											+ " Funds Remaining: $"
											+ f.format(moneyInserted - pick.getPurchasePrice())
									);
									if(pick.getProductType().equals("Chip")) System.out.println("Crunch Crunch, Yum!");
									else if(pick.getProductType().equals("Candy")) System.out.println("Munch Munch, Yum!");
									else if(pick.getProductType().equals("Drink")) System.out.println("Glug Glug, Yum!");
									else if(pick.getProductType().equals("Gum")) System.out.println("Chew Chew, Yum!");
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
									selecting = false;
								}
								else {
									System.out.println("This item costs more than the amount tendered.");
									selecting = false;
									break;
								}
							}
						}
					}
					if(!exists) {
						System.out.println("This product code does not exist.");
						selecting = false;
					}
				}
			} else if (menuSelection == 3) {
				//FINISH TRANSACTION
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
					else {
						System.out.println("Tendering error: cannot return $" + f.format(moneyInserted));
					}
				}
				if(quarters > 0) System.out.println("Quarters returned: " + quarters);
				if(dimes > 0) System.out.println("Dimes returned: " + dimes);
				if(nickels > 0) System.out.println("Nickels returned: " + nickels);
				valid = true;
			} else {
				System.out.println("Invalid selection.  Please select an available menu option.");
			}
		}
	}
}
