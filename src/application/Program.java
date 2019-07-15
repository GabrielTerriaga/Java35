package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import model.entities.Contract;
import model.entities.Installment;
import model.services.ContractService;
import model.services.PaypalService;

public class Program {

	public static void main(String[] args) throws ParseException {

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		
		System.out.println("Enter contract data");
		System.out.print("Number: ");
		int number = sc.nextInt();
		System.out.print("Date (dd/MM/yyyy): ");
		Date date = sdf.parse(sc.next());
		System.out.print("Contract value: ");
		double totalValue = sc.nextDouble();
		
		//obj para enviar entrada de dados
		Contract ct = new Contract(number, date, totalValue);
		
		//entrada numero de parcelas
		System.out.print("Enter number of installments: ");
		int n = sc.nextInt();

		//construtor com upcasting das classes serviço
		ContractService contractService = new ContractService(new PaypalService());
		//enviando os dados de contrato
		contractService.processContract(ct, n);

		
		//mostrando na tela percorrendo array
		System.out.println("Installments:");

		for (Installment x : ct.getInstallments()) {

			System.out.println(x);
		}
			
	sc.close();
	}

}
