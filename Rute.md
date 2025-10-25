Dodavanje korisnika:
admin@uns.ac.rs
password
http://localhost:8765/users/newUser
{
      "email": "newuser@uns.ac.rs",
      "password": "password",
      "role": "USER"
}

Lista svih usera:
owner@uns.ac.rs ili admin@uns.ac.rs
password
http://localhost:8765/users

Brisanje odredjenog usera
owner@uns.ac.rs
password
http://localhost:8765/users?email=user@uns.ac.rs

Pretraga odredjenog usera:
admin@uns.ac.rs ili owner@uns.ac.rs
http://localhost:8765/bank-account?email=user@uns.ac.rs

Azuriranje bank-account:
admin@uns.ac.rs 
password
http://localhost:8765/bank-account
{
      "id": 1,
      "userEmail": "user@uns.ac.rs",
      "eurBalance": 100000,
      "usdBalance": 10000,
      "gbpBalance": 10000,
      "chfBalance": 10000,
      "rsdBalance": 100000
}

User pregleda svoj wallet
user@uns.ac.rs
password
http://localhost:8765/crypto-wallet?email=user@uns.ac.rs

Azuriranje crypto-wallet
admin@uns.ac.rs
password
http://localhost:8765/crypto-wallet
 {
      "id": 1,
      "userEmail": "user@uns.ac.rs",
      "btcBalance": 50000,
      "ethBalance": 50,
      "usdtBalance": 10000,
      "bnbBalance": 100,
      "adaBalance": 50000
}

Kupovina valuta
user@uns.ac.rs
password
http://localhost:8765/currency-exchange?from=GBP&to=CHF

Kupovina kripto valuta
user@uns.ac.rs
password
http://localhost:8765/crypto-exchange?from=ETH&to=USDT



















