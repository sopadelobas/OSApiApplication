package br.dev.smds.OSApiApplication.api.exceptionhandler;

import br.dev.smds.OSApiApplication.domain.exception.DomainException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    
    private static Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ProblemaException problema = new ProblemaException();
        problema.setStatus(status.value());
        problema.setTitulo("Um ou mais campos inválidos! Tente novamente.");
        problema.setDataHora(LocalDateTime.now());

        List<ProblemaException.CampoProblema> camposComErro = new ArrayList<ProblemaException.CampoProblema>();
        
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String nomeCampo = ((FieldError) error).getField();
            String mensagemCampo = error.getDefaultMessage();
            
             camposComErro.add(new ProblemaException.CampoProblema(nomeCampo, mensagemCampo));
        }
        
        problema.setCampos(camposComErro);

        return super.handleExceptionInternal(ex, problema, headers, status, request);
                     
    }
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Object> handleDomainException(DomainException ex,WebRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        ProblemaException problema = new ProblemaException();
        problema.setStatus(status.value());
        problema.setTitulo(ex.getMessage());
        problema.setDataHora(LocalDateTime.now());
        
        return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
        
    }
}
