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
