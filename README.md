[![Build Status](https://travis-ci.org/baso10/ethereumKeyJ.svg?branch=master)](https://travis-ci.org/baso10/ethereumKeyJ)
[![Coverage Status](https://coveralls.io/repos/github/baso10/ethereumKeyJ/badge.svg?branch=master)](https://coveralls.io/github/baso10/ethereumKeyJ?branch=master)

# Ethereum KeyJ

Java library to create Ethereum wallets.

# Usage
```
Wallet newWAllet = Wallet.newWallet();
String address = newWAllet.getAddress();
String privateKey = newWAllet.getPrivateKey();

//load from existing private key
Wallet wallet = Wallet.loadFromPrivate("0c276dea6126fc41da8303d13f53fdbf4c5b69c8a0cbe8526a8f56483f1b51e5");
```

Create and sign transaction
```
Wallet fromWallet = Wallet.loadFromPrivate("d20486c9ab8fdfff4d4645563b9ef617d7c69e23d3ded425bf1391acb93b6696");
Wallet toWallet = Wallet.loadFromAddress("0x718863e4fFC6D9CC4c40ba6ABE6EE5A2ec5a395c");
//prepare transaction    
Transaction transaction = TransactionBuilder.create(toWallet, new BigInteger("1"));
//sign
transaction.sign(fromWallet);
//get data
transaction.getEncoded();
transaction.getHash();
```

License
-------
Apache license 2.0
