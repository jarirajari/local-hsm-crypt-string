# local-hsm-crypt-string
Java executable for encrypting and decrypting strings using CLI with PKCS#11 device like SoftHSM

USAGE
```
java -jar target/stringcrypt.jar -e "Using public and private key for encryption and decryption!"

Using crypto provider=SunPKCS11-hsmPkcsConfig using library /usr/lib/softhsm/libsofthsm2.so

INPUT : Using public and private key for encryption and decryption!
OUTPUT: mNdoU9EpSsHUia43I1rhyWPIhU49gCoqCTTsrnD3oI3K0AmB3KXQsLZZd7iSD5RhN7q/d9mLNQfDAkx2wuGVMb8Le47WE+RyMGBYvtX6lPgzGCjZghXSRmd+edtP49h4/8YzqTaDs9AM+R0jeQ/EukDzj/WzkKFhhsm1ud1E7pkTF4ne5bKf6AunfhsfGBB2U/l6qTGZ/mWEno27VW0WokS8SuBWV4XmCI1t3teHM4YcbCl2ujk6HZon2pHtIwfNhVUvrchmeldlNn3WAefqoCU4u7boEgFNgIzUHzwYi4iJh6d4T2wmuUPIJo2UwWoMp9z/7ujz3rttonnFW83sBA==
```

```
java -jar target/stringcrypt.jar -d mNdoU9EpSsHUia43I1rhyWPIhU49gCoqCTTsrnD3oI3K0AmB3KXQsLZZd7iSD5RhN7q/d9mLNQfDAkx2wuGVMb8Le47WE+RyMGBYvtX6lPgzGCjZghXSRmd+edtP49h4/8YzqTaDs9AM+R0jeQ/EukDzj/WzkKFhhsm1ud1E7pkTF4ne5bKf6AunfhsfGBB2U/l6qTGZ/mWEno27VW0WokS8SuBWV4XmCI1t3teHM4YcbCl2ujk6HZon2pHtIwfNhVUvrchmeldlNn3WAefqoCU4u7boEgFNgIzUHzwYi4iJh6d4T2wmuUPIJo2UwWoMp9z/7ujz3rttonnFW83sBA==

Using crypto provider=SunPKCS11-hsmPkcsConfig using library /usr/lib/softhsm/libsofthsm2.so

INPUT : mNdoU9EpSsHUia43I1rhyWPIhU49gCoqCTTsrnD3oI3K0AmB3KXQsLZZd7iSD5RhN7q/d9mLNQfDAkx2wuGVMb8Le47WE+RyMGBYvtX6lPgzGCjZghXSRmd+edtP49h4/8YzqTaDs9AM+R0jeQ/EukDzj/WzkKFhhsm1ud1E7pkTF4ne5bKf6AunfhsfGBB2U/l6qTGZ/mWEno27VW0WokS8SuBWV4XmCI1t3teHM4YcbCl2ujk6HZon2pHtIwfNhVUvrchmeldlNn3WAefqoCU4u7boEgFNgIzUHzwYi4iJh6d4T2wmuUPIJo2UwWoMp9z/7ujz3rttonnFW83sBA==
OUTPUT: Using public and private key for encryption and decryption!
```
