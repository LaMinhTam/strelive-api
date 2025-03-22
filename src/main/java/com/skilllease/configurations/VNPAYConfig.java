package com.skilllease.configurations;

import com.skilllease.utils.VNPayUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Data;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@ApplicationScoped
@Data
public class VNPAYConfig {
    @Inject
    @ConfigProperty(name = "payment.vnPay.url")
    private String vnpPayUrl;

    @Inject
    @ConfigProperty(name = "payment.vnPay.returnUrl")
    private String vnpReturnUrl;

    @Inject
    @ConfigProperty(name = "payment.vnPay.tmnCode")
    private String vnpTmnCode;

    @Inject
    @ConfigProperty(name = "payment.vnPay.secretKey")
    private String secretKey;

    @Inject
    @ConfigProperty(name = "payment.vnPay.version")
    private String vnpVersion;

    @Inject
    @ConfigProperty(name = "payment.vnPay.command")
    private String vnpCommand;

    @Inject
    @ConfigProperty(name = "payment.vnPay.orderType")
    private String orderType;

    public Map<String, String> getVNPayConfig() {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", this.vnpVersion);
        vnpParamsMap.put("vnp_Command", this.vnpCommand);
        vnpParamsMap.put("vnp_TmnCode", this.vnpTmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        // Generate random numbers for transaction reference and order info
        vnpParamsMap.put("vnp_TxnRef", VNPayUtil.getRandomNumber(8));
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" + VNPayUtil.getRandomNumber(8));
        vnpParamsMap.put("vnp_OrderType", this.orderType);
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", this.vnpReturnUrl);
        
        // Set the creation and expire dates
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
        calendar.add(Calendar.MINUTE, 15);
        String vnpExpireDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_ExpireDate", vnpExpireDate);
        return vnpParamsMap;
    }
}
