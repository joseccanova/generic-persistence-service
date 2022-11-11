package org.nanotek.ormservice.api;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.mediatype.problem.Problem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class ApiProblem extends Problem{
	
	private String id; 
	
	private List<?> messages;

	public ApiProblem() {
		super();
		generateId();
	}
	
	public ApiProblem(URI type, String title, int status, String detail, URI instance) {
		super(type, title, status, detail, instance);
		generateId();
	}

	private void generateId() {
		id = UUID.randomUUID().toString();		
	}


	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}


	public List<?> getMessages() {
		return messages;
	}

	public ApiProblem setMessages(List<?> messages) {
		this.messages = messages;
		return this;
	}

}
