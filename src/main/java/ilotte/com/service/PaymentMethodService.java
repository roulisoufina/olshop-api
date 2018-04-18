package ilotte.com.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ilotte.com.model.PaymentMethod;
import ilotte.com.model.PaymentMethod.PaymentType;

@Component
public class PaymentMethodService {

	private static List<PaymentMethod> paymentMethods = new ArrayList<>();

	static {
		// Initialize Data
		PaymentMethod payment1 = new PaymentMethod(1, "Kartu Kredit BCA", "BCA", PaymentType.CREDITCARD, true);
		PaymentMethod payment2 = new PaymentMethod(2, "Kartu Kredit BNI", "BNI", PaymentType.CREDITCARD, true);
		PaymentMethod payment3 = new PaymentMethod(3, "Kartu Kredit BRI", "BRI", PaymentType.CREDITCARD, true);
		PaymentMethod payment4 = new PaymentMethod(4, "Bank BCA", "BCA", PaymentType.TRANSFER, true);
		PaymentMethod payment5 = new PaymentMethod(5, "Bank Mandiri", "Mandiri", PaymentType.TRANSFER, true);
		PaymentMethod payment6 = new PaymentMethod(6, "Bank OCBC", "OCBC", PaymentType.TRANSFER, false);
		paymentMethods.add(payment1);
		paymentMethods.add(payment2);
		paymentMethods.add(payment3);
		paymentMethods.add(payment4);
		paymentMethods.add(payment5);
		paymentMethods.add(payment6);
	}

	public List<PaymentMethod> getPaymentMethods() {
		return paymentMethods;
	}

}
