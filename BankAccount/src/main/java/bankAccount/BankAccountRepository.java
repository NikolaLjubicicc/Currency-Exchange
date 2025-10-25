package bankAccount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccountModel, Long> {

	BankAccountModel findByUserEmail(String email);

	void deleteByUserEmail(String email);
}