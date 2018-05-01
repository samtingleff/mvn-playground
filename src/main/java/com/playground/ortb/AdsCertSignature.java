package com.playground.ortb;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class AdsCertSignature {

	public static void main(String[] args) throws Exception {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
		
		keyGen.initialize(new ECGenParameterSpec("secp112r2"));
        SecureRandom random = new SecureRandom();
        keyGen.initialize(112, random);

        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey priv = pair.getPrivate();
        PublicKey pub = pair.getPublic();

        /*
         * Create a Signature object and initialize it with the private key
         */

        Signature dsa = Signature.getInstance("SHA1withECDSA");

        dsa.initSign(priv);

        String str = "This is string to sign";
        byte[] strByte = str.getBytes("UTF-8");
        dsa.update(strByte);

        /*
         * Now that all the data to be signed has been read in, generate a
         * signature for it
         */

        byte[] realSig = dsa.sign();
        String b64 = Base64.getEncoder().encodeToString(realSig);
        System.out.println("Signature: " + b64 + " with length " + b64.length());

        Key privateKey = importPrivateKey("/Users/stingleff/tmp/secret_key.pem");
	}

	private void importPublicKey(String path) {
		
	}

	private static Key importPrivateKey(String path) throws Exception {
		/*byte[] bytes = Files.readAllBytes(Paths.get(path));
		KeyFactory kf = KeyFactory.getInstance("EC");

		ECPrivateKeySpec spec = new ECPrivateKeySpec();
		Key pub = kf.generatePrivate(spec);
		return pub;*/
		return null;
	}
}
