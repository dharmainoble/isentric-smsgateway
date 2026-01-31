//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.utility;


import com.isentric.bulkgateway.manager.LoggerManager;
import org.apache.log4j.Logger;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;

public class JavaSSLCertificate {
    private static final Logger logger = LoggerManager.createLoggerPattern(JavaSSLCertificate.class);
    private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

    public static void retImportCert(String host, int port) throws Exception {
        char[] passphrase = "changeit".toCharArray();
        System.out.println("Opening connection to " + host + ":" + port + "...");
        File file = new File("/home/arun/Documents/rec/MyMaxisKeyStore.jks");
        logger.debug("Loading KeyStore " + file + "...");
        InputStream in = new FileInputStream(file);
        KeyStore ks = KeyStore.getInstance("jks");
        ks.load(in, "changeit".toCharArray());
        in.close();
        SSLContext context = SSLContext.getInstance("SSL");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager defaultTrustManager = (X509TrustManager)tmf.getTrustManagers()[0];
        SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
        context.init((KeyManager[])null, new TrustManager[]{tm}, (SecureRandom)null);
        SSLSocketFactory factory = context.getSocketFactory();
        logger.info("Opening connection to " + host + ":" + port + "...");
        SSLSocket socket = (SSLSocket)factory.createSocket(host, port);
        socket.setSoTimeout(10000);

        try {
            System.out.println("Starting SSL handshake...");
            socket.startHandshake();
            socket.close();
            System.out.println();
            System.out.println("No errors, certificate is already trusted");
        } catch (SSLException e) {
            System.out.println();
            e.printStackTrace(System.out);
        }

        X509Certificate[] chain = tm.getChain();
        if (chain == null) {
            logger.error("Could not obtain server certificate chain");
        } else {
            new BufferedReader(new InputStreamReader(System.in));
            System.out.println();
            logger.info("Server sent " + chain.length + " certificate(s):");
            System.out.println();
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            for(int i = 0; i < chain.length; ++i) {
                X509Certificate cert = chain[i];
                System.out.println(" " + (i + 1) + " Subject " + cert.getSubjectDN());
                System.out.println("   Issuer  " + cert.getIssuerDN());
                sha1.update(cert.getEncoded());
                System.out.println("   sha1    " + toHexString(sha1.digest()));
                md5.update(cert.getEncoded());
                System.out.println("   md5     " + toHexString(md5.digest()));
                System.out.println();
            }

            X509Certificate cert = chain[0];
            String alias = host + "-" + DateUtil.getDateYYYYMMDD(new Date());
            ks.setCertificateEntry(alias, cert);
            OutputStream out = new FileOutputStream("/home/arun/Documents/rec/MyMaxisKeyStore.jks");
            ks.store(out, "changeit".toCharArray());
            out.close();
            System.out.println();
            System.out.println();
            logger.info("Added certificate to keystore [C:\\bulk-cert\\MyMaxisKeyStore.jks using alias '" + alias + "'");
            System.setProperty("javax.net.ssl.trustStore", "/home/arun/Documents/rec/MyMaxisKeyStore.jks");
        }
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 3);

        for(int b : bytes) {
            b &= 255;
            sb.append(HEXDIGITS[b >> 4]);
            sb.append(HEXDIGITS[b & 15]);
            sb.append(' ');
        }

        return sb.toString();
    }

    private static class SavingTrustManager implements X509TrustManager {
        private final X509TrustManager tm;
        private X509Certificate[] chain;

        SavingTrustManager(X509TrustManager tm) {
            this.tm = tm;
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
            // Not used in this context
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
            this.chain = chain;
            try {
                tm.checkServerTrusted(chain, authType);
            } catch (java.security.cert.CertificateException e) {
                // Continue even if the certificate chain is not trusted
                // This allows us to capture and save the certificate
            }
        }

        X509Certificate[] getChain() {
            return chain;
        }
    }
}
