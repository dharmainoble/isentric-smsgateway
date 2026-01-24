//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.utility;

public class CreditDeductionUtil {
    public static int checkCreditDeduction(int productType, int messageLength) {
        int credit = 1;
        double sepLength = (double)1.0F;
        if (productType == 3) {
            sepLength = (double)256.0F;
        } else if (productType == 4) {
            sepLength = (double)288.0F;
        } else if (productType == 5) {
            sepLength = (double)256.0F;
        } else if (productType == 1) {
            sepLength = (double)70.0F;
        } else if (productType == 6) {
            sepLength = (double)256.0F;
        } else if (productType == 0) {
            if (messageLength < 161) {
                sepLength = (double)160.0F;
            } else {
                sepLength = (double)153.0F;
            }
        } else {
            sepLength = (double)messageLength;
        }

        credit = (short)((int)Math.ceil((double)messageLength / sepLength));
        return credit;
    }
}
