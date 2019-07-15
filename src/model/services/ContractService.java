package model.services;

import java.util.Calendar;
import java.util.Date;

import model.entities.Contract;
import model.entities.Installment;

public class ContractService {

	private OnlinePaymentService onlinePaymentService;
	
	public ContractService(OnlinePaymentService onlinePaymentService) {
		this.onlinePaymentService = onlinePaymentService;
	}
	
	public void processContract(Contract contract, int months) {
		
		//Cota basica em o juros mensal do paypal 1% e a taxa de pagamento de 2%
		//Logica quota basic para calcular valor total de parcelas:
		//Pegar o valor total do contrato e dividir pela quantidade de mês (Parcelas)
		
		double basicQuota = contract.getTotalValue() / months;
		
		for (int i = 1; i <= months; i++) {
			/*
			 * Exemplo quota 1#:
			 * contract 600, parcelas 3;
			 * 200 (basicQuota) + juro(interest) * quantida de parcelas (months (i));
			 */
			
			/*
				variavel Date para adicionar a data dentro do Contract atráves do method addMonths
				o valor inteiro (é do contador i) dentro desse escopo
			*/
			
			Date date = addMonths(contract.getDate(), i);
			
			/*variavel para receber a quota atualizada após vir da classe onlinePaymentService
				com os novos valores (basicQuota + os parametro dentro do oninePaymentService)
				através do method interest somando o basic quota com basicQuota dentro do method
				e somando a quantidade de parcelas (meses) através do contador (i)
			*/
			
			double updateQuota = basicQuota + onlinePaymentService.interest(basicQuota, i);
			
			/*  variavel para receber a quota total através da updatedQuota mais o valor passado através
			    do method paymentFee com o parametro (updatedQuota)
			 */
			 
			double fullQuota = updateQuota + onlinePaymentService.paymentFee(updateQuota);
			
			//Enviando para a classe Installment (data de vencimento e valor final da parcela após juro e taxas)
			//foi preciso instanciar um novo objeto (por que nao tinha sido instanciado em momento algum um 
			//objeto to tipo Installment) para passar os parametros
			contract.addInstallment(new Installment(date, fullQuota));
		}
	}
	
	//method private para utilizar apenas dentro dessa classe ou pacote
	//atributos recebidos do methos processContract (contract.getDate(), i(quantidade de parcelas)
	private Date addMonths(Date date, int n) {
		
		//Calendar para manipular a data
		Calendar cal = Calendar.getInstance();
		
		//setar o valor da variavel date que está vindo do method processContract
		cal.setTime(date);
		//adicionando o valor do tipo inteiro que está no contador (i) dentro do method processContract
		cal.add(Calendar.MONTH, n);
		//returnando do calendar o momento que foi editado acima
		return cal.getTime();
	}
}
