//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.utility;

import com.isentric.bg.constant.EncodingType;
import com.isentric.bg.manager.LoggerManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Hashtable;

public class Encoder {
    private static Logger log = LoggerManager.createLoggerPattern(Encoder.class);
    protected Hashtable s2thash = new Hashtable();
    protected Hashtable t2shash = new Hashtable();
    private static final String HEXINDEX = "0123456789abcdef          ABCDEF";

    public Encoder() {
        try {
            InputStream inputstream = new FileInputStream("c:\\hcutf8.txt");
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "UTF8"));

            String s;
            while((s = bufferedreader.readLine()) != null) {
                if (s.length() != 0 && s.charAt(0) != '#') {
                    this.s2thash.put(s.substring(0, 1).intern(), s.substring(1, 2));

                    for(int i = 1; i < s.length(); ++i) {
                        this.t2shash.put(s.substring(i, i + 1).intern(), s.substring(0, 1));
                    }
                }
            }
        } catch (Exception exception) {
            log.fatal(exception.getCause(), exception);
        }

    }

    public String convertString(String s, int i, int j) {
        StringBuffer stringbuffer = new StringBuffer();
        if (i == EncodingType.HZ) {
            log.info("String before:".concat(String.valueOf(String.valueOf(s))));
            s = this.hz2gb(s);
            log.info("String after:".concat(String.valueOf(String.valueOf(s))));
        }

        for(int k = 0; k < s.length(); ++k) {
            if (i != EncodingType.GB2312 && i != EncodingType.GBK && i != EncodingType.ISO2022CN_GB && i != EncodingType.HZ && i != EncodingType.UNICODE && i != EncodingType.UNICODES && i != EncodingType.UTF8 || j != EncodingType.BIG5 && j != EncodingType.CNS11643 && j != EncodingType.UNICODET && j != EncodingType.ISO2022CN_CNS) {
                if ((i == EncodingType.BIG5 || i == EncodingType.CNS11643 || i == EncodingType.UNICODET || i == EncodingType.UTF8 || i == EncodingType.ISO2022CN_CNS || i == EncodingType.GBK || i == EncodingType.UNICODE) && (j == EncodingType.GB2312 || j == EncodingType.UNICODES || j == EncodingType.ISO2022CN_GB || j == EncodingType.HZ)) {
                    if (this.t2shash.containsKey(s.substring(k, k + 1))) {
                        stringbuffer.append(this.t2shash.get(s.substring(k, k + 1).intern()));
                    } else {
                        stringbuffer.append(s.substring(k, k + 1));
                    }
                } else {
                    stringbuffer.append(s.substring(k, k + 1));
                }
            } else if (this.s2thash.containsKey(s.substring(k, k + 1))) {
                stringbuffer.append(this.s2thash.get(s.substring(k, k + 1).intern()));
            } else {
                stringbuffer.append(s.substring(k, k + 1));
            }
        }

        if (j == EncodingType.HZ) {
            return this.gb2hz(stringbuffer.toString());
        } else {
            return stringbuffer.toString();
        }
    }

    public String hz2gb(String s) {
        byte[] abyte0 = new byte[2];
        byte[] abyte1 = new byte[2];
        StringBuffer stringbuffer = new StringBuffer("");

        try {
            abyte0 = s.getBytes("8859_1");
        } catch (Exception exception) {
            log.fatal(exception.getCause(), exception);
            return s;
        }

        for(int i = 0; i < abyte0.length; ++i) {
            if (abyte0[i] != 126) {
                stringbuffer.append((char)abyte0[i]);
            } else if (abyte0[i + 1] != 123) {
                if (abyte0[i + 1] == 126) {
                    stringbuffer.append('~');
                } else {
                    stringbuffer.append((char)abyte0[i]);
                }
            } else {
                for(i += 2; i < abyte0.length; i += 2) {
                    if (abyte0[i] == 126 && abyte0[i + 1] == 125) {
                        ++i;
                        break;
                    }

                    if (abyte0[i] == 10 || abyte0[i] == 13) {
                        stringbuffer.append((char)abyte0[i]);
                        break;
                    }

                    abyte1[0] = (byte)(abyte0[i] + 128);
                    abyte1[1] = (byte)(abyte0[i + 1] + 128);

                    try {
                        stringbuffer.append(new String(abyte1, "GB2312"));
                    } catch (Exception exception) {
                        log.fatal(exception.getCause(), exception);
                    }
                }
            }
        }

        return stringbuffer.toString();
    }

    public String gb2hz(String s) {
        byte[] abyte0 = new byte[2];
        StringBuffer stringbuffer = new StringBuffer("");

        try {
            abyte0 = s.getBytes("GB2312");
        } catch (Exception exception) {
            log.fatal(exception.getCause(), exception);
            return s;
        }

        for(int i = 0; i < abyte0.length; ++i) {
            if (abyte0[i] >= 0) {
                if (abyte0[i] == 126) {
                    stringbuffer.append("~~");
                } else {
                    stringbuffer.append((char)abyte0[i]);
                }
            } else {
                stringbuffer.append("~{");

                boolean flag1;
                for(flag1 = false; i < abyte0.length; i += 2) {
                    if (abyte0[i] == 10 || abyte0[i] == 13) {
                        stringbuffer.append("~}".concat(String.valueOf(String.valueOf((char)abyte0[i]))));
                        flag1 = true;
                        break;
                    }

                    if (abyte0[i] >= 0) {
                        stringbuffer.append("~}".concat(String.valueOf(String.valueOf((char)abyte0[i]))));
                        flag1 = true;
                        break;
                    }

                    stringbuffer.append((char)(abyte0[i] + 256 - 128));
                    stringbuffer.append((char)(abyte0[i + 1] + 256 - 128));
                }

                if (!flag1) {
                    stringbuffer.append("~}");
                }
            }
        }

        return new String(stringbuffer);
    }

    public void convertFile(String s, String s1, int i, int j) {
        try {
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(s), EncodingType.javaname[i]));
            BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(s1)));

            String s2;
            while((s2 = bufferedreader.readLine()) != null) {
                String testStr = this.convertString(s2, i, j);
                byte[] byteArr = new byte[testStr.length() * 4];
                byteArr = testStr.getBytes(EncodingType.javaname[j]);
                int intByte = 0;
                String hexString = "";
                String totalHexString = "";

                for(int m = 0; m < byteArr.length; ++m) {
                    Byte b = new Byte(byteArr[m]);
                    intByte = b.intValue();
                    if (intByte < 0) {
                        intByte += 256;
                    }

                    hexString = Integer.toHexString(intByte);
                    if (hexString.length() == 1) {
                        hexString = "0".concat(String.valueOf(String.valueOf(hexString)));
                    }

                    if (!hexString.equals("ff") && !hexString.equals("fe")) {
                        totalHexString = String.valueOf(totalHexString) + hexString;
                    }
                }

                log.info("Total Hex String :".concat(totalHexString));
                bufferedwriter.write(SwapBytes(totalHexString));
            }

            bufferedreader.close();
            bufferedwriter.close();
        } catch (Exception exception) {
            log.fatal(exception.getCause(), exception);
        }

    }

    public String convertChinese(String s1, int i, int j) throws Exception {
        int intByte = 0;
        String hexString = "";
        String totalHexString = "";

        try {
            String testStr = this.convertString(s1, i, j);
            byte[] byteArr = new byte[testStr.length() * 4];
            byteArr = testStr.getBytes(EncodingType.javaname[j]);

            for(int m = 0; m < byteArr.length; ++m) {
                Byte b = new Byte(byteArr[m]);
                intByte = b.intValue();
                if (intByte < 0) {
                    intByte += 256;
                }

                hexString = Integer.toHexString(intByte);
                if (hexString.length() == 1) {
                    hexString = "0".concat(String.valueOf(String.valueOf(hexString)));
                }

                totalHexString = String.valueOf(totalHexString) + hexString;
            }

            if (totalHexString.startsWith("fffe")) {
                totalHexString = totalHexString.substring(4);
            }
        } catch (Exception exception) {
            log.fatal(exception.getCause(), exception);
        }

        return SwapBytes(totalHexString);
    }

    public static String SwapBytes(String str) throws Exception {
        StringBuffer strB = new StringBuffer(str);
        int Length = str.length();
        if (Length % 4 != 0) {
            throw new Exception("Invalid byte length, please use big% traditional");
        } else {
            for(int position = 0; position < Length - 3; position += 4) {
                char c1 = strB.charAt(position);
                char c2 = strB.charAt(position + 1);
                strB.setCharAt(position, strB.charAt(position + 2));
                strB.setCharAt(position + 1, strB.charAt(position + 3));
                strB.setCharAt(position + 2, c1);
                strB.setCharAt(position + 3, c2);
            }

            return strB.toString();
        }
    }

    public static byte[] hexToByte(String s) {
        int l = s.length() / 2;
        byte[] data = new byte[l];
        int j = 0;

        for(int i = 0; i < l; ++i) {
            char c = s.charAt(j++);
            int n = "0123456789abcdef          ABCDEF".indexOf(c);
            int b = (n & 15) << 4;
            c = s.charAt(j++);
            n = "0123456789abcdef          ABCDEF".indexOf(c);
            b += n & 15;
            data[i] = (byte)b;
        }

        return data;
    }

    public static String hexStringToUnicode(String s) {
        byte[] b = hexToByte(s);
        ByteArrayInputStream bin = new ByteArrayInputStream(b);
        DataInputStream in = new DataInputStream(bin);

        try {
            return in.readUTF();
        } catch (IOException e) {
            log.fatal(e.getCause(), e);
            return null;
        }
    }

    public static String hexToEncodedString1(String s, String strCharSet) {
        byte[] b = new byte[s.length() / 2];
        String s2 = "";

        try {
            for(int i = 0; i < s.length(); i += 2) {
                b[i / 2] = (byte)Integer.parseInt(s.substring(i, i + 2), 16);
            }

            s2 = new String(b, "big5");
        } catch (Exception ex) {
            log.info(ex.getCause(), ex);
        }

        return s2;
    }

    public static void main(String[] args) {
    }
}
