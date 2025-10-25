package userService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.BankAccountDto;
import api.dtos.CryptoWalletDto;
import api.dtos.UserDto;
import api.proxies.BankAccountProxy;
import api.proxies.CryptoWalletProxy;
import api.services.UsersService;

@RestController
public class UserServiceImpl implements UsersService{

	@Autowired
	private UserRepository repo;

	@Autowired
	private BankAccountProxy bankAccountProxy;

	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;

	@Override
	public List<UserDto> getUsers() {
		List<UserModel> models = repo.findAll();
		List<UserDto> dtos = new ArrayList<UserDto>();
		for(UserModel model : models) {
			dtos.add(convertModelToDto(model));
		}
		return dtos;
	}

	@Override
	public UserDto getUserByEmail(String email) {
		UserModel model = repo.findByEmail(email);
		if(model == null) {
			return null;
		}
		return convertModelToDto(model);
	}

	@Override
	public ResponseEntity<?> createAdmin(UserDto dto, String callerRole) {
		if(!"OWNER".equals(callerRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Only OWNER can create ADMIN users");
		}

		if(repo.findByEmail(dto.getEmail()) == null) {
			dto.setRole("ADMIN");
			UserModel model = convertDtoToModel(dto);
			return ResponseEntity.status(HttpStatus.CREATED).body(convertModelToDto(repo.save(model)));
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Admin with passed email already exists");
		}
	}

	@Override
	public ResponseEntity<?> createUser(UserDto dto, String callerRole) {
		if(!"OWNER".equals(callerRole) && !"ADMIN".equals(callerRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Only OWNER or ADMIN can create USER");
		}

		if(repo.findByEmail(dto.getEmail()) == null) {
			dto.setRole("USER");
			UserModel model = convertDtoToModel(dto);
			UserModel savedUser = repo.save(model);

			BankAccountDto bankAccount = new BankAccountDto(null, dto.getEmail(), BigDecimal.ZERO, BigDecimal.ZERO,
					BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
			bankAccountProxy.createBankAccount(bankAccount, callerRole);

			CryptoWalletDto cryptoWallet = new CryptoWalletDto(null, dto.getEmail(), BigDecimal.ZERO, BigDecimal.ZERO,
					BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
			cryptoWalletProxy.createCryptoWallet(cryptoWallet, callerRole);

			return ResponseEntity.status(HttpStatus.CREATED).body(convertModelToDto(savedUser));
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("User with passed email already exists");
		}
	}

	@Override
	public ResponseEntity<?> createOwner(UserDto dto, String callerRole) {
		UserModel existingOwner = repo.findByRole("OWNER");
		if(existingOwner != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("OWNER already exists in the system. Only one OWNER is allowed.");
		}

		if(!"OWNER".equals(callerRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Only OWNER can create another OWNER");
		}

		if(repo.findByEmail(dto.getEmail()) == null) {
			dto.setRole("OWNER");
			UserModel model = convertDtoToModel(dto);
			return ResponseEntity.status(HttpStatus.CREATED).body(convertModelToDto(repo.save(model)));
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Owner with passed email already exists");
		}
	}

	@Override
	public ResponseEntity<?> updateUser(UserDto dto, String callerRole, String callerEmail) {
		UserModel existingUser = repo.findByEmail(dto.getEmail());

		if(existingUser == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("User with email " + dto.getEmail() + " does not exist");
		}

		if("OWNER".equals(callerRole)) {
			repo.updateUser(dto.getEmail(), dto.getPassword(), dto.getRole());
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		} else if("ADMIN".equals(callerRole)) {
			if("USER".equals(existingUser.getRole())) {
				if(!"USER".equals(dto.getRole())) {
					return ResponseEntity.status(HttpStatus.FORBIDDEN)
							.body("ADMIN cannot change USER role to " + dto.getRole());
				}
				repo.updateUser(dto.getEmail(), dto.getPassword(), dto.getRole());
				return ResponseEntity.status(HttpStatus.OK).body(dto);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body("ADMIN can only update USER accounts");
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Insufficient permissions to update user");
		}
	}

	@Override
	public ResponseEntity<?> deleteUser(String email, String callerRole) {
		if(!"OWNER".equals(callerRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Only OWNER can delete users");
		}

		UserModel existingUser = repo.findByEmail(email);
		if(existingUser == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("User with email " + email + " does not exist");
		}

		if("USER".equals(existingUser.getRole())) {
			try {
				bankAccountProxy.deleteBankAccount(email, callerRole);
			} catch (Exception e) {
			}

			try {
				cryptoWalletProxy.deleteCryptoWallet(email, callerRole);
			} catch (Exception e) {
			}
		}

		repo.deleteByEmail(email);
		return ResponseEntity.status(HttpStatus.OK)
				.body("User with email " + email + " has been deleted successfully");
	}

	public UserDto convertModelToDto(UserModel model) {
		return new UserDto(model.getEmail(), model.getPassword(), model.getRole());
	}

	public UserModel convertDtoToModel(UserDto dto) {
		return new UserModel(dto.getEmail(), dto.getPassword(), dto.getRole());
	}

}
