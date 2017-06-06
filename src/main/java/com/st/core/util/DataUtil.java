package com.st.core.util;
import java.math.BigDecimal;
/**
 * Created by Administrator on 2016/8/10.
 */
public class DataUtil {
    /**
     * 比较两个double类型数值大小（后者大 返回正数；前者大负数）
     * @param d1
     * @param d2
     * @return
     * @throws
     */
    public static int doubleCompare(double d1,double d2){
        BigDecimal data1 = BigDecimal.valueOf(d1);
        BigDecimal data2 = BigDecimal.valueOf(d2);
        return (compare(data1, data2));
    }
    public static int compare(BigDecimal val1, BigDecimal val2) {
        int result = 0;
        if (val1.compareTo(val2) < 0) {
            result = 1;
        }
        if (val1.compareTo(val2) > 0) {
            result =-1;
        }
        return result;
    }
}
