import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/payments")
public class PaymentsScheduleController {

    @Autowired
    private PaymentsScheduleService paymentsScheduleService;

    @GetMapping
    public ResponseEntity<List<PaymentsSchedule>> findAll() {
        List<PaymentsSchedule> list = paymentsScheduleService.findAll();
        for (PaymentsSchedule ps : list) {
            if (ps.getDate().equals(Instant.now()) || ps.getDate().isBefore(Instant.now())) {
                ps.setPaymentStatus(PaymentStatus.PAID);
                paymentsScheduleService.save(ps);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(paymentsScheduleService.findAll());
    }
//listar
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        Optional<PaymentsSchedule> paymentsScheduleId = paymentsScheduleService.findById(id);

        if (paymentsScheduleId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamento de pagamento invalido.");
        }

        else if (paymentsScheduleId.get().getDate().equals(Instant.now()) ||
                paymentsScheduleId.get().getDate().isBefore(Instant.now())) {
            paymentsScheduleId.get().setPaymentStatus(PaymentStatus.PAID);
            paymentsScheduleId.save(paymentsScheduleId.get());
        }

        return ResponseEntity.status(HttpStatus.OK).body(paymentsScheduleId.get());
    }
//salvar
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody PaymentsSchedule paymentsSchedule) {
        if (paymentsSchedule.getDate().equals(Instant.now()) || paymentsSchedule.getDate().isBefore(Instant.now())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A data e horário devem ser posteriores ao atual.");
        }
        paymentsSchedule.setPaymentStatus(PaymentStatus.PENDING);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentsScheduleService.save(paymentsSchedule));
    }
//atualizar
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody PaymentsSchedule paymentsSchedule) {
        Optional<PaymentsSchedule> paymentsScheduleId = paymentsScheduleService.findById(id);

        if (paymentsScheduleId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id não encontrado.");
        }

        else if (paymentsScheduleId.get().getDate().equals(Instant.now()) ||
                paymentsScheduleId.get().getDate().isBefore(Instant.now())) {
            paymentsScheduleId.get().setPaymentStatus(PaymentStatus.PAID);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Pagamento efetuado.");
        }

        else if (paymentsSchedule.getDate().equals(Instant.now()) ||
                paymentsSchedule.getDate().isBefore(Instant.now())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A data e horário devem ser posteriores ao atual.");
        }

        var updatedPaymentsSchedule = new PaymentsSchedule();
        BeanUtils.copyProperties(paymentsSchedule, updatedPaymentsSchedule);
        updatedPaymentsSchedule.setId(paymentsScheduleId.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(paymentsScheduleService.save(updatedPaymentsSchedule));
    }
//deletar
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<PaymentsSchedule> paymentsScheduleId = paymentsScheduleService.findById(id);

        if (paymentsScheduleId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id não encontrado.");
        }

        else if (paymentsScheduleId.get().getDate().isAfter(Instant.now())) {
            paymentsScheduleService.delete(paymentsScheduleId.get());
            return ResponseEntity.status(HttpStatus.OK).body("Agendamento apagado.");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Pagamento já foi efetuado.");
    }
}