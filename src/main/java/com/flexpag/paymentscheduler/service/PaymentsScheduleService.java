import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentsScheduleService {

    @Autowired
    PaymentsScheduleRepository paymentsScheduleRepository;

	//busca todos
    public List<PaymentsSchedule> findAll() {
        return paymentsScheduleRepository.findAll();
    }

	//busca por id
    public Optional<PaymentsSchedule> findById(Long id) {
        if (!PaymentsSchedule.isPresent()) {
            throw new NotFoundException("Payment id not found (id: " + id+").");
        }
        return paymentsScheduleRepository.findById(id);
    }

    //busca Status com id
	public Optional<PaymentStatus> findStatusById(Long id) {
		return paymentRepository.findById(id);
    }

    @Transactional
    public PaymentsSchedule save(PaymentsSchedule paymentsSchedule) {
        return paymentsScheduleRepository.save(paymentsSchedule);
    }

    @Transactional
    public void delete(PaymentsSchedule paymentsSchedule) {
        paymentsScheduleRepository.delete(paymentsSchedule);
    }
}