package org.nanotek.ormservice;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class EntityExceptionSupplierFactory {

	@Autowired
	private MessageSource messageSource; 
	
	public EntityExceptionSupplierFactory() {
	}

	public  Supplier<RuntimeException> applyMessage(String messageKey) {
		Holder<String> holder = new Holder<>();
		try {
			String message = messageSource.getMessage(messageKey, null, LocaleContext.getCurrentLocale());
			holder.put(message);
		}catch(Exception ex) {}
	    return () -> new RuntimeException(holder.get().orElse(""));
	}
}
