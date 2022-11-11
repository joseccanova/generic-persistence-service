package org.nanotek.ormservice.controller;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path.Node;

import org.nanotek.ormservice.Holder;
import org.nanotek.ormservice.api.ApiProblem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
public class BaseExceptionHandlerControllerAdvice {

	@Autowired
	private MessageSource messageSource;
	
//	@Autowired
//	private AcceptHeaderLocaleResolver acceptHeaderLocaleResolver;
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiProblem> constraintViolation(HttpServletRequest req, ConstraintViolationException  e) {
		ApiProblem problem =  createApiProblem(req, "data.integrity", HttpStatus.CONFLICT, null);
		problem.setMessages(buildMessageList(req , e));
		return new ResponseEntity<>(problem , problem.getStatus());
	}
	
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiProblem> acessDenied(HttpServletRequest req, AccessDeniedException  e) {
		ApiProblem problem =  createApiProblem(req, "data.integrity", HttpStatus.UNAUTHORIZED, null);
		return new ResponseEntity<>(problem , problem.getStatus());
	}

	//TODO: Fix the runtime exception class
	@ExceptionHandler(RuntimeException.class)
	public  ResponseEntity<ApiProblem>  constraintViolation(HttpServletRequest req, RuntimeException  e) {
		StringBuffer sb = new StringBuffer();
		sb.append(e.getMessage());
		ApiProblem problem =  createApiProblem(req, "data.integrity" , HttpStatus.CONFLICT  , sb.toString());
		return new ResponseEntity<>(problem , problem.getStatus());
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public  ResponseEntity<ApiProblem>  dataIntegrityViolationException(HttpServletRequest req, DataIntegrityViolationException  e) {
		StringBuffer sb = new StringBuffer();
		sb.append(e.getMessage());
		ApiProblem problem =  createApiProblem(req, "data.integrity" , HttpStatus.CONFLICT  , sb.toString());
		return new ResponseEntity<>(problem , problem.getStatus());
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public  ResponseEntity<ApiProblem>  noSuchElementException(HttpServletRequest req, NoSuchElementException  e) {
		StringBuffer sb = new StringBuffer();
		sb.append(e.getMessage());
		ApiProblem problem =  createApiProblem(req, "data.integrity" , HttpStatus.NOT_FOUND  , sb.toString());
		return new ResponseEntity<>(problem , problem.getStatus());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public  ResponseEntity<ApiProblem>  illegalArgumentException(HttpServletRequest req, IllegalArgumentException  e) {
		StringBuffer sb = new StringBuffer();
		sb.append(e.getMessage());
		ApiProblem problem =  createApiProblem(req, "data.integrity" , HttpStatus.BAD_REQUEST  , sb.toString());
		return new ResponseEntity<>(problem , problem.getStatus());
	}
	
	private List<ConstraintMessagePair> buildMessageList(HttpServletRequest req , ConstraintViolationException e) {
		List<ConstraintMessagePair> messages = new ArrayList<>();
		e
		.getConstraintViolations()
		.forEach(ex ->{
			Object ob = ex.getInvalidValue();
			Object ob1 = ex.getLeafBean();
			String path = Optional
					 .ofNullable(ex.getPropertyPath())
					.map(p -> {
									Optional<String> beanPath = getBeanPathNode(p.spliterator()); 
									return beanPath.isPresent()?beanPath.get():p.toString();
					}).orElse("");
			messages.add(new ConstraintMessagePair(path, ex.getMessage(), ob , ob1));
		});
		return messages;
	}
	
	public Optional<String> getBeanPathNode(Spliterator<Node> iterator) {
		Holder<String> holder = new Holder<>();
		List<Node> pathJoin = new ArrayList<>();
		iterator
		.forEachRemaining(p -> {
			if (p.getKind().equals(ElementKind.PROPERTY) 
						|| p.getKind().equals(ElementKind.PARAMETER) 
						|| p.getKind().equals(ElementKind.BEAN) ) {
				pathJoin.add(p);
			}
		});
		if (pathJoin.size() > 0) {
			String path = pathJoin.stream()
                    .map(Node::toString)
                    .collect(Collectors.joining("."));
			holder.put(path);
		}
		return holder.get();
	}
	
	//TODO: Fix Locale Resolver
	private ApiProblem createApiProblem(HttpServletRequest req , String titleKey , HttpStatus status , String detail) {
		URI uri  = URI.create(req.getRequestURI());
		Locale locale = Optional
//						.ofNullable(acceptHeaderLocaleResolver.resolveLocale(req))
						.of(Locale.getDefault()).get();
		String title = messageSource.getMessage(titleKey, null, locale);
		ApiProblem problem = new ApiProblem(uri, title , status.value() , detail,  null);
		return problem;
	};
	
	@JsonInclude(Include.NON_NULL)
	class ConstraintMessagePair {
		
		private String path; 
		
		private String message;

		private Object object;

		private Object leafBean;
		
		
		public ConstraintMessagePair() {
			super();
		}

		public ConstraintMessagePair(String path, String message) {
			super();
			this.path = path;
			this.message = message;
		}

		public ConstraintMessagePair(String path2, String message2, Object ob) {
			super();
			this.path = path2;
			this.message = message2;
			this.object = ob;
		}

		public ConstraintMessagePair(String path2, String message2, Object ob, Object ob1) {
			super();
			this.path = path2;
			this.message = message2;
			this.object = ob;
			this.leafBean = ob1;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		@Override
		public String toString() {
			return " {path:" + path + ", message:" + message + "} ";
		}

		public Object getObject() {
			return object;
		}

		public void setObject(Object object) {
			this.object = object;
		}

		public Object getLeafBean() {
			return leafBean;
		}

		public void setLeafBean(Object leafBean) {
			this.leafBean = leafBean;
		}
		
	}
	
}