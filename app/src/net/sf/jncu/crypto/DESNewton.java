/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 *
 * http://sourceforge.net/projects/jncu
 *
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 *
 */
package net.sf.jncu.crypto;

import net.sf.jncu.util.NumberUtils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Newton DES cryptography.
 *
 * @author moshew
 */
public class DESNewton {

    /**
     * Default Newton key.
     */
    public static final long NEWTON_DEFAULT_KEY = 0x57406860626D7464L;

    private SecretKeyFactory keyFactory;
    private Cipher cipher;
    private static byte[] zero;

    /**
     * Creates a new DES for Newton.
     */
    public DESNewton() {
        super();
    }

    /**
     * Get the cipher.
     *
     * @return the cipher.
     */
    public Cipher getCipher() {
        return cipher;
    }

    /**
     * Initializes this cipher with a key.
     *
     * @param opmode the operation mode of this cipher.
     * @param key    the key.
     */
    public void init(int opmode, long key) {
        try {
            if (cipher == null) {
                cipher = Cipher.getInstance("DES");
            }

            key <<= 1;
            byte[] keyBytes = NumberUtils.toBytes(key);
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKey skey = getKeyFactory().generateSecret(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, skey);

            if (zero == null)
                zero = NumberUtils.toBytes(0L);
            byte[] tmpKeyBytes = cipher.doFinal(zero);
            long tmpKey = NumberUtils.toLong(tmpKeyBytes);
            byte[] tmpKeyBits = DESUtils.toBits(tmpKey);
            DESUtils.oddParity(tmpKeyBits);
            key = tmpKey | DESUtils.fromBits(tmpKeyBits);

            key <<= 1;
            keyBytes = NumberUtils.toBytes(key);
            keySpec = new DESKeySpec(keyBytes);
            skey = keyFactory.generateSecret(keySpec);
            cipher.init(opmode, skey);
        } catch (NoSuchPaddingException nspe) {
            nspe.printStackTrace();
        } catch (BadPaddingException bpe) {
            bpe.printStackTrace();
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        } catch (InvalidKeyException ike) {
            ike.printStackTrace();
        } catch (InvalidKeySpecException ikse) {
            ikse.printStackTrace();
        } catch (IllegalBlockSizeException ibse) {
            ibse.printStackTrace();
        }
    }

    /**
     * Initializes this cipher with a key.
     *
     * @param opmode the operation mode of this cipher.
     * @param key    the key.
     */
    public void init(int opmode, Key key) {
        init(opmode, NumberUtils.toLong(key.getEncoded()));
    }

    /**
     * Initializes this cipher with a key.
     *
     * @param opmode  the operation mode of this cipher.
     * @param keySpec the key specification.
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     */
    public void init(int opmode, KeySpec keySpec) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
            NoSuchPaddingException {
        SecretKey skey = getKeyFactory().generateSecret(keySpec);
        init(opmode, skey);
    }

    /**
     * Initializes this cipher with the default Newton key.
     *
     * @param opmode the operation mode of this cipher.
     */
    public void init(int opmode) {
        init(opmode, NEWTON_DEFAULT_KEY);
    }

    /**
     * Get the secret key factory instance.
     *
     * @return the key factory.
     * @throws NoSuchAlgorithmException
     */
    private SecretKeyFactory getKeyFactory() throws NoSuchAlgorithmException {
        if (keyFactory == null) {
            keyFactory = SecretKeyFactory.getInstance("DES");
        }
        return keyFactory;
    }

    /**
     * Cipher the data.
     *
     * @param data the plain-text.
     * @return the cipher-text.
     */
    public long cipher(long data) {
        byte[] input = NumberUtils.toBytes(data);
        byte[] ciphered = cipher.update(input);
        return NumberUtils.toLong(ciphered);
    }
}
