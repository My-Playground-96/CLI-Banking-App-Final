import java.util.Scanner;

public class BankingApp{
    private static final Scanner scanner = new Scanner( System.in);

    public static void main(String[] args) {
        
        final String CLEAR = "\033[H\033[2J";
        final String COLOR_BLUE_BOLD = "\033[34;1m";
        final String COLOR_RED_BOLD = "\033[31;1m";
        final String COLOR_GREEN_BOLD = "\033[33;1m";
        final String RESET = "\033[0m";

        final String DASHBOARD = "Welcome to Smart Banking";
        final String ADD_CUTOMER = "Creat new Account";
        final String DEPOSIT = "Deposit";
        final String WITHDRAW = "Withdraw";
        final String TRANSFER = "Transfer";
        final String CHECK_ACC_BALANCE = "Check Account balance";
        final String DELETE_ACC ="Delete Account";
        

        final String ERROR_MSG = String.format("\t%s%s%s\n", COLOR_RED_BOLD, "%s", RESET);
        final String SUCCESS_MSG = String.format("\t%s%s%s\n", COLOR_GREEN_BOLD, "%s", RESET);

        String screen = DASHBOARD;
        String name;
        double initialDeposit;
        String [][] customerDetails = new String[0][];
        String accNumber;
        
        double depositAmount;
        double withdrawAmount ;
        double newBalance;
        double newBalanceFromAcc;
        double newBalanceToAcc = 0;
    
        do{
            boolean valid = true;
            System.out.println(CLEAR);
            String appTitle = screen;
            System.out.println("+".concat("-".repeat(40)).concat("+"));
            System.out.printf("%s%s%s\n",COLOR_BLUE_BOLD,appTitle,RESET);
            System.out.println("+".concat("-".repeat(40)).concat("+"));

            switch (screen){
                case DASHBOARD:
                System.out.println("\t[1] Create New Account\n\t[2] Deposit\n\t[3] Withdraw\n\t[4] Transfer\n\t[5] Check Account Balance\n\t[6] Remove Account\n\t[7] Exit");
                System.out.println();
                System.out.print("Enter option: ");
                int option = scanner.nextInt();
                scanner.nextLine();
                
                switch (option){
                    case 1:screen = ADD_CUTOMER;
                    break;
                    case 2:screen = DEPOSIT;
                        break;
                    case 3:screen = WITHDRAW;
                        break;
                    case 4:screen = TRANSFER;
                        break;
                    case 5:screen = CHECK_ACC_BALANCE;
                        break;
                    case 6:screen = DELETE_ACC;
                        break;
                    case 7:;System.out.println(CLEAR);
                        System.exit(0);
                        break;
                    default:
                        continue;
                }
                break;
            
                case ADD_CUTOMER:
                    accNumber = String.format("SDB-%05d\n",customerDetails.length+1);
                    System.out.printf("Account No: %s\n",accNumber);
                    
                    // name validation
                    do{ 
                        System.out.print("Name: ");
                        name = scanner.nextLine().strip();

                        if (isBlank( name)){
                            System.out.printf(ERROR_MSG,"Name can't be empty");
                            valid = false;
                            continue;
                        }else if(!followFormat(name)){
                            System.out.printf(ERROR_MSG,"Invalid name");
                            valid = false;
                            continue;
                        }else valid = true;

                    }while(!valid);

                    // Initial amount validation
                    do{
                        System.out.print("Initial Deposit Rs: ");
                        initialDeposit = scanner.nextDouble();
                        scanner.nextLine();
                        if (isBlank(""+initialDeposit)){
                            System.out.printf(ERROR_MSG,"Name can't be empty");
                            valid = false;
                            continue;
                        }if (initialDeposit < 5000){
                            System.out.printf(ERROR_MSG,"Minimun amout should be RS.5000.00!!");
                            valid = false;
                            continue ;
                        }else {
                            valid = true;     
                        }
    
                    }while(!valid);

                    System.out.printf(SUCCESS_MSG,accNumber +","+ name +" has been successfully added....\n",accNumber,name);

                    //Add initial customer details
                    String [][] newCustomer = new String[customerDetails.length+1][3];
                    for (int i = 0; i < customerDetails.length; i++) {
                        
                        newCustomer[i] = customerDetails[i];
                    }
                    newCustomer[newCustomer.length-1][0] = accNumber;
                    newCustomer[newCustomer.length-1][1] = name;
                    newCustomer[newCustomer.length-1][2] = ""+ initialDeposit;
                    customerDetails = newCustomer;

                    System.out.print("Do you want add another Account (Y/n)?");
                    if (scanner.nextLine().strip().toUpperCase().equals("Y") ){
                        screen = ADD_CUTOMER; 
                        continue ; 
                    }else{
                        screen = DASHBOARD;
                        break;
                    } 
                    //break;
                    case DEPOSIT:
                        
                        valid = false;
                        int existIndex;
                        do{
                            System.out.print("Enter Account number:");
                            accNumber = scanner.nextLine().strip();
                            valid = accNumberIsValid(accNumber,customerDetails);

                        }while(!valid);

                        existIndex = existIndex(accNumber,customerDetails);
                        System.out.printf("Current Balance: Rs.%,.2f\n",Double.valueOf(customerDetails[existIndex][2]));
                        System.out.print("Deposit Amount: ");
                        depositAmount = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.printf("New Balance: %,.2f\n",Double.valueOf(customerDetails[existIndex][2])+depositAmount);
                        customerDetails[existIndex][2] = ""+ (Double.valueOf(customerDetails[existIndex][2])+depositAmount);

                        System.out.print("Do you want add another Account (Y/n)?");
                        if (scanner.nextLine().strip().toUpperCase().equals("Y") ){
                            screen = DEPOSIT; 
                            continue ; 
                        }else{
                        screen = DASHBOARD;
                            continue;
                        }

                    case WITHDRAW:
                        valid = false;
                        do{
                            System.out.print("Enter Account number:");
                            accNumber = scanner.nextLine().strip();
                            valid = accNumberIsValid(accNumber,customerDetails);

                        }while(!valid);

                        existIndex = existIndex(accNumber,customerDetails);
                        System.out.printf("Current Balance: Rs.%,.2f\n",Double.valueOf(customerDetails[existIndex][2]));
                            
                        newBalance = newBlanaceAfterWithdraw(customerDetails,existIndex);
                        System.out.printf("New Balance: %,.2f\n",newBalance);
                        customerDetails[existIndex][2] = ""+newBalance;

                        System.out.print("Do you want to withdraw again (Y/n)?");
                        if (scanner.nextLine().strip().toUpperCase().equals("Y") ){
                            screen = WITHDRAW; 
                        }else{
                            screen = DASHBOARD;
                        }
                    break;
                    
                    case TRANSFER:
                        valid = false;
                    label:{
                        do{
                            System.out.print("Enter from Account number:");
                            accNumber = scanner.nextLine().strip();
                            valid = accNumberIsValid(accNumber,customerDetails);

                        }while(!valid);
                        
                        int fromAccExistIndex = existIndex(accNumber,customerDetails);
                        System.out.printf("Name: %s\n",customerDetails[fromAccExistIndex][1]);
                        double fromBalance = Double.valueOf(customerDetails[fromAccExistIndex][2]);
                        System.out.printf("Current Balance: Rs.%,.2f\n",Double.valueOf(customerDetails[fromAccExistIndex][2]));
                    
                        do{
                            System.out.print("Enter To Account number:");
                            accNumber = scanner.nextLine().strip();
                            valid = accNumberIsValid(accNumber,customerDetails);

                        }while(!valid);
                        
                        double toBalance;
                        do{
                            int toAccExistIndex = existIndex(accNumber,customerDetails);
                            System.out.printf("Name: %s\n",customerDetails[toAccExistIndex][1]);
                            toBalance = Double.valueOf(customerDetails[toAccExistIndex][2]);
                            System.out.printf("Current Balance: Rs.%,.2f\n",Double.valueOf(customerDetails[toAccExistIndex][2]));
                            System.out.print("Transfer Amount: ");
                            double transferAmount = scanner.nextDouble();
                            scanner.nextLine();
                            if(transferAmount<100){
                                System.out.printf(ERROR_MSG,"Minimum transfer Amount is RS.100!!");
                                System.out.print("Do you want do another Transefer (Y/n)?");
                                if (scanner.nextLine().strip().toUpperCase().equals("Y") ){
                                    screen = TRANSFER; 
                                    valid = false;
                                    break;
                                }else{
                                    screen = DASHBOARD;
                                    break;
                                }
                            }
                            newBalanceFromAcc = fromBalance - 1.02*(transferAmount);
                            if(newBalanceFromAcc < 500){
                                newBalanceFromAcc +=  1.02*(transferAmount);
                                System.out.printf(ERROR_MSG,"Insuficiennt Amount!!\n");
                                System.out.print("Do you want do another Transefer (Y/n)?");
                                if (scanner.nextLine().strip().toUpperCase().equals("Y") ){
                                    screen = TRANSFER; 
                                    valid = false;
                                    break;
                                }else{
                                    screen = DASHBOARD;
                                    break;
                                }
                            }else{
                                newBalanceToAcc = toBalance + transferAmount;
                                customerDetails[toAccExistIndex][2] = ""+ newBalanceToAcc;
                                customerDetails[fromAccExistIndex][2] = ""+newBalanceFromAcc;
                                valid = true;
                            }
                            System.out.printf("New From Account Balance: %,.2f\n",newBalanceFromAcc);
                            System.out.printf("New To account Balance: %,.2f\n",newBalanceToAcc);

                            System.out.print("Do you want do another Transfer (Y/n)?");
                            if (scanner.nextLine().strip().toUpperCase().equals("Y") ){
                                screen = TRANSFER; 
                                valid = false;
                                break; 
                            }else{
                                screen = DASHBOARD;
                                valid = true;
                                break;
                            }        
                        }while(!valid);
                    }
                    break;

                    case CHECK_ACC_BALANCE:
                        do{
                            System.out.print("Enter from Account number:");
                            accNumber = scanner.nextLine().strip();
                            valid = accNumberIsValid(accNumber,customerDetails);
                            if (valid) break;
                            else{
                                System.out.print("Do you want to check another Acc (Y/n)?");
                                if (scanner.nextLine().strip().toUpperCase().equals("Y") ){
                                    screen = CHECK_ACC_BALANCE; 
                                    valid = false;
                                    continue;
                                    //break; 
                                }else{
                                    screen = DASHBOARD;
                                    valid = true;
                                    break;
                                }  
                            }

                        }while(!valid);
                        
                        int ExistIndex = existIndex(accNumber,customerDetails);
                        if (ExistIndex>=0){
                        System.out.printf("Name: %s\n",customerDetails[ExistIndex][1]);
                        double accBalance = Double.valueOf(customerDetails[ExistIndex][2]);
                        System.out.printf("Current Balance: Rs.%.2f\n",Double.valueOf(customerDetails[ExistIndex][2]));
                        System.out.printf("Balance Available for withdraw: Rs.%,.2f\n",accBalance - 500);
                        System.out.print("Do you want to check another Acc (Y/n)?");
                            if (scanner.nextLine().strip().toUpperCase().equals("Y") ){
                                screen = CHECK_ACC_BALANCE; 
                                valid = false;
                                //break; 
                            }else{
                                screen = DASHBOARD;
                                valid = true;
                                //break;
                            }  
                        }      
                        break;
                    case DELETE_ACC:
                        do{
                            System.out.print("Enter from Account number:");
                            accNumber = scanner.nextLine().strip();
                            valid = accNumberIsValid(accNumber,customerDetails);

                        }while(!valid);
                        ExistIndex = existIndex(accNumber,customerDetails);
                        System.out.printf("Name: %s\n",customerDetails[ExistIndex][1]);
                        double accBalance = Double.valueOf(customerDetails[ExistIndex][2]);
                        System.out.printf("Current Balance: Rs.%.2f\n",Double.valueOf(customerDetails[ExistIndex][2]));

                        System.out.print("Are you sure do you want to delete this account ?(Y/n)");
                            if (scanner.nextLine().strip().toUpperCase().equals("Y") ){
                                String msg = String.format("Acc no: %s,Successfully Deleted.......\n",customerDetails[ExistIndex][0]);
                                System.out.printf(SUCCESS_MSG,msg);
                                newCustomer = new String[customerDetails.length-1][3];
                                for (int i = 0; i < customerDetails.length; i++) {
                                    if (i < ExistIndex){
                                    newCustomer[i] = customerDetails[i];
                                    }else if(i == ExistIndex){
                                        continue;
                                    }
                                    newCustomer[i-1] = customerDetails[i];
                                }
                                customerDetails = newCustomer;
                                System.out.print("Do you want to delete another Acc (Y/n)?");
                                if (scanner.nextLine().strip().toUpperCase().equals("Y") ){
                                    screen = DELETE_ACC; 
                                    valid = false;
                                }else{
                                    screen = DASHBOARD;
                                    valid = true;
                                }                 
                            }else{
                                screen = DASHBOARD;
                                valid = true;
                            }  
            }
        }while(true);
    }
    
    //Check blanks
    public static boolean isBlank(String input){
        if (input.isBlank()) {
            return true;   
        }
        return false;    
    }

    //check whether A-Z / a-z and no Digits
    public static boolean followFormat(String input){
        for (int i = 0; i < input.length(); i++) {
            if (!(Character.isLetter(input.charAt(i)) || Character.isWhitespace(input.charAt(i)))) {
                return false;   
            }
        }
        return true;
    }

    //Acc no format validation;
    public static boolean accNumberIsValid(String accNumber,String[][] customerDetails){
        final String CLEAR = "\033[H\033[2J";
        final String COLOR_BLUE_BOLD = "\033[34;1m";
        final String COLOR_RED_BOLD = "\033[31;1m";
        final String COLOR_GREEN_BOLD = "\033[33;1m";
        final String RESET = "\033[0m";

        final String ERROR_MSG = String.format("\t%s%s%s\n", COLOR_RED_BOLD, "%s", RESET);
        boolean valid = true;
       
        for (int i = 4; i < accNumber.length(); i++) {
            if (!Character.isDigit(Integer.valueOf(accNumber.charAt(i)))){
                System.out.printf(ERROR_MSG,"Invalid Account number!!");
                return false;
            }
        }
        for (int i = 0; i < customerDetails.length; i++) {
            if (customerDetails[i][0].strip().equals(accNumber.strip())){
                return true; 
            }
        }   
        if(isBlank(accNumber)){
            System.out.printf(ERROR_MSG,"Account number can't be empty!!\n");
            return false;    
        }
        if(!(accNumber.startsWith("SDB-") && accNumber.length()==9) ){
            System.out.printf(ERROR_MSG,"invalid format!!");
            return false;
        }   
        System.out.printf(ERROR_MSG,"Account number doesn't exist!!\n");
        return false;
    }

    // Exist Index
    public static int existIndex (String input ,String[][] customerDetails){
        int existIndex = -1;
        for (int j = 0; j < customerDetails.length; j++) {
            if(customerDetails[j][0].strip().equals(input)){
                existIndex = j;
                break;
            }existIndex = -1;
        }
        return existIndex;     
    }

    public static double newBlanaceAfterWithdraw(String[][] customerDetails,int existIndex){
        final String COLOR_RED_BOLD = "\033[31;1m";
        final String RESET = "\033[0m";
        final String ERROR_MSG = String.format("\t%s%s%s\n", COLOR_RED_BOLD, "%s", RESET);
            
            double newBalance;
 
            System.out.print("Withdraw Amount: ");
            double withdrawAmount = scanner.nextDouble();
            scanner.nextLine();
            newBalance = Double.valueOf(customerDetails[existIndex][2]) - withdrawAmount;

            if (newBalance < 500){ 
                newBalance = Double.valueOf(customerDetails[existIndex][2]);
                System.out.printf(ERROR_MSG,"Insuficient Amount!!\n");

            }else{
                newBalance = Double.valueOf(customerDetails[existIndex][2]) - withdrawAmount;
            }
            return newBalance;
        
    }

}
