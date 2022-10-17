import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter

@Entity

@Table(name = "payments_table")

public class PaymentsSchedule implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant date;

    @Column(name = "value")
    private Double paymentValue;

    @Enumerated(EnumType.STRING)
    private Integer paymentStatus;

    public PaymentsSchedule() {
        setPaymentStatus(PaymentStatus.PENDING);
    }

    public PaymentsSchedule(Long id, Instant date, Double paymentValue) {
        this.id = id;
        this.date = date;
        this.paymentValue = paymentValue;
        setPaymentStatus(PaymentStatus.PENDING);
    }

    public PaymentStatus getPaymentStatus() {
        return PaymentStatus.valueOf(paymentStatus);
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus.getCode();
    }

}