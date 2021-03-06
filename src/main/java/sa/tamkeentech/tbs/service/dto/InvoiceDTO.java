package sa.tamkeentech.tbs.service.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import lombok.*;
import sa.tamkeentech.tbs.domain.PaymentMethod;
import sa.tamkeentech.tbs.domain.enumeration.InvoiceStatus;
import sa.tamkeentech.tbs.domain.enumeration.PaymentStatus;

/**
 * A DTO for the {@link sa.tamkeentech.tbs.domain.Invoice} entity.
 */
@ApiModel(description = "The InvoiceDTO entity.")
@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InvoiceDTO implements Serializable {

    private Long id;

    private CustomerDTO customer;

    private InvoiceStatus status;

    private Long number;

    private Long accountId;

    private String note;

    private ZonedDateTime dueDate;

    private ZonedDateTime expiryDate;

    private BigDecimal subtotal;

    private BigDecimal amount;

    private DiscountDTO discount;

    private ClientDTO client;

    private ZonedDateTime createdDate;

    private PaymentStatus paymentStatus;

    private List<InvoiceItemDTO> invoiceItems;

    private PaymentMethod paymentMethod;

    private String billNumber; //RESP

    private BigDecimal taxFees;

    private Integer expirationDays;

    private String vatNumber;

    private Integer billerId;

    private String companyName;

    public InvoiceDTO() {
    }
}
