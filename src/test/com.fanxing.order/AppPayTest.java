package com.zwztf.fanxing.order;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipayEncrypt;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.jthe.midstrage.alipay.common.bean.AlipayBaseConfig;
import com.jthe.midstrage.alipay.pay.request.trade.AlipayTradeGoodDetails;
import com.jthe.midstrage.alipay.pay.request.trade.AlipayTradeRefundParam;
import com.jthe.midstrage.alipay.pay.request.trade.AlipayTradeRequest;
import com.jthe.midstrage.weixin.wxpay.bean.order.WxPayMpOrderResult;
import com.jthe.midstrage.weixin.wxpay.bean.request.WxPayRefundRequest;
import com.jthe.midstrage.weixin.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.jthe.midstrage.weixin.wxpay.bean.result.WxPayRefundResult;
import com.jthe.midstrage.weixin.wxpay.config.WxPayConfig;
import com.jthe.midstrage.weixin.wxpay.constant.WxPayConstants;
import com.jthe.midstrage.weixin.wxpay.exception.WxPayException;
import com.jthe.midstrage.weixin.wxpay.service.WxPayService;
import com.zwztf.fanxing.common.test.annotation.WithMockOAuth2User;
import com.zwztf.fanxing.order.api.constant.PayTypeEnum;
import com.zwztf.fanxing.order.api.dto.app.AppOrderCommonDTO;
import com.zwztf.fanxing.order.api.dto.app.AppPayDTO;
import com.zwztf.fanxing.order.api.entity.PayOrder;
import com.zwztf.fanxing.order.service.AppPayService;
import com.zwztf.fanxing.order.service.AppRefundOrderService;
import com.zwztf.fanxing.order.util.PayUtil;
import com.zwztf.fanxing.shop.api.to.WechatPayConfigTO;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lb
 * @version 1.1
 * @date 2021-07-15 10:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FanxingOrderApplication.class)
@Configuration
@WithMockOAuth2User(tenant = "10000001", username = "admin@@2", password = "123456")
public class AppPayTest {

    @Autowired
    private AppPayService appPayService;
    @Autowired
    private PayUtil payUtil;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private AppRefundOrderService appRefundOrderService;

    /**
     * 支付宝网关
     */
    private static final String GATEWAY = "https://openapi.alipay.com/gateway.do";

    private static final String FORMAT_TYPE = "json";

    private static final String CHARSET = "utf-8";

    private static final String SIGN_TYPE = "RSA2";

    private static final String AES = "AES";

    /**
     * 支付宝配置
     *
     * @return
     */
    public static AlipayBaseConfig aliConfig() {
        AlipayBaseConfig config = new AlipayBaseConfig();
        //config.setAppId("2021002169664144");
        //config.setPrivateKey("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDIqGEStNfogm1n+3DJyjeSpyofyhFQH1FHjPuRMZFAvXEPCbBpXQUQqVm2EXvt7E0Y5lVDgljpr2/GxigBUjj5SG7FM0GR0v0WlTmw8k9MQ6zPBXH4yT/+PsWsPWosM8VmH3FYjJ8Gfji62q0y5OYyyaQ4n/nelNPI76ru8zKotnKz+nMdQSmE8uHcV668HoPoU8pqj0DZfDk3IDJzi/mXSlct62vE+q6AHf/G7a+/e5D8gW2Pv7Nx92KCOZL5zKY7BZzxVT/ZRd0wZpTTdnHfafHeHfseEYBIdJR1zuJjx+DEXc0wSnSBdrLlgo+zx+QBLx4a7dDKKkQEP9eyMnatAgMBAAECggEAdJqEbp5wqUjB0x8ml9zPFoXV0MtX7DuGvBGNTv6C7iAoRxjWTyQjAV+oJklavxQGaWXQPn7MtF+ikNwt+zaQrSwOUsJKZDc7o5fsAouM0UCyd0suw+gPK+vGayT1QeKD36h98XsmHlvTE6Jn659gtjq/F7i/Nz09JRNBPcJNugPjESxWOtplgBk/sEe9pRhnr4SUIcVhQMaZhl59FoYAqIkGnG4sk4ixBCWMWC5ufdyccb9msVVzI8mmOuc1Ny1IIHYDv+5oOcMQHbjLDlyvG5ltdDhA7L3aByy/i5SajhmuBIVjTiUHv1vBuNoLHxdkHNnYQC/W4neUuQMqMnwOcQKBgQD8LY4S5r/TPfbUL2JVxIS/mskwO7eWHOmvqIx91i+hsWMZ2SalZ3sVut3YZuiSZMwEI8KKzUaI6duf6gYVz9rjmitdSaL1RHleVxs8z3FILySPVlepnyOSI3MndFb3APEJ1cLjR8ftjc9EDUKXkA+OK79nrbDpkfPfZVhHd2TJkwKBgQDLsu1H1LyKuAekv6Y69GMgEmm/YU0KERUJL/wyGAeSOlHvxsuLOii+uwr3V3Nx3G0RVaQhG5sYdGTvfmEJSafE9LOb+aLpDr2Ifw1jh4fh0LG8dZ0lLYVfESTD4OEXON2WcKOgZlfX4cAAVMveDPS7HnNqbYGUyuV+IiUyI0LmvwKBgQDDqRDf53uUijJWJcG1bDnXdGB/dQYXia1jJnKDPElOupR6vLq4gVbF4Gw6eHYhB+YnnBtYmLcOiexm4iwAhnp+N+NHiVfP12YX5ZvzjbyGVlWWhVaU/C2cOLcjOKg1E9zxVZLVx57+2RWk5mjxscvv8k+bQO/P+fdvMHHTWXaw0wKBgH/DgJS3WHLhrtmk44zC0JHDOtSxIPMZd8pFcLey3rzXG6F7XRu8pOXMx7oP/fcCvN5Mix/BAuy5xL161T5QFoywtx8z2vy3JCZDGpC853u5vGFqm+A/xxGz3Q2HNWetYT8E83yd9KcRj64lgaGLGs1q8hVlDgvXn04X+KCUAVhdAoGBALw5UmuL6zU0IAwTceP1x+eWFjIaa6qdOxIeZ985LDt8XN6oUWRREwr/x7+wNowuDdmJ8fKn2OA3Q6uWNwuICc8uTUJDgUPYV8kfRzMAmKRySxgJZKaBSusMpIsSv9ZKMx9yf+LZTlf7yvu1G2Jws5REAb4re0HW+fBX052anIFr");
        //config.setAlipayPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj+N3vpKKEa6eqwi2a1x01UthiOtkcer1FocMxMFMOECLnZ/Hl4KC/kR8Ad5PKJvmLYa0fzJABBt5eQSKslNItQO1TgmLr9/SKuH0n4H8J3BMLTwfMvM1PtiyqivSF7zaNNBd3pFs9z6AXJaMyuwEu5zXNargOk5Z0GB4nadI+cV2keC7rIn9TA0w4q6EbU9ay/FVpTzT9x30OgIMMQPaC0nUhjTLzZ1okALdUAFCb3aZpXk7gbOmGY5J12thjCghpMTlIT5oOIlZeTHN8ujsBF3252utyxtq0Ptc+yEH6R1eM3p96vOpoejSYByAbs5OMAepE0p43W5zNSbjohsZBwIDAQAB");

        config.setAppId("2021002172654791");
        config.setPrivateKey("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCx4L89SahbFDEXTCGhIv22huwGcvL6UNglc1s9nmLPw/8jYLk0rEYO8gcw3P71ypbJUqP+EV2dAY4L6fDti6oGmO5HZiPXN6X2KFohOMdrUuoX+osJ8dXGAhTSBNlwFurL8mqa5fw8qbtFrz1cydULOZDBg3v8hwFUHwJeXRXJxT6VBa/Y/nH1iov1+6vLN1p40+wa3hq8Fk0Ay/bJCL/pAuBh0KdJFwerOnyLh9x4PR6BOIS64k/CA4p/WsLJNCPJ8PyfCf2t4M/Lg8yyNSy1F4F7j4tGiNXoD/z0UJkEfFSUjnxaaSzqv9ZrSK3DrrzIIOZskI9jEleeevnyUNK1AgMBAAECggEAbmjJa5pXxMjgu8xGp4VXpD9VK5+YECW0NHLI9JNmU/4dVPFJpFc2WTqDmiHio+Au/iGspxxSVg1MBTsdj+T8EYJFjM3qe0EQY52ibDKZHZXmtiGOwgp5HaHXGJFoAfpHnXYIE8OjcGgOVO+0D+87rO77WhJqGFIYUgW5a6ctygH6LHTC9BPuGuVywY/MvzsOjoNShE6gGGuByyTwd/+FHR6t5KTlKWVHBHVZoY0xwOZjUo7hPKvgqjgZYyG11xvQmeUBC5/0sybiqBrYZI4Fg9GwB/HYdEdgIbHR8qnQzf9HdtpuAgGwedAB+QdlCSlWo/rKmP3W9qpZeqbriTUfAQKBgQDjG20eI+6rWwvIttmWDglx/T6ijINlWzCwkqGu7d8wJem32fLyzcQ/unoms24RYzCongy+Yl2w7/Cr6eHfr1TVMj4U8EXOsFjTzGrk5A8hZOlPYJIprzUFOM3WGvI1LicU4aEuYlbgIwKBTv9vwgI881wtpRhFyfnfAp7mzrXXQQKBgQDIgf3RRPMQsBVkWHJz/dye3JMFh3mZYtzmY9CHpoBLGxjWWJynWJas61FnrftNCr1UKARcp53A0m8UW4r+JNsmJ4tdiYrOgLPRSOaE6wm32qkTwXn4kEYZikyE1AvSE3Bus6n37qvgIByP5q20vh0/w74hE8ptIEiYF/PGDzrydQKBgFtVCEj6wlz/PHn3rwF9m6bP5YSRZbY5OheIoKUs3HkMhjV93QpwXeATKlSuDDHJ5iOpjA50mKEznWJFqKTAailjDzx0dF5u5QooR0TNwpf3cSyO9bj10SjMc1sLcySX7vei1aNFwRJaggNmtatIJoZEiGmC34QRef+JmkXQE6kBAoGAP5Z6GFP1geVV42zpXz+zJ+5r8eT0+2APDkG5cEuthCQjzFZt1+SQZGZ8epUCjXxKbtYCDCBcNzqFX79oZRDrLz57RD7KdpL6c/TapmyjuFrwJAPH2rxmftMNUVKuLQFdNr5juO1INNv6MujFTfy86ev58COGnvUOEK4H8VgDKcECgYEArt96tbP5XPbyTQD1U+EY3kfPwpT+kI/l6ARcPgmf5mMbs+X1nSLK1dHOV69neGUSiUf3wRATzlgujqkSA0pjcux0aEGgN9pkNZ1ZkPAnQ2XYaCiwkj2Nu38PLApYklaV/f7/onPScJT3JgfkwHp2zWuqe4ObtUEQ5hhO+mj1IYk=");
        config.setAlipayPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlDnhX++ERS8DicCxH2zFz8m67MjTs2nNlwVq11FIdCnBIcsucQ5f+HjfDSisu7C+ay6Kbkb+/NO0xADtimCrWQutFuUXrdPr+ClLTDtnHOqbFuVPkREjSn8hxGuVtGoa9OzDDBb6gg7EXah6+g0ICYOGp3JXph671b3/Oim9LUN/2nrahrUElw51m5LTUK9BBdK4SZJrIDqP6abd5O+OjmPGoRT6f8gktJTsrdcK9IRAwSpqOKOZjV7eAnTHvCyNn7HbTkd7QLnIMrwFH8Si3yvcm6OZiLyPPV8p6po0mv/icfGMl/f2Tw9bJPZNzly8rVWLPkQ9xgFy1ReR6uNHqQIDAQAB");
        config.setAppAuthToken("202110BBe6c66ca1b644410091d34b7f5bb60X68");

        /*config.setAppId("2021002174635142");
        config.setPrivateKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA7aEitxKaNV2soGXgKu3OCo1isa+rt3rYhPpadC4KZ7dk72bsWzfVEx57K9F2B7fHZiTw0rPjleJzZp+Rb7yWMquuQYi7cVUTeWXj4k00GcoGDRjhUidgn8S0qt7wMvdDhuCsAJ0rBT8BqPx3XIIKcN39Mk1bcjEPZJFey9GQ2QO0aAbUQSrCUQB1BHdPUlk4qEmM3C69dh+V0J0+k+oQ+ExFSh2oy70uAn0F8OWQ/iRFuDa1mLeIBc50WM4j9jitvu3cDiLAG39HuU1F00ojMYzVUW7QyOEmgvbX9OGK9weif68Db1bniH/i+FTaziBXI5ZHsZSxVHBs8ORNQzahjQIDAQAB");
        config.setAlipayPublicKey("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDtoSK3Epo1XaygZeAq7c4KjWKxr6u3etiE+lp0Lgpnt2TvZuxbN9UTHnsr0XYHt8dmJPDSs+OV4nNmn5FvvJYyq65BiLtxVRN5ZePiTTQZygYNGOFSJ2CfxLSq3vAy90OG4KwAnSsFPwGo/Hdcggpw3f0yTVtyMQ9kkV7L0ZDZA7RoBtRBKsJRAHUEd09SWTioSYzcLr12H5XQnT6T6hD4TEVKHajLvS4CfQXw5ZD+JEW4NrWYt4gFznRYziP2OK2+7dwOIsAbf0e5TUXTSiMxjNVRbtDI4SaC9tf04Yr3B6J/rwNvVueIf+L4VNrOIFcjlkexlLFUcGzw5E1DNqGNAgMBAAECggEAVI455BridIrYmlql4aCs6GRW8hMobylvUiIjfaPEYBOJ/wYIpIzBbQQzn+vwq95hPZ93cgJAwVzK44F0JuZcK7ubTgZxbGpxe4keBbCjEWoprQ9lXe5VG4TLJOIfZoNFrADWM4G5hSxeUttDPc+oF3FmXzfXilKav5ZavPvdJtrTFEYHFFinBXB+aAJILf5RcfeCHIgv+FcElv5oTC4dxnm902lOnvCvUjEwJK58LM34Whu/yceDCF6Hau8SdEbMEXT1+USGyl8S1YdlWdoopEwo2j0njBKpMZZjUrDeaIFCBBlX+/JygJNEOdG5JlUJc7SO3oJg/4LnDTtY8d4zoQKBgQD5sMC+O0K8faXhRVQFp5zYWN0yS6iwEUS8vjzIsRqwaSRr3dbQQk6xuLcQcSnzCudpwK3zm90t41NVJVKyDhBWbtw7bNElCpQYy/1UsuVGrmPqY+C+VAvUPviKB1KxRDi3fky+wo8F+0ac/dtjCEuKbaSwmihpqvTot1HDPOzc9wKBgQDzolwuJeBYZhvrCSn1y1WLE4S5MR/HTniyc7zxxhwcn2hwVOz7ZjkFbxeFBdn+bhOdQu2OYC/WZ1DhzDh3OitSvkYQLtwZipM3e6WJTsTpLPRBiaLHRIHVP7HYf6WsBCJ/GgZcrhJIcgYFSS9m0pXVfBdTQnEdkyIKxCDrApvomwKBgCKGhEBJpXK21QcPNQ0LHi70KrHXsF2YJa8LhxAhHRWjlDuuExP89zCv3/BQFYLb0R/YWrVCqhS8SDuPy51/QFcTQTSeWVUL32qGWmMp0XYyivJQGTV4HaKdinRbit2kF0uYFTiTYn9rPVpb58gLz5Y2jv00c74Yu4CqSI38kKwFAoGBANlG7+ycDOLpVXacOpahW03n/1Llk5C80/laLKR2aLXCGHMt2hcuiCXneFNC77Hv24BBovh5odAVXdWxup3h+6cUWAmwsMv6gOZXWD8HR6coV0FnjQsndp6G6oXI+Sham6U8wXXALD/9qI7oQOTzY3r76xVuyNB2WkK9vRm5vu+HAoGBALUzqAA53HGKgNSzPh0qn9Q7xhqziI6q4NFotgnGJ0wO/hzp7tCpP9zipJQsM58M84MKEBdAI/WRKRaaX/nVwZLLFcF77DRpM6PrKTLrZ6A+1nlTBmLr2imGe+uf21pp2p5bUcJ+o5ZVjmplIRQrqCN9dCa7dPJGFE+RFkuT/hEE");
        config.setAlipayPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAir8ElgMuTMQ/kz8LoOb3JpbNl8OzTj6bqoHMcn8G3oTMDANBl9V+1gJit/M46psE15/NIGCbtRYxKaz7e2PJ+MBYEc73KQBnqERROYRwF1cbfcNVZjqT0cA1TUYwvS26KmfkssUeNxOOHg/aH+M3N4Yw2IXLaHLFKgcprsOPp8sNoMLPEtruawNXcLaDpso2n9xtY43XoyDIsD41yZyURt5y8KE1o4np441kOAeXuEkdBRI3hRKznG538et+N6wEKJQ2nAnIs0iqXGOwCTiZRDmDjj9DHx7JeqxRhkiJ3ra1MTh9CX7eyF9awzM61PAn6uhxsIWmN0UCjU01cNXM2QIDAQAB");*/
        return config;
    }

    /**
     * 获取支付宝客户端
     *
     * @return
     */
    public AlipayClient getClient() {
        AlipayBaseConfig conf = aliConfig();
        return new DefaultAlipayClient(GATEWAY, conf.getAppId(), conf.getPrivateKey(), FORMAT_TYPE, CHARSET, conf.getAlipayPublicKey(), SIGN_TYPE, conf.getAppAes(), AES);
    }

    /**
     * 关闭订单
     */
    @Test
    public void closeOrder() {
        try {
            PayOrder payOrder = new PayOrder();
            payOrder.setOrderNo("4200001170202107208591591277");
            payOrder.setPayType(1);
            System.out.println(JSONUtil.toJsonStr(payUtil.cancelOrder(payOrder)));
        } catch (WxPayException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取aes
     *
     * @throws AlipayApiException
     */
    @Test
    public void getAes() throws AlipayApiException {
        AlipayOpenAuthAppAesGetRequest request = new AlipayOpenAuthAppAesGetRequest();
        JSONObject params = new JSONObject();
        params.put("merchant_app_id", "2021002175627783");
        request.setBizContent(params.toJSONString());
        AlipayOpenAuthAppAesGetResponse response = getClient().execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    /**
     * 获取token
     *
     * @throws AlipayApiException
     */
    @Test
    public void getToken() throws AlipayApiException {
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode("0734f7c0fef04a97a8baedf021d2NX66");
        AlipaySystemOauthTokenResponse response = getClient().execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    /**
     * 支付宝获取手机号
     */
    @Test
    public void getMobile() {
        String content = "IFzwCqe7L6Y4CjMF/jFxaX/3MYP32IjDTrgjz0yWa1x8jDatTBilM1vmBTxCofFDCl9lTCQO0w7GTX23O0qWkbFGJRiONaIGh/ht5MdbMsyFfsgI0RsghEw2YOTOCvySjv" +
                "kCBK2xmExWzL5gK8U9cSmXiBQgKQaLEf5WbG4JFQtH+8yzfsgzJo80W++IbHZdKG5ZStbuYvzicU8h16CLTC6hSthni2eocLSauhhaiawCFLOMHpBy82Rog+kMbEyhCH/2Zz3e3KjqPx361g" +
                "nJgROq0iUuGM9VMg44nkd8oWSte7wnV+EdtNkLuboRyNltNucDJC2tJBSDQmU7ihtGDB9r1tpp+R7TrOIHCiYEurY=";
        //判断是否为加密内容
        boolean isDataEncrypted = !content.startsWith("{");
        String result = null;
        if (isDataEncrypted) {
            try {
                result = AlipayEncrypt.decryptContent(content, AES, "UVIfjSfPb5tbyRG74DIWaA==", CHARSET);
            } catch (Exception e) {
                //解密异常, 记录日志
                e.printStackTrace();
            }
        }
        if (result == null) {
            return;
        }
        JSONObject json = JSONObject.parseObject(result);
        System.out.println(json);
    }

    /**
     * 支付宝下单
     */
    @Test
    public void tradeCreate() {
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        AlipayTradeRequest tradeRequest = new AlipayTradeRequest();
        tradeRequest.setBuyerId("2088802557318870");
        tradeRequest.setTotalAmount(new BigDecimal(1));
        tradeRequest.setSubject("用户下单");
        tradeRequest.setOutTradeNo("20486117899340849152");
        request.setNotifyUrl("http://console.zwztf.net:9014/order/app/pay/10000001/alipay/callback");
        List<AlipayTradeGoodDetails> goodDetails = new ArrayList<AlipayTradeGoodDetails>() {{
            AlipayTradeGoodDetails details = new AlipayTradeGoodDetails();
            details.setGoodsName("iPhone12");
            details.setGoodsId("20210020345");
            details.setPrice(new BigDecimal(1));
            details.setQuantity(1);
            add(details);
        }};
        tradeRequest.setGoodDetails(goodDetails);
        AlipayTradeCreateResponse response = null;
        try {
            response = getClient().execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response == null) {
            System.out.println("支付异常");
        }
        System.out.println((String.format("支付宝预支付返回参数：%s", JSONUtil.toJsonStr(response))));
    }

    /**
     * 支付宝退款
     */
    @Test
    public void refund() {
        AlipayBaseConfig conf = aliConfig();
        conf.setAppAuthToken("2021122422001441581443567113");
        AlipayTradeRefundParam param = new AlipayTradeRefundParam();
        param.setRefundAmount(new BigDecimal(2192).divide(new BigDecimal(100)));
        param.setRefundReason("正常退款");
        param.setNotifyUrl("https://fanxing.zwztf.net/api/order/app/pay/10000006/ali/return");
        param.setTradeNo("30526692467671191552");
        param.setOutRequestNo("30526701067219128320");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setNotifyUrl(param.getNotifyUrl());
        request.setBizContent(JSONObject.toJSONString(param));
        if (StringUtils.isNotBlank(conf.getAppAuthToken())) {
            request.putOtherTextParam("app_auth_token", conf.getAppAuthToken());
        }
        AlipayTradeRefundResponse response = null;

        System.out.println(JSONObject.toJSON(request));
        System.out.println(JSONObject.toJSON(param));

        try {

            response = getClient().execute(request);
        } catch (AlipayApiException e) {
            System.out.println(String.format("alipay退款接口调用失败: %s, %s", e.getErrCode(), e.getErrMsg()));
        }
        if (!response.isSuccess()) {
            System.out.println(String.format("alipay[{}]退款请求失败: %s", JSON.toJSONString(response)));
        }
    }

    /**
     * 支付宝支付查询
     */
    @Test
    public void tradeQuery() {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject params = new JSONObject();
        params.put("trade_no", "2021123022001455421456882460");
        request.setBizContent(params.toString());
        AlipayTradeQueryResponse response = null;
        AlipayBaseConfig conf = aliConfig();
        conf.setAppAuthToken("202112BBbd1ce15dc828464481634359e711dX95");
        //conf.setAppAuthToken("202112BB205d1dfd5d63464a96ee7ae8df11dX87");
        if (StringUtils.isNotBlank(conf.getAppAuthToken())) {
            request.putOtherTextParam("app_auth_token", conf.getAppAuthToken());
        }
        try {
            response = getClient().execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (!response.isSuccess()) {
            //log.info("alipay[{}]查询支付成功请求失败: {}", JSON.toJSONString(response));
        }
        System.out.println(JSONUtil.toJsonStr(response));
    }


    /**
     * 查询订单用的券使用的券类型
     */
    @Test
    public void orderCouponQuery() {
        AlipayBaseConfig conf = aliConfig();
        AlipayMarketingCampaignOrderVoucherConsultRequest request = new AlipayMarketingCampaignOrderVoucherConsultRequest();
        request.setBizContent("{" +
                "      \"scene_code\":[" +
                "        \"DEFAULT\"" +
                "      ]," +
                "\"specified_app_id\":\"2021001159604390\"," +
                "\"order_amount\":\"120.87\"," +
                "      \"item_consult_list\":[{" +
                "        \"item_id\":\"20210105003\"," +
                "\"quantity\":\"3\"," +
                "\"price\":\"20.29\"" +
                "        }]" +
                "  }");
        if (StringUtils.isNotBlank(conf.getAppAuthToken())) {
            request.putOtherTextParam("app_auth_token", conf.getAppAuthToken());
        }
        AlipayMarketingCampaignOrderVoucherConsultResponse response = null;
        try {
            response = getClient().execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.println(JSONObject.toJSONString(response));

    }

    /**
     * 支付宝退款查询
     */
    @Test
    public void refundQuery() {
        /**
         * {"out_trade_no":"945259230123871431","trade_no":"2021082722001435661418293783","out_request_no":"945259230123871431"}
         */
        String alipay_trade_no = "2021121422001464785735745086";
        String pay_order_pay_number = "30526688397564727296";
        String refund_order_pay_number = "30528601467010629632";
        AlipayBaseConfig conf = aliConfig();
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        if (null != conf.getAppAuthToken() && !"".equals(conf.getAppAuthToken())) {
            request.putOtherTextParam("app_auth_token", conf.getAppAuthToken());
        }

        JSONObject bizContent = new JSONObject();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(alipay_trade_no)) {
            bizContent.put("trade_no", alipay_trade_no);
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(pay_order_pay_number)) {
            bizContent.put("out_trade_no", pay_order_pay_number);
        }
        bizContent.put("out_request_no", refund_order_pay_number);
        bizContent.put("query_options", new String[]{"gmt_refund_pay", "refund_detail_item_list"});
        request.setBizContent(bizContent.toString());

        AlipayTradeFastpayRefundQueryResponse response = null;
        try {
            response = getClient().execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.println(response);
        if (response == null || !response.isSuccess()) {
            System.out.println("订单退款失败1");
            return;
        }
        if (StrUtil.isBlank(response.getRefundStatus()) || !StrUtil.equalsAnyIgnoreCase(response.getRefundStatus(), "REFUND_SUCCESS")) {
            System.out.println("订单退款失败2");
            return;
        }
        System.out.println("订单退款成功");
    }

    /**
     * 微信下单
     */
    @Test
    public void tradeWxCreate() {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId("wx6cff519a5888b91d");
        wxPayConfig.setMchId("1501584331");
        wxPayConfig.setMchKey("hfztf20804091436kaixinbalalalala");
        wxPayConfig.setNotifyUrl("1111");
        wxPayConfig.setTradeType(WxPayConstants.TradeType.JSAPI);
        wxPayConfig.setSignType("MD5");
        wxPayConfig.setSubAppId("wx589ff20c39346ce3");
        wxPayConfig.setSubMchId("1603542579");
        try {
            WxPayMpOrderResult result = wxPayService.createOrder(WxPayUnifiedOrderRequest.newBuilder()
                    .body("用户下单")
                    .totalFee(1)
                    .spbillCreateIp("1.1.1.1")
                    .notifyUrl(wxPayConfig.getNotifyUrl() + "/wxpay/callback")
                    .tradeType(wxPayConfig.getTradeType())
                    .subOpenid("o_-cx5XxggwRB2ijHWEvecnuIwZA")
                    .outTradeNo("123127983279237912")
                    .build(), wxPayConfig);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    /**
     * 小程序付款接口(支持混合支付)
     */
    @Test
    public void paymentAggregate() {
        AppPayDTO appPay = new AppPayDTO();
        appPay.setPayType(PayTypeEnum.GIFT_CARD.getStatus());
        List<String> orderNoList = new ArrayList<>();
        orderNoList.add("437569880696559348");
        appPay.setOrderNoList(orderNoList);
        try {
            String result = appPayService.payment(appPay);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户操作取消聚合支付订单
     */
    @Test
    public void cancelAggregate() {
        AppOrderCommonDTO appPay = new AppOrderCommonDTO();
        appPay.setOrderNo("437569880696559348");
        appPay.setReason("测试");
        try {
            System.out.println(appRefundOrderService.cancelOrder(appPay));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信退款
     *
     * @throws WxPayException
     */
    @Test
    public void wxRefund() throws WxPayException {
        String shopConfig = "{\"appId\":\"wxf515e5a94a7fac65\",\"mchId\":\"1532970801\",\"mchKey\":\"dkjeiufj390293984490d3kfjoi3i093\"," +
                "\"keyPath\":\"https://img.zwztf.net/10000001/85da49c1b0684b63b6425ca010e9c293.p12\",\"privateKeyPath\":\"https://img.zwztf.net/10000001/2730f8a7d159956b10c02c47cd85b7b9.pem\"," +
                "\"privateCertPath\":\"https://img.zwztf.net/10000001/99ccd2ce06031c2cd742546deaa767af.pem\",\"notifyUrl\":\"http://console.zwztf.net:9014/order/app/pay/10000001\"," +
                "\"payStatus\":1,\"refundStatus\":1,\"transferStatus\":1}";
        String outRefundNo = "30523556350679818240";
        String transactionId = "4200001331202112155038951016";
        WechatPayConfigTO payConfig = JSONObject.parseObject(shopConfig, WechatPayConfigTO.class);
        WxPayConfig wxPayConfig = JSONObject.parseObject(shopConfig, WxPayConfig.class);
        wxPayConfig.setAppId(payConfig.getAppId());
        wxPayConfig.setMchId(payConfig.getMchId());
        wxPayConfig.setMchKey(payConfig.getMchKey());
        wxPayConfig.setNotifyUrl(payConfig.getNotifyUrl());
        wxPayConfig.setTradeType(WxPayConstants.TradeType.JSAPI);
        wxPayConfig.setSignType("MD5");
        wxPayConfig.setKeyPath(payConfig.getKeyPath());
        wxPayConfig.setPrivateKeyPath(payConfig.getPrivateKeyPath());
        wxPayConfig.setPrivateCertPath(payConfig.getPrivateCertPath());
        wxPayConfig.setSubMchId(payConfig.getSubMchId());
        wxPayConfig.setSubAppId(payConfig.getSubAppId());
        WxPayRefundResult result = wxPayService.refund(
                WxPayRefundRequest.newBuilder()
                        .notifyUrl(wxPayConfig.getNotifyUrl() + "/wxrefund/callback")
                        .outRefundNo(outRefundNo)
                        .transactionId(transactionId)
                        .totalFee(100)
                        .refundFee(100)
                        .build(), wxPayConfig);
        System.out.println(result);
    }
}
