package cryptoWallet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.CryptoWalletDto;
import api.services.CryptoWalletService;

@RestController
public class CryptoWalletServiceImpl implements CryptoWalletService {

	@Autowired
	private CryptoWalletRepository repo;

	@Override
	public ResponseEntity<?> getCryptoWalletByEmail(String email, String callerRole, String callerEmail) {
		if ("USER".equals(callerRole) && !callerEmail.equals(email)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("USER can only view their own wallet");
		}

		CryptoWalletModel wallet = repo.findByUserEmail(email);

		if (wallet == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Crypto wallet not found for user: " + email);
		}

		return ResponseEntity.ok(convertModelToDto(wallet));
	}

	@Override
	public ResponseEntity<?> getAllCryptoWallets(String callerRole) {
		if (!"ADMIN".equals(callerRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Only ADMIN can view all wallets");
		}

		List<CryptoWalletModel> wallets = repo.findAll();
		List<CryptoWalletDto> dtos = new ArrayList<>();
		for (CryptoWalletModel wallet : wallets) {
			dtos.add(convertModelToDto(wallet));
		}

		return ResponseEntity.ok(dtos);
	}

	@Override
	public ResponseEntity<?> createCryptoWallet(CryptoWalletDto dto, String callerRole) {
		if (!"ADMIN".equals(callerRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Only ADMIN can create wallets");
		}

		if (repo.findByUserEmail(dto.getUserEmail()) != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Crypto wallet already exists for user: " + dto.getUserEmail());
		}

		CryptoWalletModel wallet = convertDtoToModel(dto);
		CryptoWalletModel saved = repo.save(wallet);

		return ResponseEntity.status(HttpStatus.CREATED).body(convertModelToDto(saved));
	}

	@Override
	public ResponseEntity<?> updateCryptoWallet(CryptoWalletDto dto, String callerRole, String callerEmail) {
		// USER može da update-uje samo svoj wallet, ADMIN može bilo čiji
		if ("USER".equals(callerRole) && !callerEmail.equals(dto.getUserEmail())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("USER can only update their own wallet");
		}

		if (!"ADMIN".equals(callerRole) && !"USER".equals(callerRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Only ADMIN or USER can update wallets");
		}

		CryptoWalletModel existing = repo.findByUserEmail(dto.getUserEmail());

		if (existing == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Crypto wallet not found for user: " + dto.getUserEmail());
		}

		existing.setBtcBalance(dto.getBtcBalance());
		existing.setEthBalance(dto.getEthBalance());
		existing.setUsdtBalance(dto.getUsdtBalance());
		existing.setBnbBalance(dto.getBnbBalance());
		existing.setAdaBalance(dto.getAdaBalance());

		CryptoWalletModel updated = repo.save(existing);

		return ResponseEntity.ok(convertModelToDto(updated));
	}

	@Override
	public ResponseEntity<?> deleteCryptoWallet(String email, String callerRole) {
		if (!"ADMIN".equals(callerRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Only ADMIN can delete wallets");
		}

		CryptoWalletModel existing = repo.findByUserEmail(email);

		if (existing == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Crypto wallet not found for user: " + email);
		}

		repo.deleteByUserEmail(email);

		return ResponseEntity.ok("Crypto wallet for user " + email + " deleted successfully");
	}

	public CryptoWalletDto convertModelToDto(CryptoWalletModel model) {
		return new CryptoWalletDto(model.getId(), model.getUserEmail(), model.getBtcBalance(), model.getEthBalance(),
				model.getUsdtBalance(), model.getBnbBalance(), model.getAdaBalance());
	}

	public CryptoWalletModel convertDtoToModel(CryptoWalletDto dto) {
		CryptoWalletModel model = new CryptoWalletModel();
		model.setUserEmail(dto.getUserEmail());
		model.setBtcBalance(dto.getBtcBalance() != null ? dto.getBtcBalance() : BigDecimal.ZERO);
		model.setEthBalance(dto.getEthBalance() != null ? dto.getEthBalance() : BigDecimal.ZERO);
		model.setUsdtBalance(dto.getUsdtBalance() != null ? dto.getUsdtBalance() : BigDecimal.ZERO);
		model.setBnbBalance(dto.getBnbBalance() != null ? dto.getBnbBalance() : BigDecimal.ZERO);
		model.setAdaBalance(dto.getAdaBalance() != null ? dto.getAdaBalance() : BigDecimal.ZERO);
		return model;
	}
}