package com.techelevator;
import com.techelevator.view.Menu;
import junit.framework.TestCase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.awt.SystemColor.menu;
import static org.junit.jupiter.api.Assertions.*;


public class VendingMachineCLITest {

    private Menu menu;
    VendingMachineCLI f = new VendingMachineCLI(menu);
    VendingMachineCLI test = new VendingMachineCLI(menu);
    List<Vendable> test1 = new ArrayList<>();
    VendingMachineCLI product = new VendingMachineCLI(menu);

    //Work
    @Test
    public void feedMoneyInvidInput() {

        assertEquals(true, f.InvalidOption("3"));
        assertEquals(true, f.InvalidOption("4"));
        assertEquals(true, f.InvalidOption("6"));
        assertEquals(true, f.InvalidOption("7"));
        assertEquals(true, f.InvalidOption("8"));
        assertEquals(true, f.InvalidOption("9"));
        assertEquals(true, f.InvalidOption("Don"));
        assertEquals(true, f.InvalidOption("One"));
        assertEquals(true, f.InvalidOption("Doe"));
        assertEquals(true, f.InvalidOption("Dne"));

    }
    //Work
    @Test
    public void feedMoneyValidInput() {


        assertEquals(true, f.InvalidOption("1"));
        assertEquals(true, f.InvalidOption("2"));
        assertEquals(true, f.InvalidOption("5"));
        assertEquals(true, f.InvalidOption("10"));
        assertEquals(false, f.InvalidOption("DONE"));
        assertEquals(false, f.InvalidOption("DONe"));
        assertEquals(false, f.InvalidOption("DOnE"));
        assertEquals(false, f.InvalidOption("DoNE"));
        assertEquals(false, f.InvalidOption("dONE"));
    }
//Work
    @Test
    public void feedMoneyAcceptedOneTwoFiveTen() {
        assertEquals(1.00, f.MoneyAccepted("1"));
        assertEquals(3.00, f.MoneyAccepted("2"));
        assertEquals(8.00, f.MoneyAccepted("5"));
        assertEquals(18.00, f.MoneyAccepted("10"));
    }
//Work
    @Test
    public void feedMoneyNotAccepted() {
        assertEquals(0.00, f.MoneyAccepted("3"));
        assertEquals(0.00, f.MoneyAccepted("4"));
        assertEquals(0.00, f.MoneyAccepted("6"));
        assertEquals(0.00, f.MoneyAccepted("7"));

    }

////Needs refactoring
//    @Test
//    void itemWasFoundCaseZero() {
//        //product.run();
//
//        //create a list of items in stock
//        File dataFile = new File("C:\\MAJAVA\\Capstones\\module-1-capstone\\capstone\\vendingmachine.csv");
//        try (Scanner dataInput = new Scanner(dataFile)) {
//            while(dataInput.hasNextLine()) {
//                String lineOfInput = dataInput.nextLine();
//                String[] splitInput = lineOfInput.split("\\|");
//                Vendable stockItem = new Vendable(
//                        splitInput[0],
//                        splitInput[1],
//                        Double.parseDouble(splitInput[2]),
//                        splitInput[3],
//                        5
//                );
//                test1.add(stockItem);
//            }
//        } catch (FileNotFoundException e) {
//            System.err.println("The file does not exist.");
//        }
//
//        String productSelected = "C69";
//        double moneyInserted = 10.00;
//        boolean foundStockCode = false;
//        for (Vendable pick : test1) {
//            if (pick.getSlotLocation().equals(productSelected)) {
//                assertEquals(false, VendingMachineCLI.itemWasFound(pick, moneyInserted));//should break if false
//            }
//        }
//        assertEquals(false, foundStockCode);//should break if false
//
//    }
////Does not Work
//    @Test
//    void itemWasFoundCaseDefault() {
//        //product.run();
//
//        //create a list of items in stock
//        File dataFile = new File("C:\\MAJAVA\\Capstones\\module-1-capstone\\capstone\\vendingmachine.csv");
//        try (Scanner dataInput = new Scanner(dataFile)) {
//            while(dataInput.hasNextLine()) {
//                String lineOfInput = dataInput.nextLine();
//                String[] splitInput = lineOfInput.split("\\|");
//                Vendable stockItem = new Vendable(
//                        splitInput[0],
//                        splitInput[1],
//                        Double.parseDouble(splitInput[2]),
//                        splitInput[3],
//                        5
//                );
//                test1.add(stockItem);
//            }
//        } catch (FileNotFoundException e) {
//            System.err.println("The file does not exist.");
//        }
//
//        String productSelected = "C4";
//        for (Vendable pick : test1) {
//            if (pick.getSlotLocation().equals(productSelected)) {
//                //exists = true;
//                assertEquals(true, product.itemWasFound(pick));//should break if false
//                break;
//            }
//        }
//
//    }
//Works
    @Test
    void snackSoundVendingMachine() {
        //product.run();
        assertEquals("Crunch Crunch, Yum!", product.snackSound("Chip"));

    }

//Works
    @Test
    void amountOfQuatersReturned() {
        int[] expectedResult = {5, 1, 0};
        assertEquals(Arrays.toString(expectedResult), test.finishedTransaction(1.35));

    }
}