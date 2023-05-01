package org.example.util;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.jcajce.provider.digest.Blake2b;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;
import org.bouncycastle.jcajce.provider.digest.Blake2b;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.bouncycastle.crypto.digests.Blake2bDigest;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class SignUtil {
    public static HashMap<String, String> signature(HashMap<String, String> head) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String query = "";
        List<String> keys = new ArrayList<String>();
        head.put("appkey", CST.appTvkey);
        for (String k : head.keySet()) {
            keys.add(k);
        }
        Collections.sort(keys);
        for (String k : keys) {
            query += k + "=" + URLEncoder.encode(head.get(k), "UTF-8") + "&";
        }
        query = query.substring(0, query.length()-1);
        head.put("sign", signature(query+CST.appTvSpec));
        return head;
    }
    public static HashMap<String, Object> signatureByAndroid(HashMap<String, Object> head) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String query = "";
        List<String> keys = new ArrayList<String>();
        head.put("appkey", CST.appTvkey);
        for (String k : head.keySet()) {
            keys.add(k);
        }
        Collections.sort(keys);
        for (String k : keys) {
            query += k + "=" + URLEncoder.encode(head.get(k).toString(), "UTF-8") + "&";
        }
        query = query.substring(0, query.length()-1);
        head.put("sign", signature(query+CST.appAndroidSpec));
        return head;
    }
    public static void signatureByAndroidWithoutReturn(Map<String, Object> params) throws Exception {
        List<String> keys = new ArrayList<String>(params.keySet());
        params.put("appkey", CST.appTvkey);
        Collections.sort(keys);
        StringJoiner sj = new StringJoiner("&");
        for (String k : keys) {
            sj.add(k + "=" + URLEncoder.encode(params.get(k).toString(), StandardCharsets.UTF_8.name()));
        }
        String query = sj.toString() + CST.appAndroidSpec;
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(query.getBytes(StandardCharsets.UTF_8));
        String sign = String.format("%032x", new BigInteger(1, hashInBytes));
        params.put("sign", sign);
    }
    public static void signatureWithoutReturn(Map<String, String> params) throws Exception {
        List<String> keys = new ArrayList<String>(params.keySet());
        params.put("appkey", CST.appTvkey);
        Collections.sort(keys);
        StringJoiner sj = new StringJoiner("&");
        for (String k : keys) {
            sj.add(k + "=" + URLEncoder.encode(params.get(k), StandardCharsets.UTF_8.name()));
        }
        String query = sj.toString() + CST.appTvSpec;
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(query.getBytes(StandardCharsets.UTF_8));
        String sign = String.format("%032x", new BigInteger(1, hashInBytes));
        params.put("sign", sign);
    }
    public static String signature(String query) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(query.getBytes());
        String hash = DatatypeConverter.printHexBinary(md5.digest());
        return hash.toLowerCase();
    }
    public static String signatureNoFormat(String query) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(query.getBytes());
        String hash = DatatypeConverter.printHexBinary(md5.digest());
        return hash;
    }

    public static String buildQueryUrl(HashMap<String, String> head){
        StringBuffer query = new StringBuffer();
        query.append("?");
        for (Map.Entry<String, String> headItem : head.entrySet()) {
            query.append(headItem.getKey()).append("=").append(headItem.getValue()).append("&");
        }
        return query.substring(0,query.length()-1);
    }


//    public static String clientSign(String data) throws NoSuchAlgorithmException {
//        MessageDigest h1 = MessageDigest.getInstance("SHA-512");
//        MessageDigest h2 = MessageDigest.getInstance("SHA3-512");
//        MessageDigest h3 = MessageDigest.getInstance("SHA-384");
//        MessageDigest h4 = MessageDigest.getInstance("SHA3-384");
//        Blake2b.Blake2b512 h5 = new Blake2b.Blake2b512();
//
//        h1.update(data.getBytes(StandardCharsets.UTF_8));
//        h2.update(toHexString(h1.digest()).getBytes(StandardCharsets.UTF_8));
//        h3.update(toHexString(h2.digest()).getBytes(StandardCharsets.UTF_8));
//        h4.update(toHexString(h3.digest()).getBytes(StandardCharsets.UTF_8));
//        h5.update(toHexString(h4.digest()).getBytes(StandardCharsets.UTF_8));
//
//        return toHexString(h5.digest());
//    }

    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String clientSign(HashMap<String, Object> data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String _str = mapper.writeValueAsString(data);
        String[] algorithms = {"SHA-512", "SHA3-512", "SHA-384", "SHA3-384", "Blake2b-512"};
        for (String algorithm : algorithms) {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithm, "BC");
                md.update(_str.getBytes());
                _str = new String(Hex.encode(md.digest()));
            } catch (Exception e) {
                continue;
            }
        }
        return _str;
    }


    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    public static String clientSign(String data) {
        byte[] hash1 = sha512(data.getBytes());
        byte[] hash2 = sha3(Hex.toHexString(hash1).getBytes());
        byte[] hash3 = sha384(Hex.toHexString(hash2).getBytes());
        byte[] hash4 = sha3_384(Hex.toHexString(hash3).getBytes());
        byte[] hash5 = blake2b(Hex.toHexString(hash4).getBytes());
        return new String(Hex.encode(hash5));
    }

    private static byte[] sha512(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            return md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] sha3(byte[] data) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            MessageDigest md = MessageDigest.getInstance("SHA3-512");
            return md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] sha384(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            return md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] sha3_384(byte[] data) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            MessageDigest md = MessageDigest.getInstance("SHA3-384");
            return md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] blake2b(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("BLAKE2B-512");
            return md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
