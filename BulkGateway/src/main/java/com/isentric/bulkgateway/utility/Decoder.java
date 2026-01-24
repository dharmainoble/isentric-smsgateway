//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.utility;

import com.ibm.icu.text.UnicodeSet;

import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Decoder {
    public static final int GB2312 = 0;
    public static final int GBK = 1;
    public static final int HZ = 2;
    public static final int BIG5 = 3;
    public static final int CNS11643 = 4;
    public static final int UTF8 = 5;
    public static final int UNICODE = 6;
    public static final int UNICODET = 7;
    public static final int UNICODES = 8;
    public static final int UTF32 = 9;
    public static final int ISO2022CN = 10;
    public static final int ISO2022CN_CNS = 11;
    public static final int ISO2022CN_GB = 12;
    public static final int ASCII = 13;
    public static final int OTHER = 14;
    private static int TOTALTYPES = 15;
    public static final Integer MAXUNICODE = new Integer(65535);
    private static String[] javaname;
    private static String[] nicename;
    private static String[] htmlname;
    protected Hashtable s2thash;
    protected Hashtable t2shash;
    protected boolean[] specialChars = new boolean[128];
    protected Map e2i = new HashMap();
    protected Map i2e = new HashMap();
    byte[] buffer = new byte[1000];

    public Decoder() {
        this.init();
        this.initialSpecialChar();
        this.s2thash = new Hashtable();
        this.t2shash = new Hashtable();

        try {
            FileInputStream inputstream = new FileInputStream("c:\\hcutf8.txt");
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

            inputstream.close();
            bufferedreader.close();
        } catch (Exception exception) {
            System.err.println(exception);
        }

    }

    public void init() {
        javaname = new String[TOTALTYPES];
        nicename = new String[TOTALTYPES];
        htmlname = new String[TOTALTYPES];
        javaname[0] = "GB2312";
        javaname[2] = "ASCII";
        javaname[1] = "GBK";
        javaname[12] = "ISO2022CN_GB";
        javaname[3] = "BIG5";
        javaname[4] = "EUC-TW";
        javaname[11] = "ISO2022CN_CNS";
        javaname[10] = "ISO2022CN";
        javaname[5] = "UTF8";
        javaname[6] = "Unicode";
        javaname[7] = "Unicode";
        javaname[8] = "Unicode";
        javaname[9] = "UTF-32";
        javaname[13] = "ASCII";
        javaname[14] = "ISO8859_1";
        htmlname[0] = "GB2312";
        htmlname[2] = "HZ-GB-2312";
        htmlname[1] = "GB2312";
        htmlname[12] = "ISO-2022-CN-EXT";
        htmlname[3] = "BIG5";
        htmlname[4] = "EUC-TW";
        htmlname[11] = "ISO-2022-CN-EXT";
        htmlname[10] = "ISO-2022-CN";
        htmlname[5] = "UTF-8";
        htmlname[6] = "UTF-16";
        htmlname[7] = "UTF-16";
        htmlname[8] = "UTF-16";
        htmlname[9] = "UTF-32";
        htmlname[13] = "ASCII";
        htmlname[14] = "ISO8859-1";
        nicename[0] = "GB-2312";
        nicename[2] = "HZ";
        nicename[1] = "GBK";
        nicename[12] = "ISO2022CN-GB";
        nicename[3] = "Big5";
        nicename[4] = "CNS11643";
        nicename[11] = "ISO2022CN-CNS";
        nicename[10] = "ISO2022 CN";
        nicename[5] = "UTF-8";
        nicename[6] = "Unicode";
        nicename[7] = "Unicode (Trad)";
        nicename[8] = "Unicode (Simp)";
        nicename[9] = "UTF-32";
        nicename[13] = "ASCII";
        nicename[14] = "OTHER";
    }

    public String convertString(String dataline, int source_encoding, int target_encoding) {
        StringBuffer outline = new StringBuffer();
        if (source_encoding == 2) {
            dataline = this.hz2gb(dataline);
        }

        for(int lineindex = 0; lineindex < dataline.length(); ++lineindex) {
            if (source_encoding != 0 && source_encoding != 8 && source_encoding != 12 && source_encoding != 1 && source_encoding != 6 && source_encoding != 2 || target_encoding != 3 && target_encoding != 4 && target_encoding != 7 && target_encoding != 11) {
                if ((source_encoding == 3 || source_encoding == 4 || source_encoding == 7 || source_encoding == 11 || source_encoding == 1 || source_encoding == 6) && (target_encoding == 0 || target_encoding == 8 || target_encoding == 12 || target_encoding == 2)) {
                    if (this.t2shash.containsKey(dataline.substring(lineindex, lineindex + 1))) {
                        outline.append(this.t2shash.get(dataline.substring(lineindex, lineindex + 1).intern()));
                    } else {
                        outline.append(dataline.substring(lineindex, lineindex + 1));
                    }
                } else {
                    outline.append(dataline.substring(lineindex, lineindex + 1));
                }
            } else if (this.s2thash.containsKey(dataline.substring(lineindex, lineindex + 1))) {
                outline.append(this.s2thash.get(dataline.substring(lineindex, lineindex + 1).intern()));
            } else {
                outline.append(dataline.substring(lineindex, lineindex + 1));
            }
        }

        if (target_encoding == 2) {
            return this.gb2hz(outline.toString());
        } else {
            return outline.toString();
        }
    }

    public String hz2gb(String hzstring) {
        byte[] hzbytes = new byte[2];
        byte[] gbchar = new byte[2];
        int byteindex = 0;
        StringBuffer gbstring = new StringBuffer("");

        try {
            hzbytes = hzstring.getBytes("8859_1");
        } catch (Exception usee) {
            System.err.println("Exception " + usee.toString());
            return hzstring;
        }

        for(int var10 = 0; var10 < hzbytes.length; ++var10) {
            if (hzbytes[var10] != 126) {
                gbstring.append((char)hzbytes[var10]);
            } else if (hzbytes[var10 + 1] != 123) {
                if (hzbytes[var10 + 1] == 126) {
                    gbstring.append('~');
                } else {
                    gbstring.append((char)hzbytes[var10]);
                }
            } else {
                for(var10 += 2; var10 < hzbytes.length; var10 += 2) {
                    if (hzbytes[var10] == 126 && hzbytes[var10 + 1] == 125) {
                        ++var10;
                        break;
                    }

                    if (hzbytes[var10] == 10 || hzbytes[var10] == 13) {
                        gbstring.append((char)hzbytes[var10]);
                        break;
                    }

                    gbchar[0] = (byte)(hzbytes[var10] + 128);
                    gbchar[1] = (byte)(hzbytes[var10 + 1] + 128);

                    try {
                        gbstring.append(new String(gbchar, "GB2312"));
                    } catch (Exception usee) {
                        System.err.println("Exception " + usee.toString());
                    }
                }
            }
        }

        return gbstring.toString();
    }

    public String gb2hz(String gbstring) {
        byte[] gbbytes = new byte[2];
        boolean terminated = false;
        StringBuffer hzbuffer = new StringBuffer("");

        try {
            gbbytes = gbstring.getBytes("GB2312");
        } catch (Exception usee) {
            System.err.println(usee.toString());
            return gbstring;
        }

        for(int i = 0; i < gbbytes.length; ++i) {
            if (gbbytes[i] >= 0) {
                if (gbbytes[i] == 126) {
                    hzbuffer.append("~~");
                } else {
                    hzbuffer.append((char)gbbytes[i]);
                }
            } else {
                hzbuffer.append("~{");

                for(terminated = false; i < gbbytes.length; i += 2) {
                    if (gbbytes[i] == 10 || gbbytes[i] == 13) {
                        hzbuffer.append("~}" + (char)gbbytes[i]);
                        terminated = true;
                        break;
                    }

                    if (gbbytes[i] >= 0) {
                        hzbuffer.append("~}" + (char)gbbytes[i]);
                        terminated = true;
                        break;
                    }

                    hzbuffer.append((char)(gbbytes[i] + 256 - 128));
                    hzbuffer.append((char)(gbbytes[i + 1] + 256 - 128));
                }

                if (!terminated) {
                    hzbuffer.append("~}");
                }
            }
        }

        return new String(hzbuffer);
    }

    public void convertFile(String sourcefile, String outfile, int source_encoding, int target_encoding) {
        try {
            BufferedReader srcbuffer = new BufferedReader(new InputStreamReader(new FileInputStream(sourcefile), javaname[source_encoding]));
            BufferedWriter outbuffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile), javaname[target_encoding]));

            String dataline;
            while((dataline = srcbuffer.readLine()) != null) {
                String resultline = this.convertString(dataline, source_encoding, target_encoding);
                outbuffer.write(resultline);
                outbuffer.flush();
            }

            srcbuffer.close();
            outbuffer.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }

    }

    public void printBytes(byte[] array, String name) {
        for(int k = 0; k < array.length; ++k) {
            System.out.println(name + "[" + k + "] = " + "0x" + this.byteToHex(array[k]));
        }

    }

    public String byteToHex(byte b) {
        char[] hexDigit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] array = new char[]{hexDigit[b >> 4 & 15], hexDigit[b & 15]};
        return new String(array);
    }

    public String charToHex(char c) {
        byte hi = (byte)(c >>> 8);
        byte lo = (byte)(c & 255);
        return this.byteToHex(hi) + this.byteToHex(lo);
    }

    protected void initialSpecialChar() {
        for(int i = 0; i < 128; ++i) {
            this.specialChars[i] = false;
        }

        this.specialChars[60] = true;
        this.specialChars[62] = true;
        this.specialChars[38] = true;
        this.specialChars[39] = true;
        this.specialChars[34] = true;
        Object[][] entities = new Object[][]{{"#39", new Integer(39)}, {"quot", new Integer(34)}, {"amp", new Integer(38)}, {"lt", new Integer(60)}, {"gt", new Integer(62)}, {"nbsp", new Integer(160)}, {"copy", new Integer(169)}, {"reg", new Integer(174)}, {"Agrave", new Integer(192)}, {"Aacute", new Integer(193)}, {"Acirc", new Integer(194)}, {"Atilde", new Integer(195)}, {"Auml", new Integer(196)}, {"Aring", new Integer(197)}, {"AElig", new Integer(198)}, {"Ccedil", new Integer(199)}, {"Egrave", new Integer(200)}, {"Eacute", new Integer(201)}, {"Ecirc", new Integer(202)}, {"Euml", new Integer(203)}, {"Igrave", new Integer(204)}, {"Iacute", new Integer(205)}, {"Icirc", new Integer(206)}, {"Iuml", new Integer(207)}, {"ETH", new Integer(208)}, {"Ntilde", new Integer(209)}, {"Ograve", new Integer(210)}, {"Oacute", new Integer(211)}, {"Ocirc", new Integer(212)}, {"Otilde", new Integer(213)}, {"Ouml", new Integer(214)}, {"Oslash", new Integer(216)}, {"Ugrave", new Integer(217)}, {"Uacute", new Integer(218)}, {"Ucirc", new Integer(219)}, {"Uuml", new Integer(220)}, {"Yacute", new Integer(221)}, {"THORN", new Integer(222)}, {"szlig", new Integer(223)}, {"agrave", new Integer(224)}, {"aacute", new Integer(225)}, {"acirc", new Integer(226)}, {"atilde", new Integer(227)}, {"auml", new Integer(228)}, {"aring", new Integer(229)}, {"aelig", new Integer(230)}, {"ccedil", new Integer(231)}, {"egrave", new Integer(232)}, {"eacute", new Integer(233)}, {"ecirc", new Integer(234)}, {"euml", new Integer(235)}, {"igrave", new Integer(236)}, {"iacute", new Integer(237)}, {"icirc", new Integer(238)}, {"iuml", new Integer(239)}, {"eth", new Integer(240)}, {"ntilde", new Integer(241)}, {"ograve", new Integer(242)}, {"oacute", new Integer(243)}, {"ocirc", new Integer(244)}, {"otilde", new Integer(245)}, {"ouml", new Integer(246)}, {"oslash", new Integer(248)}, {"ugrave", new Integer(249)}, {"uacute", new Integer(250)}, {"ucirc", new Integer(251)}, {"uuml", new Integer(252)}, {"yacute", new Integer(253)}, {"thorn", new Integer(254)}, {"yuml", new Integer(255)}, {"euro", new Integer(8364)}};

        for(int i = 0; i < entities.length; ++i) {
            this.e2i.put(entities[i][0], entities[i][1]);
            this.i2e.put(entities[i][1], entities[i][0]);
        }

    }

    public int transferJavaToHtml(char[] ch, int start, int length, char[] out) {
        int o = 0;

        for(int i = start; i < start + length; ++i) {
            if (ch[i] <= 127 && !this.specialChars[ch[i]]) {
                out[o] = ch[i];
                ++o;
            } else if (ch[i] == '<') {
                "&lt;".getChars(0, 4, out, o);
                o += 4;
            } else if (ch[i] == '>') {
                "&gt;".getChars(0, 4, out, o);
                o += 4;
            } else if (ch[i] == '&') {
                "&amp;".getChars(0, 5, out, o);
                o += 5;
            } else if (ch[i] == '"') {
                "&#34;".getChars(0, 5, out, o);
                o += 5;
            } else if (ch[i] == '\'') {
                "&#39;".getChars(0, 5, out, o);
                o += 5;
            } else {
                String dec = "&#" + Integer.toString(ch[i]) + ';';
                dec.getChars(0, dec.length(), out, o);
                o += dec.length();
            }
        }

        return o;
    }

    public String transferJavaToHtml(String source) {
        char[] dest = new char[source.length() * 8];
        int newlen = this.transferJavaToHtml(source.toCharArray(), 0, source.length(), dest);
        return new String(dest, 0, newlen);
    }

    public String transferHtmlToJava(String source) {
        StringBuffer resultBuffer = new StringBuffer();
        String entity = null;

        for(int i = 0; i < source.length(); ++i) {
            char ch = source.charAt(i);
            if (ch == '&') {
                int semi = source.indexOf(59, i + 1);
                if (semi == -1) {
                    resultBuffer.append(ch);
                } else {
                    entity = source.substring(i + 1, semi);
                    Integer iso = null;
                    if (entity.charAt(0) == '#') {
                        try {
                            iso = new Integer(entity.substring(1));
                        } catch (NumberFormatException var13) {
                        }
                    } else {
                        iso = (Integer)this.e2i.get(entity);
                    }

                    if (iso != null && iso.compareTo(MAXUNICODE) <= 0) {
                        i = semi;
                        String a = Integer.toHexString(Integer.parseInt(entity.substring(1)));
                        byte[] abyte0 = this.digits2Bytes(a, 16);
                        String s = this.byteToString(abyte0, javaname[6]);

                        try {
                            resultBuffer.append(s);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    } else {
                        resultBuffer.append(ch);
                    }
                }
            } else {
                resultBuffer.append(ch);
            }
        }

        return resultBuffer.toString();
    }

    public byte[] digits2Bytes(String s, int i) {
        if (i == 0) {
            byte[] abyte0 = new byte[s.length()];

            for(int k = 0; k < s.length(); ++k) {
                abyte0[k] = (byte)s.charAt(k);
            }

            return abyte0;
        } else {
            int j = 0;
            int l = this.digitsPerByte(i);
            int i1 = 0;
            int j1 = 0;

            for(int k1 = 0; k1 < s.length(); ++k1) {
                char c = s.charAt(k1);
                int l1 = Character.getNumericValue(c);
                if (l1 >= 0 && l1 < i) {
                    i1 = i1 * i + l1;
                    ++j1;
                    if (j1 >= l) {
                        this.buffer[j++] = (byte)i1;
                        i1 = 0;
                        j1 = 0;
                    }
                } else if (j1 != 0) {
                    this.buffer[j++] = (byte)i1;
                    i1 = 0;
                    j1 = 0;
                }
            }

            if (j1 > 0) {
                this.buffer[j++] = (byte)i1;
            }

            byte[] abyte1 = new byte[j];
            System.arraycopy(this.buffer, 0, abyte1, 0, j);
            return abyte1;
        }
    }

    public int digitsPerByte(int i) {
        if (i == 0) {
            return 1;
        } else {
            int j = 1;

            for(int k = i; k < 256; k *= i) {
                ++j;
            }

            return j;
        }
    }

    public String byteToString(byte[] abyte0, String coding) {
        try {
            if (!coding.equals(javaname[6]) && !coding.equals(javaname[9])) {
                return new String(abyte0, coding);
            }
        } catch (UnsupportedEncodingException var11) {
            return "";
        }

        StringBuffer stringbuffer = new StringBuffer();
        if (coding.equals(javaname[9])) {
            for(int i = 0; i < abyte0.length; i += 4) {
                int k = 255 & abyte0[i];
                int i1 = i + 1 < abyte0.length ? 255 & abyte0[i + 1] : 0;
                int k1 = i + 2 < abyte0.length ? 255 & abyte0[i + 2] : 0;
                int l1 = i + 3 < abyte0.length ? 255 & abyte0[i + 3] : 0;
                long l2 = (long)(k << 24 | i1 << 16 | k1 << 8 | l1);
                if (l2 <= 65535L) {
                    stringbuffer.append((char)((int)l2));
                } else {
                    l2 -= 65536L;
                    stringbuffer.append((char)((int)((l2 >> 10 & 1023L) + 55296L)));
                    stringbuffer.append((char)((int)((l2 & 1023L) + 56320L)));
                }
            }
        } else {
            for(int j = 0; j < abyte0.length; j += 2) {
                int l = 255 & abyte0[j];
                int j1 = j + 1 < abyte0.length ? 255 & abyte0[j + 1] : 0;
                stringbuffer.append((char)(l << 8 | j1));
            }
        }

        return stringbuffer.toString();
    }

    public String convertHex(String s) {
        StringBuffer t = new StringBuffer("677165b95feb8a0a003a82f1570b8b6665b9627f8a8d8aa46bba4e00540d88ab61f775916d8953ca502b6566720670b8684876845df4897f75375b50002c4f4690536b4962b564cb4e0d4e86540465b962a864ca6feb6bba71218f9c002e000d000a");
        char[] strArray = t.toString().toCharArray();
        String ch = "''u";
        String a = "";

        for(int i = 0; i < strArray.length; i += 4) {
            t.insert(i, ch);
        }

        a = t.toString();
        return a;
    }

    public String convertStringW3C(String source, int source_encoding, int target_encoding) {
        String input = this.transferHtmlToJava(source);
        String output = this.convertString(input, source_encoding, target_encoding);
        return this.transferJavaToHtml(output);
    }

    public String insertUtfIdentifier(String text) {
        String utfStr = "";
        int count = 0;
        int startIndex = 0;

        String[] tokens;
        for(tokens = new String[text.length() / 4]; startIndex + 4 <= text.length(); startIndex += 4) {
            tokens[count] = text.substring(startIndex, startIndex + 4);
            ++count;
        }

        for(int i = 0; i < tokens.length; ++i) {
            UnicodeSet unicodeSet = new UnicodeSet("[\\u" + tokens[i] + "]");
            String unicodeStr = unicodeSet.toPattern(false);
            utfStr = utfStr + unicodeStr.substring(1, unicodeStr.length() - 1);
        }

        return utfStr;
    }

    public static void main(String[] argc) {
        try {
            Decoder zhcoder = new Decoder();
            String text = "00460055004E0020519C6C114F014E1A5BB6";
            text = zhcoder.insertUtfIdentifier(text);
            System.out.println("Inserted UTF: " + text);
            System.out.println("Decoded: " + zhcoder.convertStringW3C(text, 7, 6));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String insertUtfIdentifierHex(String text) throws Exception {
        String utfStr = "";
        int count = 0;
        int startIndex = 0;

        String[] tokens;
        for(tokens = new String[text.length() / 4]; startIndex + 4 <= text.length(); startIndex += 4) {
            tokens[count] = text.substring(startIndex, startIndex + 4);
            ++count;
        }

        for(int i = 0; i < tokens.length; ++i) {
            UnicodeSet unicodeSet = new UnicodeSet("[\\u" + tokens[i] + "]");
            String unicodeStr = unicodeSet.toPattern(true);
            utfStr = utfStr + unicodeStr.substring(1, unicodeStr.length() - 1);
        }

        return utfStr;
    }
}
