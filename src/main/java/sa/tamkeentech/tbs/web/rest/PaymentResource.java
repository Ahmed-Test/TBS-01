package sa.tamkeentech.tbs.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import sa.tamkeentech.tbs.config.Constants;
import sa.tamkeentech.tbs.domain.Payment;
import sa.tamkeentech.tbs.domain.enumeration.PaymentStatus;
import sa.tamkeentech.tbs.repository.PaymentRepository;
import sa.tamkeentech.tbs.service.PaymentService;
import sa.tamkeentech.tbs.service.dto.*;
import sa.tamkeentech.tbs.service.mapper.PaymentMapper;
import sa.tamkeentech.tbs.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sa.tamkeentech.tbs.web.rest.errors.TbsRunTimeException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link sa.tamkeentech.tbs.domain.Payment}.
 */
@RestController
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    private static final String ENTITY_NAME = "payment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${tbs.payment.check-payment-url}")
    private String checkPaymentUrl;
    private final ObjectMapper objectMapper;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentResource(ObjectMapper objectMapper, PaymentService paymentService, PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.objectMapper = objectMapper;
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    /**
     * {@code POST  /payments} : Create a new payment.
     *
     * @param paymentDTO the paymentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentDTO, or with status {@code 400 (Bad Request)} if the payment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/billing/payments/credit-card")
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO paymentDTO) throws URISyntaxException {
        log.debug("REST request to save Payment : {}", paymentDTO);
        if (paymentDTO.getId() != null) {
            throw new BadRequestAlertException("A new payment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (paymentDTO.getPaymentMethod() == null) {
            paymentDTO.setPaymentMethod(PaymentMethodDTO.builder().code(Constants.CREDIT_CARD).build());
        }
        PaymentDTO result = paymentService.prepareCreditCardPayment(paymentDTO);
        return ResponseEntity.created(new URI("/api/payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    @PostMapping("/billing/payments/status")
    public ResponseEntity<PaymentDTO> updatePaymentStatus(@RequestBody PaymentStatusResponseDTO paymentStatusResponseDTO) throws URISyntaxException {
        PaymentDTO result = paymentService.updateCreditCardPaymentAndSendEvent(paymentStatusResponseDTO);
        return ResponseEntity//.created(new URI("/api/payments/" + result.getId()))
            .ok()
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    @GetMapping("/billing/check-payment/{accountId}")
    public PaymentDTO checkPaymentStatus(@PathVariable  Long accountId) throws JSONException, IOException {
        List<Payment> payments = paymentRepository.findByInvoiceAccountIdOrderById(accountId);
        if (CollectionUtils.isEmpty(payments)) {
            throw new TbsRunTimeException("No payments found");
        }
        PaymentDTO result = null;

        for (Payment payment: payments) {
            if (payment.getStatus() == PaymentStatus.PAID) {
                result =paymentMapper.toDto(payment);
            }
        }

        // check last payment
        if (result == null) {
            for (Payment payment: payments) {
                if (payment.getPaymentMethod().getCode().equals(Constants.CREDIT_CARD)) {
                    JSONObject req = new JSONObject();
                    // Payment payment = payments.get(payments.size()-1);
                    req.put("transactionId",payment.getTransactionId());
                    HttpClient client = HttpClientBuilder.create().build();
                    HttpPost post = new HttpPost(checkPaymentUrl);
                    post.setHeader("Content-Type", "application/json");
                    post.setEntity(new StringEntity(req.toString()));
                    HttpResponse response = client.execute(post);
                    log.debug("----check payment request : {}", req);
                    PaymentStatusResponseDTO paymentStatusResponseDTO = objectMapper.readValue(response.getEntity().getContent(), PaymentStatusResponseDTO.class);
                    result = paymentService.updateCreditCardPayment(paymentStatusResponseDTO, payment, payment.getInvoice());
                    if (result.getStatus() == PaymentStatus.PAID) {
                        break;
                    }
                }
            }
        }

        PaymentDTO.PaymentDTOBuilder paymentStatus = PaymentDTO.builder().status(PaymentStatus.UNPAID);
        if (result != null) {
            paymentStatus.transactionId(result.getTransactionId())
                .status(result.getStatus())
                .paymentMethod(result.getPaymentMethod())
                .amount(result.getAmount());
        }
        return paymentStatus.build();
    }
    /**
     * {@code PUT  /payments} : Updates an existing payment.
     *
     * @param paymentDTO the paymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentDTO,
     * or with status {@code 400 (Bad Request)} if the paymentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api/payments")
    public ResponseEntity<PaymentDTO> updatePayment(@RequestBody PaymentDTO paymentDTO) throws URISyntaxException {
        log.debug("REST request to update Payment : {}", paymentDTO);
        if (paymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentDTO result = paymentService.save(paymentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payments} : get all the payments.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payments in body.
     */
    @GetMapping("/api/payments")
    public List<PaymentDTO> getAllPayments() {
        log.debug("REST request to get all Payments");
        return paymentService.findAll();
    }

    /**
     * {@code GET  /payments/:id} : get the "id" payment.
     *
     * @param id the id of the paymentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/api/payments/{id}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable Long id) {
        log.debug("REST request to get Payment : {}", id);
        Optional<PaymentDTO> paymentDTO = paymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentDTO);
    }

    /**
     *
     * @param input
     * @return
     */
    // @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/payments/datatable")
    public DataTablesOutput<PaymentDTO> getAllItems(DataTablesInput input) {
        return paymentService.get(input);
    }

    /**
     *
     * @param paymentSearchRequestDTO
     * @return
     */
     @PostMapping("/api/payments/search")
     @ResponseBody
     public DataTablesOutput<PaymentDTO> getPaymentStatusBySearching(@RequestBody PaymentSearchRequestDTO paymentSearchRequestDTO) {
     log.debug("REST request to get a page of Invoices");
     return paymentService.getPaymentStatusByQuerySearch(paymentSearchRequestDTO);
     }
}
