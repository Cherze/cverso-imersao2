package caixa.verso.exception;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ValidationExceptionMapperTest {

    static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void deveRetornar400ComMapaDeErros() {
        // Bean inválido proposital
        class Bean {
            @NotNull
            String obrigatorio;
        }
        Bean bean = new Bean();
        Set<ConstraintViolation<Bean>> violations = validator.validate(bean);
        ConstraintViolationException ex = new ConstraintViolationException(violations);

        ValidationExceptionMapper mapper = new ValidationExceptionMapper();
        Response response = mapper.toResponse(ex);

        assertEquals(400, response.getStatus());
        assertTrue(response.getEntity() instanceof java.util.Map);
        @SuppressWarnings("unchecked")
        java.util.Map<String, String> body = (java.util.Map<String, String>) response.getEntity();
        assertFalse(body.isEmpty());
        // A chave será "obrigatorio" (nome do campo)
        assertTrue(body.containsKey("obrigatorio"));
    }
}
