package bankAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.BankAccountDto;
import api.services.BankAccountService;
import jakarta.transaction.Transactional;

@Service
public class BankAccountServiceImpl implements BankAccountService {

	@Autowired
	private BankAccountRepository repo;

	@Override
	public ResponseEntity<?> getBankAccountByEmail(@RequestParam String email,
			@RequestHeader("X-User-Role") String callerRole, @RequestHeader("X-User-Email") String callerEmail) {

		if ("OWNER".equals(callerRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("OWNER does not have access to bank accounts");
		}

		if ("USER".equals(callerRole) && !callerEmail.equals(email)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("USER can only view their own account");
		}

		BankAccountModel model = repo.findByUserEmail(email);
		if (model == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bank account not found for email: " + email);
		}

		return ResponseEntity.ok(convertModelToDto(model));
	}

	@Override
	public ResponseEntity<?> getAllBankAccounts(@RequestHeader("X-User-Role") String callerRole) {

		if (!"ADMIN".equals(callerRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can view all accounts");
		}

		List<BankAccountModel> accounts = repo.findAll();
		List<BankAccountDto> dtos = accounts.stream().map(this::convertModelToDto).collect(Collectors.toList());

		return ResponseEntity.ok(dtos);
	}

	@Override
	public ResponseEntity<?> createBankAccount(@RequestBody BankAccountDto dto,
			@RequestHeader("X-User-Role") String callerRole) {

		if (!"ADMIN".equals(callerRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can create bank accounts");
		}

		if (repo.findByUserEmail(dto.getUserEmail()) != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Bank account already exists for email: " + dto.getUserEmail());
		}

		BankAccountModel model = convertDtoToModel(dto);
		BankAccountModel savedModel = repo.save(model);

		return ResponseEntity.status(HttpStatus.CREATED).body(convertModelToDto(savedModel));
	}

	@Override
	public ResponseEntity<?> updateBankAccount(@RequestBody BankAccountDto dto,
			@RequestHeader("X-User-Role") String callerRole, @RequestHeader("X-User-Email") String callerEmail) {

		if ("USER".equals(callerRole) && !callerEmail.equals(dto.getUserEmail())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("USER can only update their own account");
		}

		if (!"ADMIN".equals(callerRole) && !"USER".equals(callerRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN or USER can update bank accounts");
		}

		BankAccountModel existingModel = repo.findByUserEmail(dto.getUserEmail());
		if (existingModel == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Bank account not found for email: " + dto.getUserEmail());
		}

		existingModel.setEurBalance(dto.getEurBalance());
		existingModel.setUsdBalance(dto.getUsdBalance());
		existingModel.setGbpBalance(dto.getGbpBalance());
		existingModel.setChfBalance(dto.getChfBalance());
		existingModel.setRsdBalance(dto.getRsdBalance());

		BankAccountModel updatedModel = repo.save(existingModel);

		return ResponseEntity.ok(convertModelToDto(updatedModel));
	}

	@Override
	@Transactional
	public ResponseEntity<?> deleteBankAccount(@RequestParam String email,
			@RequestHeader("X-User-Role") String callerRole) {

		if (!"ADMIN".equals(callerRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can delete bank accounts");
		}

		BankAccountModel existingModel = repo.findByUserEmail(email);
		if (existingModel == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bank account not found for email: " + email);
		}

		repo.deleteByUserEmail(email);

		return ResponseEntity.ok("Bank account deleted successfully for email: " + email);
	}

	private BankAccountDto convertModelToDto(BankAccountModel model) {
		return new BankAccountDto(model.getId(), model.getUserEmail(), model.getEurBalance(), model.getUsdBalance(),
				model.getGbpBalance(), model.getChfBalance(), model.getRsdBalance());
	}

	private BankAccountModel convertDtoToModel(BankAccountDto dto) {
		return new BankAccountModel(dto.getUserEmail(), dto.getEurBalance() != null ? dto.getEurBalance() : BigDecimal.ZERO,
				dto.getUsdBalance() != null ? dto.getUsdBalance() : BigDecimal.ZERO,
				dto.getGbpBalance() != null ? dto.getGbpBalance() : BigDecimal.ZERO,
				dto.getChfBalance() != null ? dto.getChfBalance() : BigDecimal.ZERO,
				dto.getRsdBalance() != null ? dto.getRsdBalance() : BigDecimal.ZERO);
	}
}