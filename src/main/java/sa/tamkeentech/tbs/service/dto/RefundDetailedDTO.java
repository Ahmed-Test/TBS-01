package sa.tamkeentech.tbs.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sa.tamkeentech.tbs.domain.enumeration.RequestStatus;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link sa.tamkeentech.tbs.domain.Refund} entity.
 */
@ApiModel(description = "Refund Detailed DTO.")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RefundDetailedDTO implements Serializable {

    private Long id;
    @NotBlank
    private Long accountId;
    private String customerId;

    private BigDecimal amount;
    private RequestStatus status;

    // ...
    // private Long refundId;

    // reporting
    private String formattedModifiedDate;
    private String bankId;
    private String clientName;
    private PaymentMethodDTO paymentMethod;

}
