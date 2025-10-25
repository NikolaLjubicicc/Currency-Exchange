package cryptoWallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface CryptoWalletRepository extends JpaRepository<CryptoWalletModel, Long> {

	CryptoWalletModel findByUserEmail(String userEmail);

	@Modifying
	@Transactional
	@Query("delete from CryptoWalletModel c where c.userEmail=?1")
	void deleteByUserEmail(String userEmail);
}